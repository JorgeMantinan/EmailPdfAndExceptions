package atos.manolito.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import atos.manolito.entity.Payrollconfig;
import atos.manolito.entity.Salaryrange;
import atos.manolito.entity.UserDataExtended;
import atos.manolito.exceptions.GeneralException;
import atos.manolito.exceptions.MyDataAccessException;
import atos.manolito.utils.Payroll;
import atos.manolito.Constants;
import atos.manolito.Messages;
import atos.manolito.controllers.UserDataRestController;
import atos.manolito.dao.IPayrollconfigDao;
import atos.manolito.dao.ISalaryrangeDao;

@Service
public class PayrollServiceImpl implements IPayrollService {

	@Autowired
	ISalaryrangeDao salaryRangeDao;
	@Autowired
	IPayrollconfigDao payRollConfigDao;
	@Autowired
    Messages messages;
	
	private static final Logger logger = (Logger) LogManager.getLogger(PayrollServiceImpl.class);
	
	// A efectos del cálculo de la nómina se consideran que todos los meses tienen 30 días
	private static final double NUM_DAYS_OF_MONTH = 30; 
	
	/**
	 * Este método devuelve los datos de la nómina de un usuario que se recibe como parámetro asociadas
	 * @author FGS
	 * @since  27/12/2019
	 */
	@Override
	public Payroll generatePayroll(UserDataExtended userDataExtended, LocalDate generationDay) throws Exception {
		Payroll payroll = null;
		Salaryrange salaryRange = null;
		// Retención neta = % retención IRPF - % descuento IRPF. Se pone 0 si la resta da < 0
		double netRetention = 0;
		double grossMonthlySalary = 0;  
		double grossDailySalary = 0;
		// Número de días trabajados en el mes.
		double workedDaysCount = 0;
		LocalDate ldHiredDate = null;
		
		
		ldHiredDate = Payroll.convertToLocalDate(userDataExtended.getHiredDate());
		
		if(ldHiredDate.compareTo(generationDay) <= 0) { // Fue contratado antes de la fecha de generación
			payroll = new Payroll();
			
			grossMonthlySalary =  userDataExtended.getSalary()/ (double) userDataExtended.getPayments();	
			workedDaysCount = getWorkedDaysCount(userDataExtended, generationDay);
			
			grossDailySalary = grossMonthlySalary / NUM_DAYS_OF_MONTH; 
			grossMonthlySalary = grossDailySalary * workedDaysCount;				
						
			payroll.setMonthName(generationDay.getMonth().getDisplayName(TextStyle.FULL, new Locale("es")));
			payroll.setYear(String.valueOf(generationDay.getYear()));
			payroll.setName(userDataExtended.getUserData().getName());
			payroll.setSurname1(userDataExtended.getUserData().getSurname1());
			payroll.setSurname2(userDataExtended.getUserData().getSurname2());
			if (userDataExtended.getNif() != null) {
				payroll.setIdCard(userDataExtended.getNif());
			} else {
				payroll.setIdCard(userDataExtended.getNie());
			}
			payroll.setSsNumber(userDataExtended.getSsNumber());
			
			
			try {
				salaryRange = salaryRangeDao.findSalaryRange(userDataExtended.getSalary());				
			}catch(DataAccessException dae) {
				logger.error(Constants.ERROR, messages.get("ERROR_DATABASE_GET_SALARY_RANGE",null)
						.concat(Constants.TIME.concat(LocalDateTime.now().toString())));
				throw new MyDataAccessException(messages.get("ERROR_DATABASE_GET_SALARY_RANGE",null));
			}
			payroll.setTaxDiscountPercent(userDataExtended.getSpecialCondition().getDiscount());
			payroll.setGrossMonthlySalary(Math.round(grossMonthlySalary * 100.0) / 100.0);
			if (salaryRange != null) {				
				payroll.setTaxRetentionPercent(salaryRange.getRetention());				
				netRetention = payroll.getTaxRetentionPercent() - payroll.getTaxDiscountPercent();
				if (netRetention < 0) {
					netRetention = 0;
				}
				payroll.setNetMonthlySalary(Math.round(payroll.getGrossMonthlySalary() * (1 - netRetention/100.0) * 100.0) /100.0);
			} else {
				logger.error(Constants.ERROR, messages.get("ERROR_DATABASE_GET_SALARY_RANGE",null)
						.concat(Constants.TIME.concat(LocalDateTime.now().toString())));
				throw new GeneralException(messages.get("ERROR_DATABASE_GET_SALARY_RANGE",null));
			}
			
		}
		
		return payroll;
	}

	/**
	 * Este método calcula el número de días que ha trabajado el usuario en un mes, que se recibe
	 * como segundo parámetro del método.
	 * A efectos del cálculo de la nómina se entiende que todos los meses tienen 30 días.
	 * 
	 * @author FGS
	 * @since  30/12/2019
	 * @param userDataExtended
	 * @param payrollDate objeto de tipo LocalDate que representa el mes para el cual se va
	 *   a calcular el número de días trabajados.
	 * @return
	 */
	private double getWorkedDaysCount(UserDataExtended userDataExtended, LocalDate payrollDate) {
		double workedDaysCount = 0;
		LocalDate ldHiredDate = null;
		LocalDate ldFiredDate = null;
		
		ldHiredDate = Payroll.convertToLocalDate(userDataExtended.getHiredDate());

		// No ha trabajado todos los días del mes
		if (ldHiredDate.getYear()== payrollDate.getYear() 
				&& ldHiredDate.getMonthValue() == payrollDate.getMonthValue()
				&& ldHiredDate.getDayOfMonth() > 1 ) {
			if (isFiredThisMonth(userDataExtended,payrollDate)) {
				ldFiredDate = Payroll.convertToLocalDate(userDataExtended.getFiredDate());
				workedDaysCount = ldFiredDate.getDayOfMonth() 
						- ldHiredDate.getDayOfMonth();
			} else {
				workedDaysCount = payrollDate.lengthOfMonth() 
						- ldHiredDate.getDayOfMonth();
			}
		} else { 
			if (isFiredThisMonth(userDataExtended,payrollDate)) {
				ldFiredDate = Payroll.convertToLocalDate(userDataExtended.getFiredDate());
				workedDaysCount = ldFiredDate.getDayOfMonth();
			} else { // Ha trabajado todos los días del mess
				workedDaysCount = NUM_DAYS_OF_MONTH;
			}
		}
		return workedDaysCount;
	}
	
	/**
	 * Este método comprueba si el usuario es despedido en el mes que se pasa como segundo
	 * parámetro.
	 * 
	 * @author FGS
	 * @since  30/12/2019
	 * @param userDataExtended
	 * @param actualDate
	 * @return
	 */
	public boolean isFiredThisMonth(UserDataExtended userDataExtended, LocalDate actualDate) {
		boolean isFiredThisMonth = false;
		
		isFiredThisMonth = userDataExtended.getFiredDate() != null 
				&& Payroll.convertToLocalDate(userDataExtended.getFiredDate()).getYear() == actualDate.getYear()
				&& Payroll.convertToLocalDate(userDataExtended.getFiredDate()).getMonthValue() == actualDate.getMonthValue();
		
		return isFiredThisMonth;
	}
	
	public boolean isHiredThisMonth(UserDataExtended userDataExtended, LocalDate actualDate) {
		boolean isHiredThisMonth = false;
		
		LocalDate ldUserHiredDate = null;
		
		isHiredThisMonth = userDataExtended.getHiredDate() != null 
				&& Payroll.convertToLocalDate(userDataExtended.getHiredDate()).getYear() <= actualDate.getYear()
				&& Payroll.convertToLocalDate(userDataExtended.getHiredDate()).getMonthValue() <= actualDate.getMonthValue();
		
		
		return isHiredThisMonth;
	}

	
	/**
	 * Este método devuelve una lista con el año y mes de todas las nóminas disponibles para un usuario.
	 * El formato es: añoNombreMes. Ejemplo: 2019diciembre.
	 * @author FGS
	 */
	@Override
	public List<String> getPayrollsDatesOfUser(UserDataExtended userDataExtended) throws Exception {
		List<String> listDatesOfUserPayrolls = new ArrayList<String>();
		LocalDate actualPayrollDate = null;
		LocalDate firstPayrollDate = null;
		LocalDate auxDate = null;
	
		// Obtengo la fecha de generación de nómina del mes en curso
		actualPayrollDate = getActualPayrollDate();		
		
		if (userDataExtended.getHiredDate() != null) {
			firstPayrollDate = Payroll.convertToLocalDate(userDataExtended.getHiredDate());	
		}
		
		if (firstPayrollDate != null && actualPayrollDate != null) {
			auxDate = firstPayrollDate;
			auxDate = auxDate.withDayOfMonth(1);
		
			while (auxDate.getYear() < actualPayrollDate.getYear() 
					|| (auxDate.getYear() == actualPayrollDate.getYear() && auxDate.getMonthValue()<actualPayrollDate.getMonthValue())
					|| (auxDate.getYear() == actualPayrollDate.getYear() && auxDate.getMonthValue()==actualPayrollDate.getMonthValue()
					    && auxDate.getDayOfMonth() <= actualPayrollDate.getDayOfMonth() && actualPayrollDate.getDayOfMonth()<=LocalDate.now().getDayOfMonth())) 
				{									
				
				listDatesOfUserPayrolls.add(0, String.valueOf(auxDate.getYear()) 						
						+ auxDate.getMonth().getDisplayName(TextStyle.FULL, new Locale("es")));

				auxDate = auxDate.plusMonths(1);
				
			}
		}

		
		return listDatesOfUserPayrolls;
	}

	/**
	 * Este método devuelve la fecha de generación de la nómina del mes en curso.
	 * 
	 * @author FGS
	 * @since  08/01/2020
	 * 
	 * @return
	 */
	public LocalDate getActualPayrollDate() {
		Payrollconfig payrollDay = null;
		LocalDate actualPayrollDate = null;
		
		try {
			payrollDay = payRollConfigDao.findByKeyField("fechGenNomina");
			if (payrollDay != null) {
				actualPayrollDate = 
						LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), Integer.parseInt(payrollDay.getValue()));			
			}
		} catch (Exception ex) {
			throw new GeneralException(ex.getLocalizedMessage());
		}
		return actualPayrollDate;
	}
	
	
	/**
	 * Este método devuelve true si el usuario que recibe como parámetro tiene alguna nómina para mostrar
	 * y false en caso contrario.
	 * Para que el usuario tenga alguna nómina deben darse las siguientes condiciones:
	 *  - La fecha de contratación no debe ser nula.
	 *  - Si ha sido contratado en el mes en curso, la fecha actual debe ser igual o mayor a la fecha de 
	 *    generación de nómina.
	 *  - Si ha sido contrado con anterioridad al mes en curso, tendrá alguna nómina (la del mes anterior).
	 *  
	 * @author FGS
	 * @since  08/01/2020
	 * @param userDataExtended
	 * @return
	 */
	public boolean hasPayrollToShow(UserDataExtended userDataExtended) {
		boolean hasPayrollToShow = false;
		LocalDate actualDate = LocalDate.now();
		LocalDate actualPayrollDate = null;
		
		actualPayrollDate = getActualPayrollDate();		
		
		hasPayrollToShow = (userDataExtended.getHiredDate() != null) 
				&& ((isHiredThisMonth(userDataExtended,actualDate.minusMonths(1)))
					||
					((isHiredThisMonth(userDataExtended,actualPayrollDate)
					&& actualPayrollDate.compareTo(actualDate) <= 0))
					)
				;
		
		return hasPayrollToShow;
	}
}

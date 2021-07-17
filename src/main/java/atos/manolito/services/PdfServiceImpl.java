package atos.manolito.services;

import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.itextpdf.html2pdf.HtmlConverter;


import atos.manolito.Messages;
import atos.manolito.entity.Payrollconfig;
import atos.manolito.entity.UserDataExtended;
import atos.manolito.exceptions.GeneralException;
import atos.manolito.utils.GenerationDateUtils;
import atos.manolito.utils.Payroll;

@Service
public class PdfServiceImpl implements IPdfService {
	
	static String htmlFinal;
	static String htmlDatos;
	static String pageBreak;
	static String identificativo;
	

	/*
	 * Obtener lista de usuarios del frontal obtener la longitud Pintar la nómina de
	 * cada usuario
	 */

	@Autowired
	Messages messages;

	@Autowired
	IPayrollService payrollService;
	
	@Autowired
	IPayrollconfigService payrollConfigService;
	
	/**
	 * Obtencion de varias nóminas de un listado
	 * 
	 * @branch T3009
	 * @author JMM
	 * @since 18/12/2019
	 * 
	 */
	@Override
	public int generatePdfs(List<UserDataExtended> usersDataExtended) throws Exception {
		htmlFinal = "";
		htmlDatos = "";
		int generatedPayrollsCount = 0;
		pageBreak = "<div style=\"page-break-before:always\">&nbsp;</div>";
		
		Payrollconfig payrollDay = null;
		LocalDate generationPayrollDate = null;
		String generatedFileName = null;
		
		LocalDate generationPayrollLastDate = null;
		// FGS 02/01/2020 Incorporación de la fecha de generación de la nómina
		try {
			payrollDay = payrollConfigService.findByKeyField("fechGenNomina");
			if (payrollDay != null) {
				generationPayrollDate = 
						LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), Integer.parseInt(payrollDay.getValue()));			
			}
		} catch (Exception ex) {
			throw new GeneralException(ex.getLocalizedMessage());
		}		
		
		
		try {
			for (UserDataExtended user : usersDataExtended) {
				generationPayrollLastDate = lastPayrollDate(user, generationPayrollDate);
				if (generationPayrollLastDate != null) {
					
					if (user == usersDataExtended.get(usersDataExtended.size() - 1)) {
						pageBreak = "";
					}
					
					generatedFileName = generatePdf(user,generationPayrollLastDate);
					if (generatedFileName != null) {
					htmlFinal = "<!DOCTYPE html>\r\n" + 
							"<html lang=\"en\">\r\n" + 
							"\r\n" + 
							"<head>\r\n" + 
							"    <meta charset=\"UTF-8\">\r\n" + 
							"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + 
							"    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\r\n" + 
							"    <title>Pdf Nomina</title>\r\n" + 
							"<style>\r\n" + 
							"        table {\r\n" + 
							"            table-layout: fixed;\r\n" + 
							"            width: 100%;\r\n" + 
							"        }\r\n" + 
							"        \r\n" + 
							"        th,\r\n" + 
							"        td {\r\n" + 
							"            width: 100px;\r\n" + 
							"        }\r\n" + 
							"		img {\r\n" + 
							"            display: block;\r\n" + 
							"            margin: auto;\r\n" + 
							"        }" +
							"    </style>" +
							"</head>\r\n" + 
							"\r\n" + 
							"<body>\r\n" +
							htmlDatos +
							"</body>\r\n" + 
							"\r\n" + 
							"</html>";
					

					generatedPayrollsCount++;
					}
					
				}
			}
			
			if (usersDataExtended.size() == 1) {
				generatedFileName = usersDataExtended.get(0).getUserData().getName();
			} else {
				generatedFileName = LocalDate.now() + "_" +
						LocalDateTime.now().getHour() + "_" + LocalDateTime.now().getMinute() + "_" + LocalDateTime.now().getSecond();
			}

			if (generatedPayrollsCount > 0) {
				HtmlConverter.convertToPdf(htmlFinal, new FileOutputStream(generatedFileName + ".pdf"));	
			}
			return generatedPayrollsCount;

		} catch (Exception e) {
			throw new GeneralException(e.getLocalizedMessage());
		}

	}
	
	
	
	/**
	 * Obtencion de varias nóminas de un único usuario.
	 * 
	 * @branch T3018
	 * @author FGS
	 * @since 02/01/2020
	 * @param userDataExtended	Objeto con los datos extendidos del usuario cuya nómina se va a generar.
	 * @param yearMonthPayrolls Lista con los años y meses de las nóminas a generar. Formato: "YYYYMMMM".
	 *    Por ejemplo: "2019agosto" para indicar agosto de 2019.
	 */
	@Override
	public List<String> generatePdfsOfUser(UserDataExtended userDataExtended, List<String> yearMonthPayrolls) throws Exception {
		htmlFinal = "";
		htmlDatos= "";
		pageBreak = "<div style=\"page-break-before:always\">&nbsp;</div>";
		Payrollconfig payrollDay = null;
		LocalDate actualPayrollDate = null; // Fecha de generación de la nómina del mes en curso
		LocalDate generationDatePayroll = null; // Fecha de la nómina a generar.
		String generatedFileName = null;
		List<String> listMonthYearPayrollsGenerated = new ArrayList<String>();
	
		try {
			payrollDay = payrollConfigService.findByKeyField("fechGenNomina");
			if (payrollDay != null) {
				actualPayrollDate = 
						LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), Integer.parseInt(payrollDay.getValue()));			
				
			}
		} catch (Exception ex) {
			throw new GeneralException(ex.getLocalizedMessage());
		}		
		
		try {
			for (String yearMonthPayroll : yearMonthPayrolls) {
				
				yearMonthPayroll = yearMonthPayroll.toLowerCase();
				generationDatePayroll = GenerationDateUtils.generateLocalDate(yearMonthPayroll);
				// Sólo se genera la nómina si la fecha es anterior o igual a la del mes en curso.
				if (generationDatePayroll != null && 
						(generationDatePayroll.isBefore(actualPayrollDate) 
							|| generationDatePayroll.isEqual(actualPayrollDate))) {
					if (yearMonthPayroll == yearMonthPayrolls.get(yearMonthPayrolls.size() - 1)) {
						pageBreak = "";
					}
					generatedFileName = generatePdf(userDataExtended, generationDatePayroll);
					
					if (generatedFileName != null) {
						
						listMonthYearPayrollsGenerated.add(yearMonthPayroll.substring(4) 
								+ " de " + yearMonthPayroll.substring(0, 4));
						htmlFinal = "<!DOCTYPE html>\r\n" + 
								"<html lang=\"en\">\r\n" + 
								"\r\n" + 
								"<head>\r\n" + 
								"    <meta charset=\"UTF-8\">\r\n" + 
								"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\r\n" + 
								"    <meta http-equiv=\"X-UA-Compatible\" content=\"ie=edge\">\r\n" + 
								"    <title>Pdf Nomina</title>\r\n" + 
								"<style>\r\n" + 
								"        table {\r\n" + 
								"            table-layout: fixed;\r\n" + 
								"            width: 100%;\r\n" + 
								"        }\r\n" + 
								"        \r\n" + 
								"        th,\r\n" + 
								"        td {\r\n" + 
								"            width: 100px;\r\n" + 
								"        }\r\n" + 
								"		img {\r\n" + 
								"            display: block;\r\n" + 
								"            margin: auto;\r\n" + 
								"        }" +
								"    </style>" +
								"</head>\r\n" + 
								"\r\n" + 
								"<body>\r\n" +
								htmlDatos +
								"</body>\r\n" + 
								"\r\n" + 
								"</html>";

					}
				}
			}
			
			HtmlConverter.convertToPdf(htmlFinal, new FileOutputStream(LocalDate.now() + "_" +
					LocalDateTime.now().getHour() + "_" + LocalDateTime.now().getMinute() + "_" + LocalDateTime.now().getSecond() + ".pdf"));

		} catch (Exception e) {
			throw new GeneralException(e.getLocalizedMessage());
		}
		return listMonthYearPayrollsGenerated;
	}

	/**
	 * Obtención de una nómina de un usuario pasándole la fecha
	 * Inicialmente no se le pasaba la fecha de generación, sino que se calculaba a partir de la fecha
	 * actual y del día de generación de la nómina.
	 * 
	 * @branch T3018, Método inicial creado en T3009
	 * @author FGS, a partir de lo hecho por JMM.
	 * @since 02/01/2020
	 * 
	 */
	@Override
	public String generatePdf(UserDataExtended userDataExtended, LocalDate generationDate) throws Exception {
		
		Payroll userPayroll = new Payroll();
		LocalDate generationPayrollDate = generationDate;
		String generatedFileName = null;
		
		try {
			userPayroll = payrollService.generatePayroll(userDataExtended, generationPayrollDate);	
		
			if (userPayroll != null) { 
				htmlDatos = htmlDatos +
					" <img src=\"http://localhost:8080/img/logoAtos.png\">\r\n" +
					"<br><br><br>" + 
					"  <table>\r\n" +
					"<tr>\r\n" + 
					"            <td>Nómina Mes y año</td>\r\n" + 
					"            <td>" + userPayroll.getMonthName() + " " + 
					userPayroll.getYear() + "</td>\r\n" + 
					"        </tr>\r\n" + 
					"        <tr>\r\n" + 
					"            <td>Nombre: </td>\r\n" + 
					"            <td>" + userPayroll.getName() + " " + userPayroll.getSurname1() + 
					" " + userPayroll.getSurname2() + "</td>\r\n" + 
					"        </tr>\r\n" + 
					"        <tr>\r\n" + 
					"            <td>DNI/NIE: </td>\r\n" + 
					"            <td>" + userPayroll.getIdCard() + "</td>\r\n" + 
					"        </tr>\r\n" + 
					"        <tr>\r\n" + 
					"            <td>Número Seguridad Social: </td>\r\n" + 
					"            <td>" + userPayroll.getSsNumber() + "</td>\r\n" + 
					"        </tr>\r\n" + 
					"	</table>" + 
					"    <br><br>" + 
					"    <table>\r\n" +
					"        <tr>\r\n" + 
					"            <td>Salario bruto mensual: </td>" + 
					"            <td>" + userPayroll.getGrossMonthlySalary() +"</td>\r\n" + 
					"        </tr>\r\n" + 
					"        <tr>\r\n" + 
					"            <td>Retención IRPF:  </td>" + 
					"            <td>" + userPayroll.getTaxRetentionPercent() + "</td>\r\n" + 
					"        </tr>\r\n" + 
					"        <tr>\r\n" + 
					"            <td>Descuento IRPF: </td>" + 
					"            <td>" + userPayroll.getTaxDiscountPercent() + "</td>\r\n" + 
					"        </tr>\r\n" + 
					"        <tr>\r\n" + 
					"            <td>Salario neto mensual: </td>" + 
					"            <td>" + userPayroll.getNetMonthlySalary() + "</td>\r\n" + 
					"        </tr>" +
					"    </table>\r\n" +
					pageBreak
				;
				generatedFileName = "Nomina_" + userPayroll.getName() + "_" + userPayroll.getSurname1() 
						+ "_" + userPayroll.getSurname2() + "_" + userPayroll.getYear() + "_" + userPayroll.getMonthName();
			} 
			
		} catch (Exception e) {
			throw new GeneralException(e.getLocalizedMessage());
		}
		return generatedFileName;
	}
	
	/**
	 * Este método devuelve un objeto con la fecha de la última nómina disponible para el usuario
	 * en función de la fecha de pago y de la fecha actual.
	 * 
	 * @author FGS
	 * @since 02/01/2020
	 * @param user
	 * @param payrollDate
	 * @return
	 */
	public LocalDate lastPayrollDate(UserDataExtended user, LocalDate generationPayrollDate) {
		Payrollconfig payrollDay = null;
		LocalDate ldUserHiredDate = null;
		LocalDate ldUserFiredDate = null;
		LocalDate actualDate = LocalDate.now();
		LocalDate lastPayrollDate = null;
		
		
		
		if (user.getHiredDate() != null ) {
			ldUserHiredDate = Payroll.convertToLocalDate(user.getHiredDate());			 
		}
		//Se genera nómina si tiene fecha de contratación anterior a la fecha de generación
		if (ldUserHiredDate != null && generationPayrollDate != null && ldUserHiredDate.compareTo(generationPayrollDate) <= 0) {
			// La fecha de generación es menor o igua que la actual o el usuario es despedido este mes
			if ((generationPayrollDate.compareTo(actualDate)<=0) || payrollService.isFiredThisMonth(user, generationPayrollDate)) {				
				lastPayrollDate = generationPayrollDate;
			} else if (payrollService.isHiredThisMonth(user, generationPayrollDate.minusMonths(1))){				
				//Se genera la nómina del mes anterior, en caso de que la tenga
				generationPayrollDate = generationPayrollDate.withDayOfMonth(generationPayrollDate.lengthOfMonth());
				lastPayrollDate = generationPayrollDate.minusMonths(1);
			} 
		}
		
		
		return lastPayrollDate;
	}
}

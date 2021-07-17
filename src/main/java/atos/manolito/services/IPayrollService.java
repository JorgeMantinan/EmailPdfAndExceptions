package atos.manolito.services;

import java.time.LocalDate;
import java.util.List;

import atos.manolito.entity.UserDataExtended;
import atos.manolito.utils.Payroll;

public interface IPayrollService {
	public Payroll generatePayroll(UserDataExtended userDataExtended, LocalDate generationDay) throws Exception;

	//Payroll generatePayroll(UserDataExtended userDataExtended) throws Exception;
	
	List<String> getPayrollsDatesOfUser(UserDataExtended userDataExtended) throws Exception;
	public boolean isFiredThisMonth(UserDataExtended userDataExtended, LocalDate actualDate);
	public boolean isHiredThisMonth(UserDataExtended userDataExtended, LocalDate actualDate);
	public LocalDate getActualPayrollDate();
	public boolean hasPayrollToShow(UserDataExtended userDataExtended);
}

package atos.manolito.services;

import java.time.LocalDate;
import java.util.List;

import atos.manolito.entity.UserDataExtended;

public interface IPdfService {
	
	public int generatePdfs(List<UserDataExtended> usersDataExtended) throws Exception;
	
	List<String> generatePdfsOfUser(UserDataExtended userDataExtended, List<String> yearMonthPayrolls) throws Exception;

	String generatePdf(UserDataExtended userDataExtended, LocalDate generationDatePayroll) throws Exception;
	
}

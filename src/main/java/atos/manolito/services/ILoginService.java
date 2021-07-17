package atos.manolito.services;

import atos.manolito.entity.Login;
import atos.manolito.entity.UserData;

public interface ILoginService {

	public Login findById(int id);
	
//	public Login findByDasId(String dasId);
	
	public Login save(Login login);
	
	public void delete(int id);

	public Login createLogin(UserData user);

}

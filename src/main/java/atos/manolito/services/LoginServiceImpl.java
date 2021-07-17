package atos.manolito.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import atos.manolito.dao.ILoginDao;
import atos.manolito.entity.Login;
import atos.manolito.entity.UserData;

@Service
public class LoginServiceImpl implements ILoginService {

	
	@Autowired
	private ILoginDao loginDao;
	
	@Override
	@Transactional(readOnly = true)
	public Login findById(int id) {
		return loginDao.findById(id).orElse(null);
	}

//	@Override
//	@Transactional(readOnly = true)
//	public Login findByDasId(String dasId) {
//		return loginDao.findByDasId(dasId);	
//	}

	@Override
	public Login save(Login login) {
		return loginDao.save(login);
	}

	@Override
	public void delete(int id) {
		loginDao.deleteById(id);
	}

	@Override
	public Login createLogin(UserData user) {
		Login newLogin = new Login(3,user);
		save(newLogin);
		return newLogin;
	}

}

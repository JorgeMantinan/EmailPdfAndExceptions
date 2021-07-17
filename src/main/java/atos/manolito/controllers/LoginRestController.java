package atos.manolito.controllers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import atos.manolito.Constants;
import atos.manolito.Messages;
import atos.manolito.entity.Login;
import atos.manolito.entity.UserData;
//import atos.manolito.security.constraint.PasswordConstraintValidator;
import atos.manolito.exceptions.GeneralException;
import atos.manolito.exceptions.MyDataAccessException;
import atos.manolito.services.ILoginService;
import atos.manolito.services.IUserDataService;

// DCS 28/10/2019
@CrossOrigin(origins = {"http://desktop-f1dhp23:4200/manolito" })
@RestController
@RequestMapping("manolito")
public class LoginRestController {
	
	@Autowired
	Messages messages;
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
	@Autowired
	private ILoginService loginService;
	
	@Autowired
	private IUserDataService usuarioService;

	// dcs: cambio de contraseña.
	@PutMapping("/activar/{dasId}/{password}")
	public ResponseEntity<?> changePassword(@PathVariable String dasId,
			 @PathVariable String password){ 
		Login login = null;
		UserData userData = null;
		Map<String , Object> response = new HashMap<>();
		// FGS 22/11/19 Introducido para validar el password
		// PasswordConstraintValidator pwdValidator = new PasswordConstraintValidator();
		
		try{
			userData = usuarioService.findByDasId(dasId);
		}catch(DataAccessException dae) {
			// U2009-JMM-27/11/19
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_GET",null));
		// dtg025 
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_DATABASE_INSERT_USERDATA",new Object[] {userData.getDasId()}));
		
		}
		
		if(userData == null) {
			response.put(Constants.MESSAGE, messages.get("NOT_EXIST_USERDATA_BY_DASID", null));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		// fgs: controla que el password en claro cumpla los requisitos.
		// Control cuando se crea por primera vez y cuando se modifica la contraseña al ser bloqueado
		try {
			if (userData.getLogin() == null) {
				login = loginService.createLogin(userData);
			} else {
				login = loginService.findById(userData.getId());
			}
			login.setPassword(password);
			loginService.save(login);
		} catch (DataAccessException dae) {
			// U2009-JMM-27/11/19
			throw new MyDataAccessException(messages.get("ERROR_INVALID_PASSWORD",null));
		}
		try {
			userData.setState(Constants.ACTIVE);
			usuarioService.save(userData);
		} catch (DataAccessException dae) {
			userData.setState(Constants.PEN_ACTIVATION);
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_CHANGE_STATE", new Object[] {dasId}));
			//messages.get("ERROR_CHANGE_STATE")
		} catch (Exception e) {
			userData.setState(Constants.PEN_ACTIVATION);
			throw new GeneralException(messages.get("ERROR_CHANGE_STATE", new Object[] {dasId}));
		};
		
		try {
			login.setPassword(passwordEncoder.encode(password));
			loginService.save(login);
		} catch (DataAccessException dae) {
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_SAVE_PASSWORD", new Object[] {dasId}));
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_SAVE_PASSWORD", new Object[] {dasId}));
		};
		
		return new ResponseEntity<UserData>(userData, HttpStatus.OK);
	}
	
	// dcs: actualizar intentos
	@GetMapping("/login/{dasId}")
	public ResponseEntity<?> updateAttempts(@PathVariable String dasId){
		Login login = null;
		UserData userData = null;
		Map<String , Object> response = new HashMap<>();

		try{
			userData = usuarioService.findByDasId(dasId);
			login = loginService.findById(userData.getId());
		} catch (DataAccessException dae) {
			// U2009-JMM-27/11/19
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_GET",null));
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET",null));
		}
		if(login == null) {
			response.put(Constants.MESSAGE,messages.get("NOT_EXIST_USERDATA_BY_DASID",null));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		// JMM-21/11/19
		if(login.getAttemptsNum() > 0) {
			login.setAttemptsNum(login.getAttemptsNum()-1);
			if(login.getAttemptsNum() == 0) {
				userData.setState(Constants.LOCKED);
				usuarioService.save(userData);
			}
			loginService.save(login);
		}
		return new ResponseEntity<Login>(login, HttpStatus.OK);
	}
	
	// dcs
	@GetMapping("/login/reset/{id}")
	public ResponseEntity<?> resetNum(@PathVariable int id){
		Login login = null;
		// U2009-JMM-27/11/19
		try {
			login = loginService.findById(id);
		}catch(DataAccessException dae){
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_GET",null));
		}
		try{
			login.setAttemptsNum(3);
			loginService.save(login);
		} catch (DataAccessException dae){
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_UPDATE_ATTEMPTS",null));
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_DATABASE_UPDATE_ATTEMPTS",null));
		}
		return new ResponseEntity<Login>(login,HttpStatus.OK);
	}
	
}

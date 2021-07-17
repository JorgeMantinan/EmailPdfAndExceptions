
package atos.manolito.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.Validator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import atos.manolito.Constants;
import atos.manolito.Messages;
import atos.manolito.dao.IPayrollconfigDao;
import atos.manolito.entity.Payrollconfig;
import atos.manolito.entity.UserData;
import atos.manolito.exceptions.GeneralException;
import atos.manolito.exceptions.MyDataAccessException;
import atos.manolito.services.ILoginService;
import atos.manolito.services.IMailService;
import atos.manolito.services.IUserDataService;

@CrossOrigin(origins = {"http://desktop-f1dhp23:4200/manolito" })
@RestController
@RequestMapping("manolito")
@ControllerAdvice
public class UserDataRestController {
		
	String message; // para contener el mensaje de error.
	String error;   // para contener la traza del error
    private static final Logger logger = (Logger) LogManager.getLogger(UserDataRestController.class);

    @Autowired
    Messages messages;
	
	@Autowired
	private ILoginService loginService;
	
	@Autowired
	private IUserDataService userDataService;
	
	@Autowired
	private IMailService mailService;
	
	@Secured({"ROLE_VER USUARIOS"})
	@GetMapping("/usuarios/listar")
	public List<UserData> showUsersData(){
		 return userDataService.findAll();
	}
	
	// dt003-DCS-12/11/2019: Método para paginacion de Usuarios
	@Secured({"ROLE_VER USUARIOS"})
	@GetMapping("/usuarios/listar/pagina/{page}") 
	public Page<UserData> page(@PathVariable int page,
							  @RequestParam(value="size", defaultValue = "5") int size,
							  @RequestParam(value="ordenationBy", defaultValue = "dasId") String ordenationBy,
							  @RequestParam(defaultValue = "true") boolean order,
							  @RequestParam() String dasId,
							  @RequestParam() String name,
							  @RequestParam() String surname1,
							  @RequestParam() String surname2,
							  @RequestParam() String mail,
							  @RequestParam() int state){
		Page<UserData> pages = null;
		try {
			pages = userDataService.search(page,size,ordenationBy, order, dasId, name, surname1, surname2, mail,state);
			// U2009-JMM-28/11/19
		} catch (JDBCConnectionException jdbce) {
			throw new GeneralException(messages.get("ERROR_DATABASE_CONNECT",null));
		} catch (NullPointerException npe) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_USERDATA",null));
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_USERDATA",null));
		}

		return pages;
	} 
	
	/**
	 * @author FGS
	 * @since  15/11/2019
	 * 
	 * @return
	 */
	@Secured({"ROLE_VER USUARIOS", "ROLE_VER DATOS EXTENDIDOS"})
	@GetMapping("/usuarios/listar/usuariosSinDatosExtendidos")
	public List<UserData> showUsersDataWhithoutDataExtended(){
		 return userDataService.findAllWithoutDataExtended();
	}
	
	// dtg008-JMM-11/12/19
	// Eliminado @Secured para crear los datos extendidos del propio usuario sin permisos.
	@GetMapping("/usuarios/{id}")
	public ResponseEntity<?> showUserData(@PathVariable int id) throws Exception{
		
		UserData userData = null;
		Map<String , Object> response = new HashMap<>();
		try{
			userData = userDataService.findById(id);
		}catch(DataAccessException dae) {
			// U2009-JMM-27/11/19
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_GET_USERDATA_WITH_ID",new Object[] {id}));
		}catch(Exception e) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_USERDATA_WITH_ID",new Object[] {id}));
		}
		
		if(userData == null) {
			response.put(Constants.MESSAGE, messages.get("NOT_EXIST_USERDATA_BY_DASID",null));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<UserData>(userData, HttpStatus.OK);
	}
	
	/**
	 * Método introducido para comprobar si un DAS ID ya está en la BD.
	 * 
	 * @author FGS
	 * @since 04/11/2019
	 * @param id
	 * @return
	 */
	@Secured({"ROLE_CREAR USUARIOS", "ROLE_MODIFICAR USUARIOS"})
	@GetMapping("/usuarios/dasId/{dasId}")	
	public boolean existsUser(@PathVariable String dasId){
		
		boolean existsUser = false;
		try{
			existsUser = userDataService.isDasAlreadyInUse(dasId);
		}catch(DataAccessException dae) {
			// U2009-JMM-27/11/19
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_CHECK_DASID",new Object[] {dasId}));
		}
		return existsUser;
	}
	
	/**
	 * Método introducido para comprobar si un DAS ID ya está en la BD
	 * y que funcione también cuando se está modificando.
	 * 
	 * @author FGS
	 * @since 20/12/2019
	 * @param id
	 * @return
	 */
	@Secured({"ROLE_CREAR USUARIOS", "ROLE_MODIFICAR USUARIOS"})
	@GetMapping("/usuarios/das/{dasId}")	
	public boolean existsUserV2(@PathVariable String dasId,
			@RequestParam(value="id", defaultValue="-1") int id){
		
		boolean existsUser = false;
		UserData userInDB = null;
		try{			
			System.out.println("DasId = " + dasId + "  id= " + id);
			userInDB = userDataService.findByDasId(dasId);			
			if (userInDB != null) { // Un usuario distinto está intentando tomar un DAS de otro
				System.out.println("UserInDB.id = " + userInDB.getId() + "  id= " + id);
				if (userInDB.getId() != id) {
					System.out.println("Los ids no coinciden");
					existsUser = true;
				} 
					
			}
		}catch(DataAccessException dae) {
			// U2009-JMM-27/11/19
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_CHECK_DASID",new Object[] {dasId}));
		}
		return existsUser;
	}

	@Secured({"ROLE_CREAR USUARIOS"})
	@PostMapping(value="/usuarios/crear")
	public ResponseEntity<?> create(@Valid  @RequestBody UserData userData, BindingResult result) throws Exception{

		Map<String, Object> response = new HashMap<>();
		boolean isDasUsed = false;

		// FGS 06/11/19 Arreglo para comprobar si existe el DASID y no intentar la persistencia.
		isDasUsed = userDataService.isDasAlreadyInUse(userData.getDasId());

		if(result.hasErrors() || isDasUsed){
			List<String> listErrors = result.getFieldErrors().stream()
					.map(err -> "El campo: '"+ err.getField() + "' "+err.getDefaultMessage()).collect(Collectors.toList());
			if (isDasUsed)
				listErrors.add(0,messages.get("ERROR_VALIDATION_USERDATA_DASID_USED",new Object[] {userData.getDasId()}));

			response.put("error",listErrors);
			response.put(Constants.MESSAGE,messages.get("ERROR_VALIDATION_USERDATA",null));

			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		// Fijamos el estado inicial del usuario y lo guardamos en la BD.
				try {			
					userData.setState(Constants.PEN_ACTIVATION);
					 userDataService.save(userData);			
				} catch (DataAccessException dae) {
					loginService.delete(userData.getLogin().getId());
					message = messages.get("ERROR_DATABASE_CREATE_USERDATA",new Object[] {userData.getDasId()});
					throw new MyDataAccessException(message);
				} catch (NullPointerException npe) {
					loginService.delete(userData.getLogin().getId());
					message = messages.get("ERROR_DATABASE_CREATE_USERDATA",new Object[] {userData.getDasId()});
					throw new NullPointerException(message);
				} catch (Exception e) {
					throw new GeneralException(messages.get("ERROR_DATABASE_CREATE_USERDATA",new Object[] {userData.getDasId()}));
				}						
		// Generamos una contraseña aleatoria para el nuevo usuario.
		
		
		
		String msgMailSent = messages.get("SUCCESS_SEND_MAIL",null);
		// Enviamos el correo al usuario con su DAS ID y su contraseña.
		mailService.sendMail(userData, msgMailSent);
		response.put(Constants.MESSAGE, messages.get("SUCCESS_CREATE_USERDATA",null) + "\n" 
				+ msgMailSent);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	/**
	 * Ese método actualiza los datos de un usuario.
	 * FGS 19/12/2019. Se elimina anotación @Valid y se realiza la validación dentro del método.
	 * Con esta anotación, no entraba en el cuerpo del método cuando existían errores de validación.
	 * @param userData
	 * @param id 
	 * @return
	 * @throws Exception
	 */
	@Secured({"ROLE_MODIFICAR USUARIOS","ROLE_VER USUARIOS"})
	@PutMapping("/usuarios/modificar/{id}")
	public ResponseEntity<?> update(@RequestBody UserData userData, @PathVariable int id) throws Exception {
		UserData userDataInDB;
		Map<String, Object> response = new HashMap<>();
		
		try {
			userDataInDB = userDataService.findById(id);
		} catch (DataAccessException dae) {
			// U2009-JMM-26/11/19
			message = messages.get("ERROR_VALIDATION_USERDATA",new Object[] {userData.getDasId()});
			logger.error(error + "\n\t**" + message);
			throw new MyDataAccessException(message);
		} catch (Exception e) {
			message = messages.get("ERROR_DATABASE_UPDATE_USERDATA",new Object[] {userData.getDasId()});
			throw new GeneralException(message);
		}

		if (userDataInDB == null) {
			response.put(Constants.MESSAGE,messages.get("NOT_EXIST_USERDATA_BY_ID",null));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		// FGS 19/12/19 Realización de validación dentro del método para evitar @Valid
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<UserData>> result = validator.validate(userData);

		
		if (result.size()>0 || (!userDataInDB.getDasId().equalsIgnoreCase(userData.getDasId()) && existsUser(userData.getDasId()))) {
			List<String> errors = new ArrayList<String>();
			for (ConstraintViolation<UserData> cv : result) {
			    errors.add(cv.getMessage());
			}
			// El DAS ha cambiado pero está siendo usado por otro usuario
			if (!userDataInDB.getDasId().equalsIgnoreCase(userData.getDasId()) && existsUser(userData.getDasId())) {
				errors.add("El DAS ID " + userData.getDasId() + " ya está siendo usado");
			}
			response.put(Constants.ERROR, errors);
			response.put(Constants.MESSAGE, messages.get("ERROR_VALIDATION_USERDATA",null));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}

		
		
		if(!userDataInDB.getDasId().equalsIgnoreCase(userData.getDasId()) && !existsUser(userData.getDasId())) {
			mailService.send("admin0@foo.com", userData.getEmail(), messages.get("ACTIVATION_MAIL_SUBJECT_TITLE",null),"Se ha modificado su Das id. Su nuevo Das id es: " + userData.getDasId() + ".");
		}
		
		// JMM-21/11/19
		if (userDataInDB.getState() != Constants.PEN_ACTIVATION){
			if (userData.getState() == Constants.PEN_ACTIVATION){
				userData.getLogin().setAttemptsNum(3);
				String msgMailSent = messages.get("SUCCESS_SEND_MAIL",null);
				// Enviamos el correo al usuario con su DAS ID y su contraseña.
				mailService.sendMail(userData, msgMailSent);
				response.put(Constants.MESSAGE,messages.get("SUCCESS_CREATE_USERDATA",null) + "\n" 
						+ msgMailSent);
				
			}
		}
		
		userDataService.save(userData);
		response.put(Constants.MESSAGE, messages.get("SUCCESS_UPDATE_USERDATA",null));

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	@Secured({"ROLE_ELIMINAR USUARIOS"})
	@DeleteMapping("/usuarios/listar/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
		
		UserData userData;
		Map<String, Object> response = new HashMap<>();
		try {
			// dtg012-JMM-16/12/19
			userData = userDataService.findById(id);
			if (userData.getUserDataExtended() == null) {
				userDataService.delete(id);
			} else {
				response.put(Constants.MESSAGE,messages.get("ERROR_DATABASE_DELETE_USERDATA", null));
				response.put(Constants.ERROR,messages.get("ERROR_DELETE_USERDATA_HAS_USERDATAEXTENDED", null));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.FORBIDDEN);
			}
		} catch (DataAccessException dae) {
			// U2009-JMM-27/11/19
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_DELETE_USERDATA",null));
		}
		
		response.put(Constants.MESSAGE,messages.get("SUCCESS_DELETE_USERDATA",null));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
		
//	// Metodo para crear contraseña aleatoria
//	public String assignPassword() {
//		String password = new String();
//		// Asignacion de la contraseña al usuario:
//		// Número:
//		password += Character.toString((char)((int)49 +(Math.random() * 8)));
//		// Letra en mayúscula:
//		password += Character.toString((char)((int)65 +(Math.random() * 25)));			
//		// caracter especial ("_"):
//		password += Character.toString((char)95);
//		// Letras en mayuscula hasta completar el total del tamaño
//		while(password.length() < 8)
//			password += Character.toString((char)((int)97+(Math.random() * 25)));
//		return password;
//	}
	@Autowired
	private IPayrollconfigDao salarytranches;
	
	@GetMapping("/usuarios/salary/{id}")
	public Payrollconfig salario(@PathVariable int id) {
		Payrollconfig salary = salarytranches.findById(id).orElse(null);
		System.out.println(salary);
		return salary;
	}
	
}

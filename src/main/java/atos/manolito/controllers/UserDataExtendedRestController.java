package atos.manolito.controllers;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
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
import atos.manolito.dao.ISpecialconditionsDao;
import atos.manolito.entity.Payrollconfig;
import atos.manolito.entity.Specialconditions;
import atos.manolito.entity.UserDataExtended;
import atos.manolito.exceptions.GeneralException;
import atos.manolito.exceptions.MyDataAccessException;
import atos.manolito.exceptions.NotFoundException;
import atos.manolito.exceptions.ValidationFieldException;
import atos.manolito.entity.UserData;
import atos.manolito.services.IAddressService;
import atos.manolito.services.IPayrollService;
import atos.manolito.services.IPdfService;
import atos.manolito.services.IPayrollconfigService;
import atos.manolito.services.IUserDataExtendedService;
import atos.manolito.services.IUserDataService;

/**
 * 
 * @author FGS
 * @since  11/11/2019
 *
 */
@CrossOrigin(origins = {"http://desktop-f1dhp23:4200/manolito" })
@RestController
@RequestMapping("manolito")
public class UserDataExtendedRestController {
		
	String mensaje; // para contener el mensaje de error.
	String error;   // para contener la traza del error.
    private static final Logger logger = (Logger) LogManager.
    				getLogger(UserDataExtendedRestController.class);

    @Autowired
    Messages messages;
    
	@Autowired
	IUserDataExtendedService udeService;
	
	@Autowired
	IUserDataService userDataService;
	
	@Autowired
	IAddressService addressService;
	
	@Autowired
	IPdfService pdfService;

	@Autowired
	IPayrollconfigService payrollService;
	
	@Autowired
	ISpecialconditionsDao specialConditions;

	@Autowired
	IPayrollService payrollSrv;


	
	/**
	 * 
	 * @author FGS
	 * @since  11/11/2019
	 * @return
	 */
	//@Secured({"ROLE_VER DATOS EXTENDIDOS"})
	@GetMapping("/usuarios/datosextendidos/listar")
	public List<UserDataExtended> showAll(){		
		return udeService.findAll();
	}
	
	//@Secured({"ROLE_VER DATOS EXTENDIDOS"})
	@GetMapping("/usuarios/datosextendidos/listar/pagina/{page}")
	public Page<UserDataExtended> page(@PathVariable int page,
							  @RequestParam(value="size", defaultValue = "5") int size,
							  @RequestParam(value="ordenationBy", defaultValue = "userData.dasId") String ordenationBy,
							  @RequestParam(defaultValue = "true") boolean order,
							  @RequestParam() String dasId,
							  @RequestParam() String name,
							  @RequestParam() String surname1,
							  @RequestParam() String nie,
							  @RequestParam() String nif,
							  @RequestParam() String passport,
							  @RequestParam() String ssNumber){
		Pageable pageable = null;
		if(order) {
			pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,ordenationBy));
		} else {
			pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC,ordenationBy));
		}
		return udeService.findAll(pageable,dasId,name,surname1,nie,nif,passport,ssNumber);
				
	} 
	
	/**
	 * Este método muestra los datos extendidos (NIF|NIE|pasaporte, num SS,
	 * num IBAN y dirección (o direcciones) de un usuario, en el caso de que
	 * los tenga.
	 * 
	 * @author FGS
	 * @since  11/11/2019
	 * @param id Identificador en Base de Datos del usuario cuyos datos se
	 *    desea recuperar.
	 * @return Un objeto con la respuesta HTTP a la petición. Dicha respuesta
	 *   contendrá los datos extendidos en el caso de que se hayan encontrado.
	 *   Si no existen dichos datos o no se han podido recuperar, se devuelve 
	 *   el código de error HTTP correspondiente.
	 */
	//@Secured({"ROLE_MODIFICAR DATOS EXTENDIDOS"})
	@GetMapping("/usuarios/datosextendidos/{id}")
	public ResponseEntity<?> showUserDataExtended(@PathVariable int id){
		
		UserDataExtended userDE = null;
		Map<String , Object> response = new HashMap<>();
		
		try{
			userDE = udeService.findById(id);
		}catch(DataAccessException dae) {
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_GET_USERDATAEXTENDED",null));
		}
		
		if(userDE == null) {
			response.put(Constants.MESSAGE,messages.get("NOT_FOUND_USERDATAEXTENDED",null));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<UserDataExtended>(userDE, HttpStatus.OK);
	}
	
	/**
	 * Este método crea los datos extendidos de un usuario (NIF|NIE|pasaporte, núm. SS,
	 * núm. IBAN, lista de direcciones). Si hay errores en la validación de alguno de los
	 * campos, o el NIF|NIE|pasaporte o el núm. de la SS no se permite el guardado de los
	 * datos. 
	 * El NIE|NIF|pasaporte y el núm. SS deben ser únicos, por lo que se realizan las
	 * comprobaciones necesarias antes de intentar persistir el objeto.
	 * 
	 * @author FGS
	 * @since  11/11/2019
	 * 
	 * @param userDataExtended Objeto que representa los datos extendidos del usuario 
	 *     que se van a persistir en la BD.
	 * @param result Objeto que contiene el resultado de las validaciones aplicadas
	 *     al objeto UserDataExtended.
	 * @return Un objeto con la respuesta HTTP a la petición.
	 * @throws Exception
	 */
	@PostMapping(value="/usuarios/datosextendidos/crear")
	public ResponseEntity<?> create(@Valid  @RequestBody UserDataExtended userDataExtended, 
			 BindingResult result) throws Exception{ 	
		UserData userData;	
		Map<String, Object> response = new HashMap<>();
		boolean isNifAlreadyInUse = false;
		boolean isNieAlreadyInUse = false;
		boolean isPassportAlreadyInUse = false;
		boolean isSSNumberAlreadyInUse = false;
		boolean isIbanNumberAlreadyInUse = false;
		
		
		// FGS 10/12/19 Ahora es necesario comprobar nif,nie y pasaporte por separado.
		if (userDataExtended != null) {
			if (userDataExtended.getNif() != null) {
				userDataExtended.setNif(userDataExtended.getNif().toUpperCase());
				isNifAlreadyInUse = udeService.isNifAlreadyInUse(userDataExtended.getNif());
			}
			if (userDataExtended.getNie() != null) {
				userDataExtended.setNie(userDataExtended.getNie().toUpperCase());
				isNieAlreadyInUse = udeService.isNieAlreadyInUse(userDataExtended.getNie());
			}
			if (userDataExtended.getPassport() != null) {
				userDataExtended.setPassport(userDataExtended.getPassport().toUpperCase());
				isPassportAlreadyInUse = udeService.isPassportAlreadyInUse(userDataExtended.getPassport());
			}
			
			if(userDataExtended.getSsNumber() != null)
				isSSNumberAlreadyInUse = udeService.isSSNumberAlreadyInUse(userDataExtended.getSsNumber());
			//FGS 11/12/19 Antes no se comprobaba que el iban fuese único.
			if(userDataExtended.getIbanNumber() != null) {
				userDataExtended.setIbanNumber(userDataExtended.getIbanNumber().toUpperCase());
				isIbanNumberAlreadyInUse = udeService.isIbanNumberAlreadyInUse(userDataExtended.getIbanNumber());
			}
		}
			
		if(result.hasErrors() || isNifAlreadyInUse || isNieAlreadyInUse || isPassportAlreadyInUse || isSSNumberAlreadyInUse
				|| isIbanNumberAlreadyInUse){
			String errors="";
			List<String> errorList = result.getFieldErrors().stream()
					.map(err -> "El campo: '"+ err.getField() + "' "+err.getDefaultMessage()).collect(Collectors.toList());
			if (isNifAlreadyInUse)
				errorList.add(0,messages.get("ERROR_VALIDATION_IDENTITYCARD_USED",new Object[] {userDataExtended.getNif()}));
			if (isNieAlreadyInUse)
				errorList.add(0,messages.get("ERROR_VALIDATION_IDENTITYCARD_USED",new Object[] {userDataExtended.getNie()}));
			if (isPassportAlreadyInUse)
				errorList.add(0,messages.get("ERROR_VALIDATION_IDENTITYCARD_USED",new Object[] {userDataExtended.getPassport()}));
			if (isSSNumberAlreadyInUse)
				errorList.add(0,messages.get("ERROR_VALIDATION_SSNUMBER_USED",new Object[] {userDataExtended.getSsNumber()}));
			if (isIbanNumberAlreadyInUse)
				errorList.add(0,messages.get("ERROR_VALIDATION_IBANNUMBER_USED",new Object[] {userDataExtended.getIbanNumber()}));
			
			// U2009-JMM-28/11/19
			for (String err :errorList) {
				/* lineBreak => No se puede pasar un string con codigo regex debido a que en el replace de typescrypt
				 *  tienes que meter un codigo regex para remplazarlo por un salto de linea.
				 */
				//errors = errors + "lineBreak" + err;
				errors = errors + " \n " + err;
			}
			
					
			throw new ValidationFieldException(errors);
		} 
		
		// FGS. Buscamos el usuario cuyos datos extendidos vamos a crear.
		try{		
			userData = userDataService.findById(userDataExtended.getId());			
		} catch (DataAccessException dae) {
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_GET_USERDATA",null));
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_USERDATA",null));
		}
		
		if(userData == null) {
			throw new NotFoundException(messages.get("NOT_EXIST_USERDATA_BY_ID",null));
		}
		// Creamos la relación entre el usuario y sus datos extendidos y persistimos estos.
		try {		
			userDataExtended.setUserData(userData);
			// FGS 26/12/29 la siguiente línea es que genere bien la relación con direcciones.
			if (userDataExtended.getAddresses() != null) {
				userDataExtended.getAddresses().forEach(addr -> 
									{addr.setUserdataextended(userDataExtended);});
			}			
			fixDates(userDataExtended);
			
			udeService.save(userDataExtended);		
		} catch (DataAccessException dae) {
			// U2009-JMM-28/11/19
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_SAVE_USERDATA",null));
		} catch (NullPointerException npe) {
			throw new NullPointerException(messages.get("ERROR_DATABASE_SAVE_USERDATA",null));
		}
		
				
		response.put(Constants.MESSAGE,messages.get("SUCCESS_SAVE_USERDATAEXTENDED",null));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	/**
	 * Este método permite cambiar los datos extendidos del usuario.
	 * 
	 * @author FGS
	 * @since  13/11/2019
	 * @param userDataExtended Objeto con los nuevos datos extendidos del usuario.
	 * @param id Identificador en BD del usuario.
	 * @param result objeto que contiene el resultado de las validaciones aplicadas
	 *     al objeto UserDataExtended.
	 * @return Un objeto con la respuesta HTTP a la petición.
	 */
	@PutMapping("/usuarios/datosextendidos/modificar/{id}")
	public ResponseEntity<?> update(@RequestBody UserDataExtended userDataExtended,
			@PathVariable int id) {
		
		UserDataExtended udeInDB;
		Map<String, Object> response = new HashMap<>();
			
		boolean isNifAlreadyInUse = false;
		boolean isNieAlreadyInUse = false;
		boolean isPassportAlreadyInUse = false;
		boolean isSSNumberAlreadyInUse = false;
		boolean isIbanNumberAlreadyInUse = false;
				
		try {
			udeInDB = udeService.findById(id);
		} catch (DataAccessException dae) {
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_GET_USERDATAEXTENDED",null));
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_USERDATAEXTENDED",null));
		}
		
		if (udeInDB == null) {
			response.put(Constants.MESSAGE,messages.get("NOT_FOUND_USERDATAEXTENDED",null));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		// FGS 14/11/19 Para comprobar si NIF/NIE o pasaporte y el número SS ya están en el sistema.
		if (userDataExtended != null) {
			// He cambiado el NIE/NIF/pasaporte del usuario -> debo comprobar si el nuevo ya existe.
			if (userDataExtended.getNif()!= null && userDataExtended.getNif() != "" &&					
					!userDataExtended.getNif().equals(udeInDB.getNif())) {	
				userDataExtended.setNif(userDataExtended.getNif().toUpperCase());
				isNifAlreadyInUse = udeService.isNifAlreadyInUse(userDataExtended.getNif());	
			}
			// FGS 11/12/19 Ahora NIF/NIE/pasaporte están en un campo distinto => Necesitan comparaciones diferenciadas.
			if (userDataExtended.getNie()!= null && userDataExtended.getNie() != "" &&					
					!userDataExtended.getNie().equals(udeInDB.getNie())) {				
				userDataExtended.setNie(userDataExtended.getNie().toUpperCase());
				isNieAlreadyInUse = udeService.isNieAlreadyInUse(userDataExtended.getNie());	
			}
			if (userDataExtended.getPassport()!= null && userDataExtended.getPassport() != "" &&					
					!userDataExtended.getPassport().equals(udeInDB.getPassport())) {			
				userDataExtended.setPassport(userDataExtended.getPassport().toUpperCase());
				isPassportAlreadyInUse = udeService.isPassportAlreadyInUse(userDataExtended.getPassport());	
			}
			// He cambiado el núm. de la SS del usuario - > debo comprobar si el nuevo ya existe.
			if (userDataExtended.getSsNumber()!= null 
					&& !userDataExtended.getSsNumber().equals(udeInDB.getSsNumber())) {
				isSSNumberAlreadyInUse = udeService.isSSNumberAlreadyInUse(userDataExtended.getSsNumber());	
			}		
			if (userDataExtended.getIbanNumber()!= null 
					&& !userDataExtended.getIbanNumber().equals(udeInDB.getIbanNumber())) {
				userDataExtended.setIbanNumber(userDataExtended.getIbanNumber().toUpperCase());
				isIbanNumberAlreadyInUse = udeService.isIbanNumberAlreadyInUse(userDataExtended.getIbanNumber());	
			}
		}
	
		// FGS 26/12/19 Realización de validación dentro del método para evitar @Valid
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<UserDataExtended>> result = validator.validate(userDataExtended);
		
		if(result.size()>0 || isNifAlreadyInUse || isNieAlreadyInUse || isPassportAlreadyInUse 
				|| isSSNumberAlreadyInUse || isIbanNumberAlreadyInUse){
			String errors="";
			List<String> errorList = new ArrayList<String>();
			for (ConstraintViolation<UserDataExtended> cv : result) {
			    errorList.add(cv.getMessage());
			}
			if (isNifAlreadyInUse)
				errorList.add(0,messages.get("ERROR_VALIDATION_IDENTITYCARD_USED",new Object[] {userDataExtended.getNif()}));
			if (isNieAlreadyInUse)
				errorList.add(0,messages.get("ERROR_VALIDATION_IDENTITYCARD_USED",new Object[] {userDataExtended.getNie()}));
			if (isPassportAlreadyInUse)
				errorList.add(0,messages.get("ERROR_VALIDATION_IDENTITYCARD_USED",new Object[] {userDataExtended.getPassport()}));
			if (isSSNumberAlreadyInUse)
				errorList.add(0,messages.get("ERROR_VALIDATION_SSNUMBER_USED",new Object[] {userDataExtended.getSsNumber()}));
			if (isIbanNumberAlreadyInUse)
				errorList.add(0,messages.get("ERROR_VALIDATION_IBANNUMBER_USED",new Object[] {userDataExtended.getIbanNumber()}));

			for (String err :errorList) {
				/* lineBreak => No se puede pasar un string con codigo regex debido a que en el replace de typescrypt
				 *  tienes que meter un codigo regex para remplazarlo por un salto de linea.
				 */
				//errors = errors + "lineBreak" + err;
				errors = errors + " \n " + err;
			}
			
			throw new ValidationFieldException(errors);
		}
		

		// FGS 26/12/29 la siguiente línea es para que genere bien la relación con direcciones.
		if (userDataExtended.getAddresses() != null) {
			userDataExtended.getAddresses().forEach(addr -> 
								{addr.setUserdataextended(userDataExtended);});
		}		
		fixDates(userDataExtended);
		
		udeService.save(userDataExtended);
		
		response.put(Constants.MESSAGE,messages.get("SUCCESS_SAVE_USERDATAEXTENDED",null));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	/**
	 * 
	 * @author FGS
	 * @since  12/11/2019
	 * 
	 * @param id Identificador en BD del usuario.
	 * @return Un objeto con la respuesta HTTP a la petición.
	 */
	@Secured({"ROLE_ELIMINAR DATOS EXTENDIDOS"})
	@DeleteMapping("/usuarios/datosextendidos/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		
		try {
			udeService.delete(id);
		} catch (Exception dae) {
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_DELETE_USERDATAEXTENDED",null));
		}
		
		response.put(Constants.MESSAGE,messages.get("SUCCESS_DELETE_USERDATAEXTENDED",null));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	/**
	 * Descarga de una o más nóminas
	 * 
	 * @author JMM
	 * @since  23/12/2019
	 * 
	 * @return Un objeto con la respuesta HTTP a la petición.
	 */
	//@Secured({"ROLE_VER NOMINAS"})
	@PostMapping("/usuarios/datosextendidos/nominas/descarga")
	public ResponseEntity<?> downloadPayrolls(@RequestBody List<UserDataExtended> usersDataExtended) {
		Map<String, Object> response = new HashMap<>();
		int generatedPayrollsCount = 0;
		
		try {
			generatedPayrollsCount = pdfService.generatePdfs(usersDataExtended);
		} catch (Exception e) {
			throw new MyDataAccessException(messages.get("ERROR_GENERATE_PDF",null));
		}
		
		if (usersDataExtended.size() == 1) {
			if (generatedPayrollsCount == 0)
				response.put(Constants.MESSAGE,"No se ha podido generar el PDF de la nómina");
			else
				response.put(Constants.MESSAGE,"PDF de nómina creado con éxito");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);	
		}
		response.put(Constants.MESSAGE,"Se han generado " + generatedPayrollsCount + " PDF con nómina");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	/**
	 * Descarga de varias nóminas de un usuario. Recibe el identificador, en BBDD del usuario y un
	 * listado con las nóminas a generar. Cada elemento de esta lista tiene el siguiente formato:
	 * añoMes. Por ejemplo: "2019enero" representa la nómina de enero de 2019.
	 * 
	 * @author FGS
	 * @since  02/01/2020
	 * 
	 * @return Un objeto con la respuesta HTTP a la petición.
	 */
	@PostMapping("/usuarios/datosextendidos/nominas/descarga/variosmeses")
	public ResponseEntity<?> downloadPayrollsOfUser(			
			@RequestParam(value="user_id") int userId, 
			@RequestBody List<String> yearMonthPayrolls) {
		
		Map<String, Object> response = new HashMap<>();
		List<String> listYearMonthPayrollsGenerated = null;
		String message = null;
		UserDataExtended udeInDB = null;
		
		try {
			udeInDB = udeService.findById(userId);
		} catch (DataAccessException dae) {
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_GET_USERDATAEXTENDED",null));
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_USERDATAEXTENDED",null));
		}
		
		if (udeInDB == null) {
			response.put(Constants.MESSAGE,messages.get("NOT_FOUND_USERDATAEXTENDED",null));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}		
		
		try {
			listYearMonthPayrollsGenerated = pdfService.generatePdfsOfUser(udeInDB, yearMonthPayrolls);
		} catch (Exception e) {
			throw new MyDataAccessException(messages.get("ERROR_GENERATE_PDF",null));
		}
		
		if (listYearMonthPayrollsGenerated.size()>0) {
			message = "Se han generado las siguientes nóminas: \n";
			for (String listItem: listYearMonthPayrollsGenerated) {
				message += listItem + " ";
			}			
		} else {
			message = "No se ha podido generar ninguna nómina";
		}
		response.put(Constants.MESSAGE,message);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	/**
	 * Método introducido para comprobar si un número identificador de usuario
	 * (NIF, NIE o pasaporte) ya está guardado en el sistema.
	 * Si no se pasa el tipo de identificador, se  toma el 1 (tipo NIF) como 
	 * valor por defecto. 
	 * 16/12/19: Se ha modificado para separar en distintos atributos nif, nie
	 * y pasaporte. Si no se recibe el identificador, se devuelve false.
	 * Si el identificador no se corresponde con el tipo, se devuelve false.
	 * 
	 * @author FGS
	 * @since 14/11/2019
	 * @param id_card Cadena con el nif/nie/pasaporte cuya existencia se va a 
	 * 		comprobar.
	 * @param type_card Número con el tipo de identificador. nif = 1, nie = 2
	 *     y pasaporte = 3.
	 * @param userId Entero con el identificador en BBDD del usuario que realiza la 
	 *    petición. 
	 * @return true si el identificador pasado existe en el sistema y false en 
	 *     caso contrario.
	 */
	//@Secured({"ROLE_CREAR DATOS EXTENDIDOS", "ROLE_MODIFICAR DATOS EXTENDIDOS"})
	@GetMapping("/usuarios/datosextendidos/idcard")	
	public boolean existsIdCard(@RequestParam(value="id_card", required=false) String idCard,
			@RequestParam(value="type_card",required=false, defaultValue="1") int typeCard,
			@RequestParam(value="id", defaultValue="-1") int userId){
		
		boolean existsIdCard = false;

		
		switch (typeCard) {
		case 1: // Es un nif
			try{
				existsIdCard = udeService.isNifAlreadyInUse(idCard, userId);
			}catch(DataAccessException dae) {
				logger.error("existsIdCard. Error: " + dae.getMostSpecificCause().getMessage());
			}
			break;
		case 2: // Es un nie
			try{
				existsIdCard = udeService.isNieAlreadyInUse(idCard, userId);
			}catch(DataAccessException dae) {
				logger.error("existsIdCard. Error: " + dae.getMostSpecificCause().getMessage());
			}
			break;
		case 3: // Es un pasaporte
			try{
				existsIdCard = udeService.isPassportAlreadyInUse(idCard, userId);
			}catch(DataAccessException dae) {
				logger.error("existsIdCard. Error: " + dae.getMostSpecificCause().getMessage());
			}
			break;
		}		
		
		return existsIdCard;
	}
	
	/**
	 * Método que comprueba si un determinado número de la seguridad Social (Núm. SS)
	 * ya está guardado en el sistema. Como primer parámetro recibe el Núm. SS que se 
	 * va a buscar y como segundo parámetro recibe el identificador en BBDD del usuario
	 * que realiza la petición.
	 * En el caso de que exista el Núm. SS, se compara el identificador del usuario que
	 * posee dicho número con el identificador recibido como parámetro; si ambos son
	 * iguales, se trata del propio usuario que ya posee ese Núm.SS y se devuelve false.
	 * Si los identificadores son distintos, el poseedor de ese Núm.SS y el usuario que
	 * lo quiere son distintos y se devuelve true.
	 * 
	 * @author FGS
	 * @since 14/11/2019
	 * @param ssNumber
	 * @param userId Entero con el identificador en BBDD del usuario que realiza la 
	 *    petición.
	 * @return true si el Núm.SS se encuentra en el sistema y pertenece a un usuario
	 *    distinto del que realizó la petición. Esto indica que el Núm.SS está cogido y 
	 *    se puede asignar a otro usuario. false si el Núm.SS no se encuentra registrado
	 *    en el sistema, o sí que lo está pero pertenece al usuario que realiza la 
	 *    petición.
	 */
	//@Secured({"ROLE_CREAR DATOS EXTENDIDOS", "ROLE_MODIFICAR DATOS EXTENDIDOS"})	
	@GetMapping("/usuarios/datosextendidos/ssnumber")	
	public boolean existsSSNumber(
			@RequestParam(value="ss_number", required=false) String ssNumber,
			@RequestParam(value="id", defaultValue="-1") int userId){
		
		boolean existsSSNumber = false;
		
		try{
			existsSSNumber = udeService.isSSNumberAlreadyInUse(ssNumber, userId);
		}catch(DataAccessException dae) {
			logger.error("existsSSNumber. Error: " + dae.getMostSpecificCause().getMessage());
		}
		
		return existsSSNumber;
	}

	/**
	 * Método que comprueba si un determinado IBAN ya está guardado en el sistema.
	 * Como primer parámetro recibe el IBAN que se va a buscar y como segundo 
	 * parámetro recibe el identificador en BBDD del usuario que realiza la petición.
	 * En el caso de que exista el IBAN, se compara el identificador del usuario que
	 * posee dicho IBAN con el identificador recibido como parámetro; si ambos son
	 * iguales, se trata del propio usuario que ya posee ese IBAN y se devuelve false.
	 * Si los identificadores son distintos, el poseedor de ese IBAN y el usuario que
	 * lo quiere son distintos y se devuelve true.
	 * 
	 * @author FGS
	 * @since 16/12/2019
	 * @param ibanNumber Cadena de texto con el IBAN cuya existencia en el sistema
	 *     se va comprobar.
	 * @param userId Entero con el identificador en BBDD del usuario que realiza la 
	 *    petición.
	 * @return true si el IBAN se encuentra en el sistema y pertenece a un usuario
	 *    distinto del que realizó la petición. Esto indica que el IBAN está cogido y 
	 *    se puede asignar a otro usuario. false si el IBAN no se encuentra registrado
	 *    en el sistema, o sí que lo está pero pertenece al usuario que realiza la 
	 *    petición.
	 */
	//@Secured({"ROLE_CREAR DATOS EXTENDIDOS", "ROLE_MODIFICAR DATOS EXTENDIDOS"})	
	@GetMapping("/usuarios/datosextendidos/ibannumber")	
	public boolean existsIbanNumber(
			@RequestParam(value="iban_number", required=false) String ibanNumber,
			@RequestParam(value="id", defaultValue="-1") int userId){
		
		boolean existsIbanNumber = false;
		
		try{
			
			existsIbanNumber = udeService.isIbanNumberAlreadyInUse(ibanNumber,userId);
		}catch(DataAccessException dae) {
			logger.error("existsIbanNumber. Error: " + dae.getMostSpecificCause().getMessage());
		}
		
		return existsIbanNumber;
	}
	

	@GetMapping("usuarios/payroll")
	public List<Payrollconfig> showConfig(){
		return payrollService.findAll();
	}
	
	@PutMapping("usuarios/payroll/save")
	public List<Payrollconfig> saveConfig( @RequestBody List<Payrollconfig> configurations){
		return payrollService.saveConfig(configurations);

	}
	
	@GetMapping("usuarios/specialConditions")
	public List<Specialconditions> showConditions(){
		return specialConditions.findAll();

	}
	
	/**
	 * Método que devuelve la lista con las fechas de las nóminas disponibles para un usuario.
	 * 
	 * @author FGS
	 * @since  03/01/2019
	 * @param userDataExtended usuario del cual se van a devolver sus nóminas disponibles.
	 * @return
	 */
	@GetMapping("/usuarios/payrollslist/{id}")
	public List<String> getListOfAvailablePayrolls(@PathVariable int id){
		List<String> listOfAvailablePayrolls = new ArrayList<String>();

		UserDataExtended udeInDB = new UserDataExtended();
		
		try {
			udeInDB = udeService.findById(id);
		} catch (DataAccessException dae) {
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_GET_USERDATAEXTENDED",null));
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_USERDATAEXTENDED",null));
		}		
		
		try {
			if (udeInDB != null) {
				listOfAvailablePayrolls = payrollSrv.getPayrollsDatesOfUser(udeInDB);	
			}			
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_GETTING_USER_PAYROLLS_LIST",null));
		}
		
		return listOfAvailablePayrolls;
	}

	/**
	 * 
	 * @author FGS
	 * @since  08/01/2020
	 * 
	 */
	@GetMapping("/usuarios/datosextendidos/haspayroll")
	public boolean hasPayrollToShow(@RequestParam(value="id", defaultValue="-1") int userId) {
		boolean hasPayrollToShow = false;
		UserDataExtended udeInDB = new UserDataExtended();
		
		try {
			udeInDB = udeService.findById(userId);
		} catch (DataAccessException dae) {
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_GET_USERDATAEXTENDED",null));
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_USERDATAEXTENDED",null));
		}		
		
		try {
			if (udeInDB != null) {			
				hasPayrollToShow = payrollSrv.hasPayrollToShow(udeInDB);		
			} 
		} catch (Exception e) {
			e.printStackTrace();						
		}
		return hasPayrollToShow;		
	}
	
	private void fixDates(UserDataExtended userDataExtended) {
		//Probando problemas con fechas
		Calendar calendarAux = Calendar.getInstance();
		if (userDataExtended.getHiredDate()!=null) {
			calendarAux.setTime(userDataExtended.getHiredDate());
			calendarAux.add(Calendar.DATE,1);
			userDataExtended.setHiredDate(calendarAux.getTime());	
		}
		if (userDataExtended.getFiredDate()!=null) {
			calendarAux.setTime(userDataExtended.getFiredDate());
			calendarAux.add(Calendar.DATE,1);
			userDataExtended.setFiredDate(calendarAux.getTime());
		}
	}

}

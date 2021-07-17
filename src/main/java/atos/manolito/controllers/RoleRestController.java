package atos.manolito.controllers;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
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
import atos.manolito.entity.Permission;
import atos.manolito.entity.Role;
import atos.manolito.entity.UserData;
import atos.manolito.exceptions.GeneralException;
import atos.manolito.exceptions.MyDataAccessException;
import atos.manolito.services.IPermissionService;
import atos.manolito.services.IRoleService;
import atos.manolito.services.IUserDataService;

@RestController
@RequestMapping("manolito")
public class RoleRestController {

	String mensaje; // para contener el mensaje de error.
	String error;   // para contener la traza del error
    
    @Autowired
    Messages messages;
    
	@Autowired
	private IRoleService roleService;
	
	@Autowired
	private IUserDataService usuarioService;
	
	// Operación READ sobre ROL
	@Secured({"ROLE_VER ROLES"})
	@GetMapping("/roles/listar")
	public List<Role> showRoles(){
		 return roleService.findAll();
	}
	
	// dt003-DCS-12/11/2019: MEtodo para paginacion de Usuarios
	@Secured({"ROLE_VER USUARIOS"})
	@GetMapping("/roles/listar/pagina/{page}")
	public Page<Role> page(@PathVariable int page,
							  @RequestParam(value="size", defaultValue = "5") int size,
							  @RequestParam(value="ordenationBy", defaultValue = "nombre") String ordenationBy,
							  @RequestParam(defaultValue = "true") boolean order,
							  @RequestParam() String dasId,
							  @RequestParam() String nameUser,
							  @RequestParam() String surname1){
		Page<Role> pages = null;
		try {
			pages = roleService.search(page,size,ordenationBy, order, dasId, nameUser, surname1);
			// U2009-JMM-28/11/19
		} catch (JDBCConnectionException jdbce) {
			throw new GeneralException(messages.get("ERROR_DATABASE_CONNECT",null));
		} catch (NullPointerException npe) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_USERDATA",null));
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_USERDATA",null));
		}
		System.out.println(pages.toString());
		return pages;
	}
	
	/**
	 *  Este método implementa la operación CREATE sobre un rol. Recibe un objeto de tipo
	 *  Rol	e intenta guardarlo en la BD. Tanto si tiene éxito como si no, devuelve una 
	 *  respuesta HTTP con el resultado de la operación.
	 *  
	 * @param rol Objeto de tipo Rol que contiene los datos asociados al rol a crear.
	 * @param resultado Objeto que almacena los posibles errores de validación en el objeto
	 *        Rol.
	 * @return Un objeto de tipo ResponseEntity con el resultado de la operación de guardado
	 *       del rol en la BD.
	 */
	@Secured({"ROLE_CREAR ROLES"})
	@PostMapping(value="/roles/crear")
	public ResponseEntity<?> create(@Valid @RequestBody Role role, BindingResult result){
		Map<String, Object> response = new HashMap<>();
			
		if(result.hasErrors()){
			List<String> errors = result.getFieldErrors().stream().map(err -> "El campo: '"+ err.getField() + "' "+err.getDefaultMessage()).collect(Collectors.toList());
			response.put("error",errors);
			response.put(Constants.MESSAGE, messages.get("ERROR_VALIDATION_CREATE_ROLE",new Object[] {role.getName()}));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		try {
			// FGS 28/11/19. Probando forma de actualizar los usuarios del rol de forma correcta.			
			// Creo la relación nueva entre los usuarios y el rol
			role.getUsersData().forEach(usr -> usuarioService.findById(usr.getId()).addRole(role));
			
			roleService.save(role);
			// FGS 04/11/19. Añado el propio rol a la lista de roles de sus usuarios.
			//role.getUsersData().forEach(usr -> usr.addRole(role));
			
			// FGS 04/11/19. Persisto los usuarios para crear la relación con el rol.
			//role.getUsersData().forEach(usr -> usuarioService.save(usr));
			
		} catch (DataAccessException dae) {
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_CREATE_ROLE",new Object[] {role.getName()}));
		} catch (Exception ex) {
			throw new GeneralException(messages.get("ERROR_DATABASE_CREATE_ROLE",new Object[] {role.getName()}));
		}
		response.put(Constants.MESSAGE,messages.get("SUCCESS_CREATE_ROLE",null));
		response.put("rol", role);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}
	
	/**
	 * Este método implementa la operación UPDATE sobre un rol. Recibe un objeto de tipo
	 * Rol, su id	e intenta guardarlo en la BD. Tanto si tiene éxito como sino, devuelve  
	 * una respuesta HTTP con el resultado de la operación.
	 * 
	 * 
	 * @param rol Objeto de tipo Rol que contiene los datos asociados al rol a modificar.
	 * @param id Campo id del objeto Rol que se va a modificar.
	 * @param resultado Objeto que almacena los posibles errores de validación en el objeto
	 *        Rol.
	 * @return Un objeto de tipo ResponseEntity con el resultado de la operación de guardado
	 *       del rol en la BD.  	
	 */
	@Secured({"ROLE_MODIFICAR ROLES"})
	@PutMapping("/roles/modificar/{id}")
	public ResponseEntity<?> update(@RequestBody Role role, @PathVariable int id) {
		Role currentRole;
		Map<String, Object> response = new HashMap<>();
		boolean isRoleNameTaken = false;
		
		// Control de errores en el acceso a la BD al buscar el rol a editar
		try {
			currentRole = roleService.findById(id);
		} catch (DataAccessException dae) {
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_GET_ROLE",new Object[] {id}));
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_ROLE",new Object[] {id}));
		}

		if (currentRole == null) {
			response.put(Constants.MESSAGE, messages.get("NOT_FOUND_EDIT_ROLE",new Object[] {id}));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		
		// FGS 23/12/19 Realización de validación dentro del método para evitar @Valid
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<Role>> result = validator.validate(role);
				
		isRoleNameTaken = !currentRole.getName().equals(role.getName()) && existsRoleName(role.getName());
		if (result.size()>0 || isRoleNameTaken) {
			List<String> errors = new ArrayList<String>();
			for (ConstraintViolation<Role> cv : result) {
			    errors.add(cv.getMessage());
			}
			if (isRoleNameTaken) {
				errors.add("El nombre " + role.getName() + " está siendo usado");
			}
			response.put(Constants.ERROR, errors);
			response.put(Constants.MESSAGE, messages.get("ERROR_VALIDATION_UPDATE_ROLE",null));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
		
		
		// FGS 29/11/19 Comprobación de  si un usuario se ha quedado sin rol.
		List<String> errors = new ArrayList<String>();
		Set<UserData> listUsrsToCheck = new HashSet<UserData>();
		currentRole.getUsersData().forEach(usr -> {
			if (usr.getRoles().size() == 1) { // Puede que el usuario se quede sin rol
				listUsrsToCheck.add(usr);				
			}
		});
		
		// Si el usuario no está en la nueva lista => se quedaría sin rol.
		listUsrsToCheck.forEach(usr -> {
			if (!role.getUsersData().contains(usr)) 
				errors.add("El usuario " + usr.getDasId() + " no puede quedarse sin roles");				
		});
		// FGS 09/12/19 METER ESTO EN EL FICHERO DE MENSAJES DE ERROR		 
		if (errors.size() > 0) { 
			response.put(Constants.ERROR, errors);
			response.put(Constants.MESSAGE, messages.get("ERROR_VALIDATION_UPDATE_ROLE",null));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
		}
				
		// FGS 28/11/19. Probando forma de actualizar los usuarios del rol de forma correcta.
		// Elimino la relación anterior entre el rol y los usuarios.
		currentRole.getUsersData().forEach(usr -> usr.removeRole(currentRole));
						
		// Creo la relación nueva entre los usuarios y el rol
		role.getUsersData().forEach(usr -> usuarioService.findById(usr.getId()).getRoles().add(role));
						
		// FGS 29/11/19 La prueba finaliza aquí.
		currentRole.setName(role.getName());
		currentRole.setPermissions(role.getPermissions());
		
		try {			
			roleService.save(currentRole);
		} catch (DataAccessException dae) {
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_UPDATE_ROLE",new Object[] {role.getName()}));
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_DATABASE_UPDATE_ROLE",new Object[] {role.getName()}));
		}
		
		
		response.put(Constants.MESSAGE,messages.get("SUCCESS_UPDATE_ROLE",null));

		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	/**
	 * Este método implementa la operación DELETE sobre un rol. Recibe el identificador
	 * del rol e intenta borrarlo de la BD. Tanto si tiene éxito como sino, devuelve  
	 * una respuesta HTTP con el resultado de la operación.
	 * Si el rol tiene al menos un usuario que tiene dicho rol como único rol, entonces
	 * no se permite la operación de borrado, ya que el usuario quedaría huérfano de
	 * rol y eso no es posible. 
	 * 
	 * @param id Identificador numérico del rol a borrar.
	 * @return Un objeto de tipo ResponseEntity con el resultado de la operación de 
	 *      borrado del rol en la BD.  
	 */
	@Secured({"ROLE_ELIMINAR ROLES"})
	@DeleteMapping("/roles/listar/{id}")
	public ResponseEntity<?> delete(@PathVariable int id) {
		Map<String, Object> response = new HashMap<>();
		Role currentRole = null;
		
		try {
			currentRole = roleService.findById(id);
		} catch (DataAccessException dae) {
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_GET_ROLE",new Object[] {id}));
		}
		
		if (currentRole == null) {
			response.put(Constants.MESSAGE, messages.get("NOT_FOUND_EDIT_ROLE",new Object[] {id}));
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		// Hemos recuperado en rol de la BD. Ahora comprobamos que se pueda borrar.
		// Si hay un usuario que tenga ese rol como único rol -> Prohibido borrar.
		for (UserData usr : currentRole.getUsersData()) {
			 if (usr.getRoles().size() == 1) {
				 response.put(Constants.MESSAGE,messages.get("UNSUCCESS_DELETE_ROLE_USERS_DEPENDS",new Object[] {currentRole.getName()}));
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.FORBIDDEN);
			 }
		}
		 
		try {
			roleService.delete(id);
		} catch (DataAccessException dae) {
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_DELETE_ROLE",new Object[] {id}));
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_DATABASE_DELETE_ROLE",new Object[] {id}));
		}
		response.put(Constants.MESSAGE, messages.get("SUCCESS_DELETE_ROLE",new Object[] {currentRole.getName()}));
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}
	
	/**
	 * Este método sirve para ver los detalles de un rol cuyo identificador
	 * se pasa como parámetro.
	 * 
	 * @param id Variable numérica conteniendo el identificador del rol a buscar.
	 * @return Objeto de tipo ResponseEntity con los datos del rol solicitado.
	 */
	@Secured({"ROLE_VER ROLES"})
	@GetMapping("/roles/{id}")
	public ResponseEntity<?> showRole(@PathVariable int id){
		Role role = null;
		Map<String , Object> response = new HashMap<>();
		try{
			role = roleService.findByIdNotRec(id);
		}catch(DataAccessException dae) {
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_GET_ROLE",new Object[] {id}));
		}
		
		if(role == null) {
			response.put(Constants.MESSAGE,messages.get("NOT_EXIST_ROLE",new Object[] {id}));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Role>(role, HttpStatus.OK);
	}
	
	/**
	 * Este método permite ver los usuarios asociados al rol cuyo id se pasa como parámetro.
	 * 
	 * @param id Identificador numérico del rol.
	 * @return Un objeto de tipo ResponseEntity con los usuarios asociados al rol que se pasa
	 *       como parámetro.
	 */
	@Secured({"ROLE_VER ROLES"})
	@GetMapping("/roles/ver/{id}")
	public ResponseEntity<?> showUsersRole(@PathVariable int id){
	
		Role role = null;
		Set<UserData> usersData = null;
		
		Map<String , Object> response = new HashMap<>();
		try{

			role = roleService.findById(id);
		}catch(DataAccessException dae) {
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_GET_ROLE",new Object[] {id}));
		}
		
		if(role == null) {
			response.put(Constants.MESSAGE, messages.get("NOT_EXIST_ROLE",new Object[] {id}));
			return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
		}
		usersData = role.getUsersData();
		
		return new ResponseEntity<Set<UserData>>(usersData, HttpStatus.OK);
	}
	
	
	@Autowired
	IPermissionService permisoService;
	
	/**
	 * Este método devuelve una lista con todos los permisos almacenados en la BD.
	 * Actualmente sólo se muestran, porque la autorización se realiza a través de los
	 * roles. En el futuro, servirán para controlar las páginas a las que pueden
	 * acceder los usuarios.
	 * 
	 * @return Lista con todos los permisos existentes en la base de datos.
	 */
	@Secured({"ROLE_VER ROLES"})
	@GetMapping("/roles/permisos/listar")
	public List<Permission> showAllPermissions(){		
		return permisoService.findAll();
	}
	
	/**
	 * Este método sirve para ver los detalles de un permiso cuyo nombre se pasa como parámetro.
	 * 
	 * @param nombre Cadena de texto con el nombre del permiso a mostrar.
	 * @return Objeto de tipo ResponseEntity con los datos del permiso solicitado.
	 */
	@Secured({"ROLE_VER ROLES"})
	@GetMapping("/permisos/listar/{name}")
	public ResponseEntity<?> showPermission(@PathVariable String name){		
			
			Permission permission = null;
			Map<String , Object> response = new HashMap<>();
			
			try{
				permission = permisoService.findByName(name);
			} catch (DataAccessException dae) {
				throw new MyDataAccessException(messages.get("ERROR_DATABASE_GET_PERMISSION",new Object[] {name}));
			} catch (Exception e) {
				throw new GeneralException(messages.get("ERROR_DATABASE_GET_PERMISSION",new Object[] {name}));
			}
			
			if(permission == null) {
				response.put(Constants.MESSAGE,messages.get("NOT_EXIST_PERMISSION",new Object[] {name}));
				return new ResponseEntity<Map<String,Object>>(response, HttpStatus.NOT_FOUND);
			}
			
			return new ResponseEntity<Permission>(permission, HttpStatus.OK);
		
	}
	/**
	 * 
	 * @author FGS
	 * @since  16/12/2019
	 * @param dasId
	 * @return
	 */
	//@GetMapping("/roles/name/{roleName}")	
	public boolean existsRoleName(@PathVariable String roleName){
		
		boolean existsRoleName = false;
		try{
			System.out.println("Voy a comprobar si existe : " + roleName);
			existsRoleName = roleService.isRoleNameAlreadyInUse(roleName);
		}catch(DataAccessException dae) {			
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_CHECK_ROLENAME",new Object[] {roleName}));
		}
		return existsRoleName;
	}
	
	@GetMapping("/roles/name/{roleName}")	
	public boolean existsRoleNameV2(@PathVariable String roleName ,
			@RequestParam(value="id", defaultValue="-1") int id){
		
		boolean existsRoleName = false;
		try{
			System.out.println("Voy a comprobar si existe : " + roleName);
			existsRoleName = roleService.isRoleNameAlreadyInUse(roleName,id);
			
			
		}catch(DataAccessException dae) {			
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_CHECK_ROLENAME",new Object[] {roleName}));
		}
		return existsRoleName;
	}
}


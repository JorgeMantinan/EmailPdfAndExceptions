package atos.manolito.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import atos.manolito.Constants;
import atos.manolito.Messages;
import atos.manolito.dao.IUserDataDao;
import atos.manolito.entity.Address;
import atos.manolito.entity.Permission;
import atos.manolito.entity.Role;
import atos.manolito.entity.UserData;

@Service
public class UserDataServiceImpl implements IUserDataService, UserDetailsService {

	// JMM ../10/19.

	private Logger logger = LoggerFactory.getLogger(UserDataServiceImpl.class);

	@Autowired
	Messages messages;
	
	@Autowired
	private IUserDataDao userDataDao;

	@Override
	public List<UserData> findAll() {
		List<UserData> usersData  = (List<UserData>) userDataDao.findAll();
		// Cortamos la recursividad con roles no devolviendo sus usuarios.
		for (UserData usr : usersData) {
			usr.getRoles().forEach(r ->	r.setUsersData(new HashSet<UserData>()));
			// FGS 18/11/19 Corto la recusividad a través de datos extendidos.
			if (usr.getUserDataExtended()!=null) {
				usr.getUserDataExtended().setUserData(null);
				for(Address address: usr.getUserDataExtended().getAddresses()) {
					address.getUserdataextended().setAddresses(null);
				}
			}
		}
		return usersData;
	}


	// dt003-DCS-12/11/2019: MEtodo para paginacion de Usuarios
		@Override
		@Transactional(readOnly = true)
		public Page<UserData> search(int page, int size, String ordenationBy, boolean order, String dasId, String name, String surname1, String surname2, String mail, int state) {
			PageRequest pageable = null;
			Page<UserData> pageUser= null;
			if(order) {
				pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,ordenationBy));
			} else {
				pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC,ordenationBy));
			}
			if(state ==  0) {
				pageUser = userDataDao.search(pageable,dasId,name,surname1,surname2,mail);
			}else {
				pageUser = userDataDao.search(pageable,dasId,name,surname1,surname2,mail,state);
			}
			return pageUser ;
		}

	@Override
	@Transactional(readOnly = true)
	public UserData findById(int id) {
		UserData userData = userDataDao.findById(id).orElse(null);

		
		//FGS 18/11/19 Corto la recursividad a través de datos extendidos.
		if (userData.getUserDataExtended()!=null) {
			// Cortamos la recursividad con roles.
			userData.getRoles().forEach(r -> r.setUsersData(new HashSet<UserData>()));
			userData.getUserDataExtended().setUserData(null);
		}

		return userData;
	}

	@Override
	@Transactional(readOnly = true)
	public UserData findByDasId(String dasId) {
		UserData userData = userDataDao.findByDasId(dasId);

		// Cortamos la recursividad con roles.
		if (userData != null) { // 191023 FGS RETOCAR ESTO USANDO LAMBDA EXPRESSIONS
			userData.getRoles().forEach(r -> r.setUsersData(new HashSet<UserData>()));
			//FGS 18/11/19 Corto la recursividad a través de datos extendidos.
			if (userData.getUserDataExtended()!=null)
				userData.getUserDataExtended().setUserData(null);
		}
		

		return userData;
	}

	/**
	 * @author LDC
	 * Este método devuelve una lista con aquellos usuarios que se ajusten a los
	 * criterios especificados en los campos que se pasan como parámetros. El
	 * filtrado se aplica sobre el DAS ID, el nombre y el primer apellido, siempre y
	 * cuando tengan algún valor.
	 */
//	@Override
//	@Transactional(readOnly = true)
//	public List<UserData> filter(String dasId, String name, String surname1) {
//
//		List<UserData> usersData = userDataDao.filter(dasId.equals("null") ? "" : dasId,
//				(name.equals("null") ? "" : name), (surname1.equals("null") ? "" : surname1));
//		// Cortamos la recursividad con roles no devolviendo sus usuarios.
//		for (UserData usr : usersData) {
//			usr.getRoles().forEach(r -> r.setUsersData(new HashSet<UserData>()));
//		}
//		return usersData;
//	}

	@Override
	@Transactional
	public UserData save(UserData userData) {
		return userDataDao.save(userData);
	}


	@Override
	@Transactional
	public void delete(int id) {
		userDataDao.deleteById(id);
	}

	/**
	 * Comprueba si un determinado DAS ID ya está siendo usuado por algún usuario.
	 * @author FGS
	 * @since  06/11/2019
	 * @param dasId
	 * @return
	 */
	@Override
	@Transactional
	public boolean isDasAlreadyInUse(String dasId){

		boolean dasInDb = true;

		if (userDataDao.findByDasId(dasId) == null) dasInDb = false;

		return dasInDb;
	}
	
	/**
	 * 
	 * @author FGS
	 * @since  15/11/2019
	 */
	@Override
	@Transactional(readOnly = true)
	public List<UserData> findAllWithoutDataExtended(){
		List<UserData> listUserData = null;
		listUserData = userDataDao.findAllWithoutDataExtended();
		// Cortamos la recursividad con roles no devolviendo sus usuarios.
		for (UserData usr : listUserData) {
			System.out.println("USUARIO RECUPERADO: " + usr.getDasId());
			usr.getRoles().forEach(r -> {
				r.setUsersData(new HashSet<UserData>());
				r.setPermissions(new HashSet<Permission>());
			});
			/*if (usr.getUserDataExtended() != null) {
				System.out.println(usr.getDasId() + " tiene datos extendidos");
				usr.getUserDataExtended().setUserData(null);
			}*/
		}
 		return listUserData;
	}
	
	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String dasId) throws UsernameNotFoundException {
		
		UserData userData = userDataDao.findByDasId(dasId);
		
		if (userData == null) {
			logger.error(messages.get("NOT_EXIST_USERDATA_WITH_DASID", new Object[] {dasId}));
			throw new UsernameNotFoundException(messages.get("NOT_EXIST_USERDATA_WITH_DASID", new Object[] {dasId}));
		}

		Set<String> listaPermisos = new HashSet<>();

		for (Role role : userData.getRoles()) {
			for (Permission permission : role.getPermissions()) {
				listaPermisos.add(permission.getName());
			}
		}
		
		List<GrantedAuthority> authorities = listaPermisos
				.stream()
				.map(permission -> new SimpleGrantedAuthority(permission))
				.peek(authority -> logger.info("Role: " + authority.getAuthority()))
				.collect(Collectors.toList());

		if (userData.getState() == Constants.PEN_ACTIVATION) {
			return new User(dasId, userData.getLogin().getPassword(), true, true, true, true, authorities);
		} else if (userData.getState() == Constants.ACTIVE) {
			return new User(dasId, userData.getLogin().getPassword(), true, true, true, true, authorities);
		} else if (userData.getState() == Constants.LOCKED) {
			return new User(dasId, userData.getLogin().getPassword(), false, true, true, false, authorities);
		}
		return null;
	}

	@Override
	public List<UserData> filter(String dasId, String name, String surname1) {
		// TODO Auto-generated method stub
		return null;
	}

}

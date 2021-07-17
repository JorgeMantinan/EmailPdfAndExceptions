package atos.manolito.services;

import java.util.HashSet;
import java.util.List;

import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import atos.manolito.Messages;
import atos.manolito.dao.IRoleDao;
import atos.manolito.entity.Role;
import atos.manolito.entity.UserData;
import atos.manolito.exceptions.GeneralException;

@Service
public class RoleServiceImpl implements IRoleService {
	
	// FGS ../10/19.

	@Autowired
	Messages messages;
	
	@Autowired
	IRoleDao roleDao;
	
	@Override
	public List<Role> findAll() {
		List<Role> roles;
		try {
		roles = (List<Role>) roleDao.findAll();
		} catch (JDBCConnectionException jdbce) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_ROLES",null));
		} catch (NullPointerException npe) {
			throw new GeneralException(messages.get("ERROR_DATABASE_NULL_ROLE",null));
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_ROLES",null));
		}
		// Para cortar la recursividad anulamos la lista de usuarios de los roles.
		for (Role role: roles) {
			for (UserData usu: role.getUsersData()) {
				usu.setRoles(new HashSet<Role>());
				// FGS 18/11/19 Corto la recursividad a través de datos extendidos
				if (usu.getUserDataExtended()!=null)
					usu.setUserDataExtended(null);
			}
		}
		return roles;
	}

	@Override
	@Transactional(readOnly = true)
	public Role findById(int id) {
		return roleDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Role findByIdNotRec(int id) {
		Role role = roleDao.findById(id).orElse(null);
		// Para evitar la recursividad.
		if (role.getUsersData() != null) {
			
			for (UserData usu: role.getUsersData()) {
				usu.setRoles(new HashSet<Role>());
				// FGS 18/11/19 Corto la recursividad a través de datos extendidos
				if (usu.getUserDataExtended()!=null)
					usu.setUserDataExtended(null);
			}
		}
		return role;
	}
	
	@Override
	@Transactional(readOnly = true)
	public Role findByName(String name) {
		return roleDao.findByName(name);
	}

	@Override
	@Transactional
	public Role save(Role role) {
		return roleDao.save(role);
	}

	@Override
	@Transactional
	public void delete(int id) {
		roleDao.deleteById(id);
	}

	@Override
	public Page<Role> findAll(Pageable pageable) {
		Page<Role> page;
		try {
			page = (Page<Role>) roleDao.findAll(pageable);
		} catch (JDBCConnectionException jdbce) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_ROLES",null));
		} catch (NullPointerException npe) {
			throw new GeneralException(messages.get("ERROR_DATABASE_NULL_ROLE",null));
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_ROLES",null));
		}
		for (Role role : page) {
			role.getUsersData().forEach(u -> {
				u.setRoles(new HashSet<Role>());
				// FGS 18/11/19 Corto la recursividad a través de datos extendidos
				if (u.getUserDataExtended() != null)
					u.setUserDataExtended(null);
			});

		}
		return page;
	}

	@Override
	public Page<Role> search(int page, int size, String ordenationBy, boolean order, String dasId, String name,
			String surname1) {
		System.out.println("Tamaño: " + size);
		PageRequest pageable = null;
		if(order) {
			pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC,ordenationBy));
		} else {
			pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC,ordenationBy));
		}
		return roleDao.search(pageable,dasId,name,surname1) ;
	}
	/**
	 * 
	 * @author FGS
	 * @since  16/12/2019
	 * @param 
	 * @return
	 */
	@Override
	@Transactional
	public boolean isRoleNameAlreadyInUse(String roleName){

		boolean roleNameInDb = true;

		if (roleDao.findByName(roleName) == null) roleNameInDb = false;

		return roleNameInDb;
	}
	
	/**
	 * 
	 * @author FGS
	 * @since  26/12/2019
	 * @param 
	 * @return
	 */
	@Override
	@Transactional
	public boolean isRoleNameAlreadyInUse(String roleName, int roleId){

		boolean roleNameInDB = false;
		Role roleInDB = null;
		
		roleInDB = roleDao.findByName(roleName);
		
		if (roleInDB != null) {
			System.out.println("roleInDB: " + roleInDB + " roleId = " + roleId);
			if (roleInDB.getId() != roleId) { // Otro rol está intentando tomar el nombre de otro
				System.out.println("Los ids no coinciden");
				roleNameInDB = true;
			} 
		}

		return roleNameInDB;
	}

	
}


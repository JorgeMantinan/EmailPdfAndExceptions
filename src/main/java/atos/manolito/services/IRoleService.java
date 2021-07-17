package atos.manolito.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import atos.manolito.entity.Role;

public interface IRoleService {
	
	public List<Role> findAll();
	
	public Page<Role> findAll(Pageable Pageable);
	
	public Role findById(int id);
	
	public Role findByIdNotRec(int id);
	
	public Role findByName(String name);
	
	public Role save(Role role);
	
	public void delete(int id);

	public Page<Role> search(int page, int size, String ordenationBy, boolean order, String dasId, String name,
			String surname1);

	boolean isRoleNameAlreadyInUse(String roleName);

	boolean isRoleNameAlreadyInUse(String roleName, int roleEd);
}

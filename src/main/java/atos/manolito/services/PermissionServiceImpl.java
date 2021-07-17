package atos.manolito.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import atos.manolito.dao.IPermissionDao;
import atos.manolito.entity.Permission;

@Service
public class PermissionServiceImpl implements IPermissionService {
	
	// FGS ../10/19.

	@Autowired
	IPermissionDao permissionDao;
	
	@Override
	public List<Permission> findAll() {
		return (List<Permission>) permissionDao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Permission findById(int id) {
		return permissionDao.findById(id).orElse(null);
	}

	@Override
	@Transactional(readOnly = true)
	public Permission findByName(String name) {
		return permissionDao.findByName(name);
	}

}


package atos.manolito.services;

import java.util.List;

import atos.manolito.entity.Permission;

public interface IPermissionService {
	
	public List<Permission> findAll();
	
	public Permission findById(int id);
	
	public Permission findByName(String name);
}
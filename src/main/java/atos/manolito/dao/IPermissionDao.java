package atos.manolito.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import atos.manolito.entity.Permission;

public interface IPermissionDao extends JpaRepository<Permission, Integer>{
	
	@Query("select p from Permission p where p.name = :name_permission") 
	public Permission findByName(@Param("name_permission") String name); 

}

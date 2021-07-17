package atos.manolito.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import atos.manolito.entity.Role;

public interface IRoleDao extends JpaRepository<Role, Integer>{
	
	@Query("select r from Role r where r.name = :name") // 19/10/07 introducido para buscar por nombre del rol.
	public Role findByName(@Param("name") String name);

	// where u.dasId like CONCAT('%',:DAS,'%') and u.name like CONCAT('%',:NAME,'%') and u.surname1 like CONCAT('%',:SURMANE1,'%') 
	@Query("select r from Role r JOIN r.usersData u where u.dasId like CONCAT('%',:DAS,'%') and u.name like CONCAT('%',:NAME,'%') and u.surname1 like CONCAT('%',:SURMANE1,'%') group by r.name")
	public Page<Role> search(Pageable pageable,@Param("DAS") String dasId,@Param("NAME") String name,@Param("SURMANE1") String surname1);
}

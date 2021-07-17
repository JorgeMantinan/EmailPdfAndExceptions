package atos.manolito.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import atos.manolito.entity.Login;

public interface ILoginDao extends JpaRepository<Login, Integer>{
	
//	@Query("select l from Login l where l.dasId = :DAS_ID") // 19/10/04 introducido para buscar por dasId
//	public Login findByDasId(@Param("DAS_ID") String dasId); 

}

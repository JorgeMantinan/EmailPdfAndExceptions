package atos.manolito.dao;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import atos.manolito.entity.Community;
import atos.manolito.entity.Province;

//t2007-DCS-21/11/2019
public interface IProvinceDao extends JpaRepository<Province, Integer> {
	
	@Query("from Province p where p.community = :COMMUNITY")
	public List<Province> findByCommunity( @Param("COMMUNITY") Community community);

}

package atos.manolito.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import atos.manolito.entity.PostalCode;
import atos.manolito.entity.Province;

//t2007-DCS-19/11/2019
public interface IPostalCodeDao  extends JpaRepository<PostalCode, Integer>{

	@Query("from PostalCode pc where pc.codTownship.cod = :TOWNSHIP and pc.codTownship.province = :PROVINCE")
	public List<PostalCode> findPostalCode(@Param("TOWNSHIP")int id_township,@Param("PROVINCE") Province province);
}

package atos.manolito.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import atos.manolito.entity.Province;
import atos.manolito.entity.TownShip;
import atos.manolito.entity.TownShipId;

//t2007-DCS-19/11/2019
public interface ITownShipDao extends JpaRepository<TownShip, TownShipId> {
	
	@Query("from TownShip t where t.cod = :TOWNSHIP and t.province = :PROVINCE")
	public TownShip findByIds(@Param ("TOWNSHIP")int id_townShip,@Param("PROVINCE")Province province);
	
	@Query("from TownShip t where t.province = :PROVINCE")
	public List<TownShip> findAllByIdProvince(@Param("PROVINCE") Province id_province);

}

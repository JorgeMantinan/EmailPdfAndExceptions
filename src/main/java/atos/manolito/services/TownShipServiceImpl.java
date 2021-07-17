package atos.manolito.services;

import java.util.List;

import org.springframework.stereotype.Service;

import atos.manolito.dao.ITownShipDao;
import atos.manolito.entity.Province;
import atos.manolito.entity.TownShip;

//t2007-DCS-21/11/2019
@Service
public class TownShipServiceImpl implements ITownShipService{

	ITownShipDao townShipDao; 
	
	@Override
	public List<TownShip> findAll() {
		return townShipDao.findAll();
	}

	@Override
	public TownShip findByIds(int id_townShip, Province province) {
		return townShipDao.findByIds(id_townShip, province);
	}

	@Override
	public List<TownShip> findAllById(Province id) {
		return townShipDao.findAllByIdProvince(id);
	}

}

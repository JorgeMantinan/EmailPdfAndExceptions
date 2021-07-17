package atos.manolito.services;

import java.util.List;

import org.springframework.stereotype.Service;

import atos.manolito.entity.Province;
import atos.manolito.entity.TownShip;

@Service
public interface ITownShipService {
	
	public List<TownShip> findAll();
	
	public TownShip findByIds(int id_townShip, Province province);
	
	public List<TownShip> findAllById(Province id);

}

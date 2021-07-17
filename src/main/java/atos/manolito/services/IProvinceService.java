package atos.manolito.services;

import java.util.List;

import org.springframework.stereotype.Service;

import atos.manolito.entity.Community;
import atos.manolito.entity.Province;

//t2007-DCS-21/11/2019
@Service
public interface IProvinceService {
	
	public List<Province> findAll();
	
	public Province findById(int id);
	
	public List<Province> findAllById(Community id);
}

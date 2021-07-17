package atos.manolito.services;

import java.util.List;

import org.springframework.stereotype.Service;

import atos.manolito.dao.IProvinceDao;
import atos.manolito.entity.Community;
import atos.manolito.entity.Province;

//t2007-DCS-21/11/2019
@Service
public class ProvinceServiceImpl implements IProvinceService {

	
	IProvinceDao provinceDao;
	
	@Override
	public List<Province> findAll(){
		return provinceDao.findAll();
	}

	@Override
	public Province findById(int id) {
		return provinceDao.findById(id).orElse(null);
	}

	@Override
	public List<Province> findAllById(Community id_community) {
		return provinceDao.findByCommunity(id_community);
	}

}
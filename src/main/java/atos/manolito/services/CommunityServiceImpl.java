package atos.manolito.services;

import java.util.List;

import org.springframework.stereotype.Service;

import atos.manolito.dao.ICommunityDao;
import atos.manolito.entity.Community;

//t2007-DCS-21/11/2019
@Service
public class CommunityServiceImpl implements ICommunityService{

	ICommunityDao communityDao;
	
	@Override
	public List<Community> findAll() {
		return communityDao.findAll();
	}

	@Override
	public Community findById(int id) {
		return communityDao.findById(id).orElse(null);
	}

}

package atos.manolito.services;

import java.util.List;

import org.springframework.stereotype.Service;

import atos.manolito.entity.Community;

//t2007-DCS-21/11/2019
@Service
public interface ICommunityService {
	
	public List<Community> findAll();
	
	public Community findById(int id);
}

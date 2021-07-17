package atos.manolito.services;

import java.util.List;

import org.springframework.stereotype.Service;

import atos.manolito.entity.PostalCode;
import atos.manolito.entity.Province;

//t2007-DCS-21/11/2019
@Service
public interface IPostalCodeService{
	
	public List<PostalCode> findAll();
	
	public List<PostalCode> findPostalCode(int id_township, Province province);

}

package atos.manolito.services;

import java.util.List;

import org.springframework.stereotype.Service;

import atos.manolito.dao.IPostalCodeDao;
import atos.manolito.entity.PostalCode;
import atos.manolito.entity.Province;

//t2007-DCS-21/11/2019
@Service
public class PostalCodeServiceImpl implements IPostalCodeService {

	private IPostalCodeDao postalCodeDao;
	
	@Override
	public List<PostalCode> findAll() {
		return postalCodeDao.findAll();
	}

	@Override
	public List<PostalCode> findPostalCode(int id_township, Province province) {
		return postalCodeDao.findPostalCode(id_township, province);
	}

}

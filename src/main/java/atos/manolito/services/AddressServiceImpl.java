package atos.manolito.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import atos.manolito.dao.IAddressDao;
import atos.manolito.dao.ICommunityDao;
import atos.manolito.dao.IPostalCodeDao;
import atos.manolito.dao.IProvinceDao;
import atos.manolito.dao.ITownShipDao;
import atos.manolito.entity.Address;
import atos.manolito.entity.Community;
import atos.manolito.entity.PostalCode;
import atos.manolito.entity.Province;
import atos.manolito.entity.TownShip;


//t2007-DCS-21/11/2019
@Service
public class AddressServiceImpl implements IAddressService {
	
	@Autowired
	private IAddressDao addressDao;
	
	@Autowired
	private IPostalCodeDao postalCodeDao;
	
	@Autowired
	private ITownShipDao townshipDao;
	
	@Autowired
	private IProvinceDao provinceDao;
	
	@Autowired
	private ICommunityDao communityDao;
	
	public List<Address> findAll(){
		return addressDao.findAll();
	}
	
	public Address save(Address address) {
		return addressDao.save(address);
	}

	public void delete(int id) {
		addressDao.deleteById(id);
	}
	
	public Address findById(int id) {
		return addressDao.findById(id).orElse(null);
	}

	@Override
	public List<PostalCode> findAllPostalCode() {
		List<PostalCode> postalCode = postalCodeDao.findAll();
		postalCode.forEach(pc -> pc.setCodTownship(null));
		return postalCode;
	}

	@Override
	public List<PostalCode> findPostalCode(int id_township, int id_province) {
		List<PostalCode> postalCode = postalCodeDao.findPostalCode(id_township, provinceDao.findById(id_province).orElse(null));
//		postalCode.forEach(pc -> {pc.getCodTownship().setPostalCode(null);
//			pc.getCodTownship().getProvince().setCommunity(null);
//			pc.getCodTownship().getProvince().setTownShip(null);});
		return postalCode;
	}
	
	@Override
	public PostalCode findAllPostalCode(int cp) {
		PostalCode postalcode = postalCodeDao.findById(cp).orElse(null);
		return postalcode;
	}

	@Override
	public List<TownShip> findAllTownShip() {
		List<TownShip> township = townshipDao.findAll();
		township.forEach(t ->{t.setPostalCode(null);
			t.setProvince(null);});
		return township;
	}

	@Override
	public List<TownShip> findAllTownShip(int id_province) {
		List<TownShip> township = townshipDao.findAllByIdProvince(provinceDao.findById(id_province).orElse(null));
		township.forEach(t ->{t.setPostalCode(null);
			t.setProvince(null);});
		return township;
	}

	//da fallo
	@Override
	public TownShip findTownShip(int id_township, int id_province) {
		TownShip township = townshipDao.findByIds(id_township, provinceDao.findById(id_province).orElse(null));
		township.setPostalCode(null);
		township.setProvince(null);
		return township;
	}

	@Override
	public List<Province> findAllProvince() {
		List<Province> province = provinceDao.findAll();
		province.forEach(p -> {p.setCommunity(null);
			p.setTownShip(null);});
		return province;
	}

	@Override
	public List<Province> findAllProvince(int id_community) {
		List<Province> province = provinceDao.findByCommunity(communityDao.findById(id_community).orElse(null));
		province.forEach(p -> {p.setCommunity(null);
		   p.setTownShip(null);});
		return province;
	}

	@Override
	public Province findProvince(int id_province) {
		Province province = provinceDao.findById(id_province).orElse(null);
		province.setCommunity(null);
		province.setTownShip(null);
		return province;
	}

	@Transactional(readOnly = true)
	@Override
	public List<Community> findAllCommunity() {
		List<Community> community = communityDao.findAll();
		community.forEach(c -> c.setProvince(null));
		return community;
	}

	@Override
	public Community findCommunity(int id_community) {
		Community community = communityDao.findById(id_community).orElse(null); 
		community.setProvince(null);
		return community;
	}

	

}
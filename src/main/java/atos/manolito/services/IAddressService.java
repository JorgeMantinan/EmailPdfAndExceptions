package atos.manolito.services;

import java.util.List;

import org.springframework.stereotype.Service;

import atos.manolito.entity.Address;
import atos.manolito.entity.Community;
import atos.manolito.entity.PostalCode;
import atos.manolito.entity.Province;
import atos.manolito.entity.TownShip;

// t2007-DCS-21/11/2019
@Service
public interface IAddressService {
	
	public List<Address> findAll();
	
	public Address findById(int id);
	
	public Address save(Address address);
	
	public void delete (int id);
	
	// Metodo para devolver todos los codigos postales
	public List<PostalCode> findAllPostalCode();
	
	/* Para cuando los municipios tengan vairos codigos postales
	// Metodo para devolver los codigos postales del municipio y provincia indicados
	public List<PostalCode> findPostalCode(int id_township, int id_province);
	*/
	
	// Metodo para devolver el codigo postal del municipio y provincia indicados
	public List<PostalCode> findPostalCode(int id_township, int id_province);
	
	// Metodo para devolver todo el listado de municipios
	public List<TownShip> findAllTownShip();
	
	// Metodo para devolver todos los minucipios que se encuentren en la provincia indicada
	public List<TownShip> findAllTownShip(int id_province);
	
	// Metod para devolver el municipio que se indica por paramentro
	public TownShip findTownShip(int id_township,int id_province);
	
	// Metodo para devolver todo el listado de provincias
	public List<Province> findAllProvince();
	
	//Metod para devolver las provincias de la comunidad autonoma indicada
	public List<Province> findAllProvince(int id_community);
	
	// Metodo pada devolver la provincia indicada en el parametro
	public Province findProvince(int id_province);
	
	// Metodo para devolver todas las comunidades autonomas
	public List<Community> findAllCommunity();
	
	// Metodo para devolver la comunidad autonoma indicada
	public Community findCommunity(int id_comunity);

	// Metodo que devuleve un codigo postal
	public PostalCode findAllPostalCode(int cp);
	
	
}

package atos.manolito.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import atos.manolito.entity.Address;
import atos.manolito.entity.Community;
import atos.manolito.entity.PostalCode;
import atos.manolito.entity.Province;
import atos.manolito.entity.TownShip;
import atos.manolito.services.IAddressService;

// dtg005-DCS-26/11/2019
@CrossOrigin(origins = {"http://desktop-f1dhp23:4200/manolito" })
@RestController
@RequestMapping("manolito")
public class AddressRestController {
	
	String mensaje; // para contener el mensaje de error.
	String error;   // para contener la traza del error
    // private static final Logger logger = (Logger) LogManager.getLogger(AddressRestController.class);
    
	@Autowired
    private IAddressService addressService;
	
	@GetMapping(value="/direccion/ver/{id}")
	public Address show(@PathVariable int id){
		return addressService.findById(id);
	}
    
    @PostMapping(value="/direccion/crear")
    public ResponseEntity<?> create(@Valid @RequestBody Address address, BindingResult result){
    	Map<String, Object> response = new HashMap<>();
    	addressService.save(address);
    	return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }
    
    @PutMapping(value="/direccion/modificar")
    public ResponseEntity<?> update(@Valid @RequestBody Address address, BindingResult result){
    	Map<String, Object> response = new HashMap<>();
    	addressService.save(address);
    	return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
    
    @DeleteMapping(value="/direccion/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
    	Map<String, Object> response = new HashMap<>();
    	addressService.delete(id);
    	return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
    }
    
    @GetMapping(value="/direccion")
    public List<Address> showAll(){
    	return addressService.findAll();
    }
    
    @GetMapping(value="/direccion/cp")
    public List<PostalCode> showAllPostalCode(){
    	return addressService.findAllPostalCode();
    }
    
    @GetMapping(value="/direccion/cp/{cp}")
    public PostalCode showAllPostalCode(@PathVariable int cp){
    	return addressService.findAllPostalCode(cp);
    }
    
    @GetMapping(value="/direccion/cp/{township}/{province}")
    public List<PostalCode> showPostalCode(@PathVariable int province, @PathVariable int township){
    	return addressService.findPostalCode(township, province);
    }
    
    @GetMapping(value="/direccion/municipio")
    public List<TownShip> showAllSownShip(){
    	return addressService.findAllTownShip();
    }
    
    @GetMapping(value="/direccion/municipio/{province}")
    public List<TownShip> showAllSownShip(@PathVariable int province){
    	return addressService.findAllTownShip(province);
    }
    
    @GetMapping(value="/direccion/municipio/{township}/{province}")
    public TownShip showTownShip(@PathVariable int province, @PathVariable int township){
    	return addressService.findTownShip(township,province);
    }
    
    @GetMapping(value="/direccion/provincias")
    public List<Province> showAllProvinces(){
    	return addressService.findAllProvince();
    }
    
    @GetMapping(value="/direccion/provincias/{id_community}")
    public List<Province> showAllProvinces(@PathVariable int id_community){
    	return addressService.findAllProvince(id_community);
    }
    
    @GetMapping(value="/direccion/provincia/{id}")
    public Province showProvinces(@PathVariable int id){
    	return addressService.findProvince(id);
    }
    
    @GetMapping(value="/direccion/comunidad")
    public List<Community> showAllCommunity(){
    	return addressService.findAllCommunity();
    }
    
    @GetMapping(value="/direccion/comunidad/{id}")
    public Community showCommunity(@PathVariable int id){
    	return addressService.findCommunity(id);
    }
}

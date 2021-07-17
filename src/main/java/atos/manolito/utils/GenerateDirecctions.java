package atos.manolito.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import atos.manolito.dao.ICommunityDao;
import atos.manolito.dao.IPostalCodeDao;
import atos.manolito.dao.IProvinceDao;
import atos.manolito.dao.ITownShipDao;
import atos.manolito.entity.Community;
import atos.manolito.entity.PostalCode;
import atos.manolito.entity.Province;
import atos.manolito.entity.TownShip;

@RestController
@RequestMapping("manolito")
public class GenerateDirecctions {
	
	@Autowired
	private ICommunityDao communityDao;

	@Autowired
	private IProvinceDao provinceDao;
	
	@Autowired
	private ITownShipDao townshipDao;
	
	@Autowired
	private IPostalCodeDao postalCodeDao;
	
	public String comm;
	
	public String prov;
	
	public String town;
	
	public String pob;
	
	public String nuc;
	
	public String postal;
	
	public Community community;
	
	public Province province;
	
	public TownShip township;
	
	String endUrl = "&type=JSON&key=3732a0b22150bde3e59459a07995d034f2559bef776d5c7dac812f9fe7ff3af0&sandbox=0";
	
	RestTemplate restTemplate = new RestTemplate();
	
	@PostMapping("actualizarDirecciones")
	public void updateAddres(){
		Response community = restTemplate.getForObject("https://apiv1.geoapi.es/comunidades?" + endUrl,Response.class);
		createCommunity(community, new Community()); 
	}
	
	public void createCommunity(Response responseAutonomy, Community communityDB) {
		for (ClassResponse communityAPI : responseAutonomy.getDataJson()) {
		    communityDB.setName(communityAPI.getCom());
		    communityDB.setCod(Integer.parseInt(communityAPI.getCcom()));
		    community = communityDao.save(communityDB);
		    comm = communityAPI.getCcom();
		    	Response province = restTemplate.getForObject("https://apiv1.geoapi.es/provincias?CCOM=" + comm + endUrl, Response.class);
		    	createProvince(province,new Province());
		}
    } 
	
	public String[] comunidades = {"18","19"};
	
	@PostMapping("CeutaMelilla")
	public void ceutaMelilla() {
		for(String com : comunidades ) {
			comm = com;
			community = communityDao.findById(Integer.parseInt(comm)).orElse(null);
			Response province = restTemplate.getForObject("https://apiv1.geoapi.es/provincias?CCOM=" + comm + endUrl, Response.class);
	    	createProvince(province,new Province());
		}
	}

    public void createProvince(Response responseProvince, Province provinceDB) {
		for (ClassResponse provinceAPI : responseProvince.getDataJson()) {
			provinceDB.setName(provinceAPI.getPro());
			provinceDB.setCod(Integer.parseInt(provinceAPI.getCpro()));
			provinceDB.setCommunity(community);
		    province = provinceDao.save(provinceDB);
		    prov = provinceAPI.getCpro();
			    Response township = restTemplate.getForObject("https://apiv1.geoapi.es/municipios?CPRO=" + prov + endUrl, Response.class);
		    	createTownship(township,new TownShip());
		}
    } 
    
    @PostMapping("actualizarDireccionesMurcia")
    public void desdeMurcia() {
    	province = provinceDao.findById(30).orElse(null);
    	prov = "30";
    	Response township = restTemplate.getForObject("https://apiv1.geoapi.es/municipios?CPRO="+ prov  + endUrl, Response.class);
    	createTownship(township,new TownShip());
    }
    
    public TownShip pueblo = new TownShip();
    
    @PostMapping("restoMurcia")
    public void restoMurcia() {
    	province = provinceDao.findById(30).orElse(null);
    	prov = "30";
    	Response township = restTemplate.getForObject("https://apiv1.geoapi.es/municipios?CPRO=" + prov + endUrl, Response.class);
    	for(ClassResponse town : township.getDataJson()) {
    		//if(Integer.parseInt(town.getCmum()) >21 && Integer.parseInt(town.getCmum()) < 100) { 
    		//if(Integer.parseInt(town.getCmum()) == 901) { 
	    	if(Integer.parseInt(town.getCmum()) == 902) { 
	    		pueblo.setName(town.getNomMun50());
	    		pueblo.setCod(Integer.parseInt(town.getCmum()));
	    		pueblo.setProvince(province);
			    this.township = townshipDao.save(pueblo);
			    this.town = town.getCmum();
			    	Response poblacion = restTemplate.getForObject("https://apiv1.geoapi.es/poblaciones?CPRO=" + prov + "&CMUM=" + this.town + endUrl ,Response.class);
			    	saltarProblacionesYNucleos(poblacion);
	    	}
    	}
    }
	

    public void createTownship(Response responseTownShip, TownShip townshipDB) {
		for (ClassResponse townshipAPI : responseTownShip.getDataJson()) {
			townshipDB.setName(townshipAPI.getNomMun50());
			townshipDB.setCod(Integer.parseInt(townshipAPI.getCmum()));
			townshipDB.setProvince(province);
		    township = townshipDao.save(townshipDB);
		    town = townshipAPI.getCmum();
		    	Response poblacion = restTemplate.getForObject("https://apiv1.geoapi.es/poblaciones?CPRO=" + prov + "&CMUM=" + town + endUrl ,Response.class);
		    	saltarProblacionesYNucleos(poblacion);
		}
    }
    
    public void saltarProblacionesYNucleos(Response responsePoblacion) {
    	for (ClassResponse poblacionAPI : responsePoblacion.getDataJson()) {
    		pob = poblacionAPI.getNentsi50();
    		Response nucleos = restTemplate.getForObject("https://apiv1.geoapi.es/nucleos?CPRO=" + prov + "&CMUM=" + town + "&NENTSI50="+ pob + endUrl ,Response.class);
    			for(ClassResponse nucleo : nucleos.getDataJson()) {
    				nuc = nucleo.getCun();
    				Response postalCodes = restTemplate.getForObject("https://apiv1.geoapi.es/cps?CPRO=" +prov+ "&CMUM=" +town+ "&CUN=" + nuc + endUrl,Response.class);
    				createPostalCode(postalCodes, new PostalCode());
    			}
    	}
    }
	

    public void createPostalCode(Response responsePostalCode, PostalCode postalCodeDB) {
		for (ClassResponse postalcodeAPI : responsePostalCode.getDataJson()) {
			postalCodeDB.setCod(Integer.parseInt(postalcodeAPI.getCpos()));
			postalCodeDB.setCodTownship(township);
		    postalCodeDao.save(postalCodeDB);
		}
    } 
	

}

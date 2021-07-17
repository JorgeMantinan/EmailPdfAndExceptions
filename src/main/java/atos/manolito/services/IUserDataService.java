package atos.manolito.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import atos.manolito.entity.UserData;
@Service
public interface IUserDataService {
	
	public List<UserData> findAll();
	
	public UserData findById(int id);
	
	public UserData findByDasId(String dasId);
	
	public UserData save(UserData userData);
	
	public void delete(int id);

	public boolean isDasAlreadyInUse(String dasId);

	public List<UserData> filter(String dasId, String name, String surname1);
	// FGS 15/11/19
	public List<UserData> findAllWithoutDataExtended();
	
	public Page<UserData> search(int page, int size, String ordenationBy, boolean order, String dasId, String name, String surname1, String surname2, String mail, int state);


}

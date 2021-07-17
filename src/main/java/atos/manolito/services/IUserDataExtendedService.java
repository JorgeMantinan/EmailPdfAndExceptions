package atos.manolito.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import atos.manolito.entity.UserDataExtended;

/**
 * 
 * @author FGS
 * @since  11/11/2019
 *
 */
@Service
public interface IUserDataExtendedService {
	
	public List<UserDataExtended> findAll();
	
	public UserDataExtended findById(int id);
	
	public UserDataExtended save(UserDataExtended ude);
	
	public void delete(int id);
	

	// FGS 10/12/19. Antes había un sólo método porque el identificador estaba en un único campo.
	public boolean isNifAlreadyInUse(String idCard);
	public boolean isNieAlreadyInUse(String idCard);
	public boolean isPassportAlreadyInUse(String idCard);
	
	// FGS 12/11/19
	public boolean isSSNumberAlreadyInUse(String ssNumber);
	
	// FGS 11/12/19
	public boolean isIbanNumberAlreadyInUse(String ibanNumber);

	Page<UserDataExtended> findAll(Pageable pageable);

	boolean isValidIbanNumber(String ibanNumber);

	boolean isIbanNumberAlreadyInUse(String ibanNumber, int userId);

	boolean isSSNumberAlreadyInUse(String ssNumber, int userId);

	boolean isNifAlreadyInUse(String idCard, int userId);

	boolean isNieAlreadyInUse(String idCard, int userId);

	boolean isPassportAlreadyInUse(String idCard, int userId);

	Page<UserDataExtended> findAll(Pageable pageable, String dasId, String name, String surname1, String nie,
			String nif, String passport, String ssNumber);


}

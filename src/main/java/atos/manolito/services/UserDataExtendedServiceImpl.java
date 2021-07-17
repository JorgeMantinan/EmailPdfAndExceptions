package atos.manolito.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.hibernate.exception.JDBCConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import atos.manolito.Messages;
import atos.manolito.dao.IUserDataExtendedDao;
import atos.manolito.entity.UserDataExtended;
import atos.manolito.exceptions.GeneralException;
import atos.manolito.exceptions.MyDataAccessException;
import atos.manolito.security.constraint.IbanConstraintValidator;

/**
 * 
 * @author FGS
 * @since  11/11/2019
 *
 */
@Service
public class UserDataExtendedServiceImpl implements IUserDataExtendedService {

	@Autowired
	Messages messages;
	
	@Autowired
	IUserDataExtendedDao udeDao;
	
	
	/**
	 * Devuelvo los datos extendidos de todos los usuarios.
	 * Como existe una asociación con el objeto que tiene los datos normales
	 * el usuario, que llevan la lista de roles, debo cortar la recursividad
	 * de roles con usuarios.
	 * 
	 * @author FGS
	 * @since  11/11/2019
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<UserDataExtended> findAll() {
		List<UserDataExtended> listUdeNotRec = new ArrayList<>();
		try {
			listUdeNotRec = udeDao.findAll();
		} catch (JDBCConnectionException jdbce) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_USERSDATAEXTENDED",null));
		} catch (NullPointerException npe) {
			throw new GeneralException(messages.get("ERROR_DATABASE_NULL_USERDATAEXTENDED",null));
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_USERSDATAEXTENDED",null));
		}

		return listUdeNotRec;
	}

	@Override
	@Transactional(readOnly = true)
	public Page<UserDataExtended> findAll(Pageable pageable,String dasId,String name,String surname1,String nie,String nif,String passport,String ssNumber) {
		Page<UserDataExtended> page;
		try {
			page = udeDao.search(pageable,dasId,name,surname1,ssNumber);
		} catch (JDBCConnectionException jdbce) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_USERSDATAEXTENDED",null));
		} catch (NullPointerException npe) {
			throw new GeneralException(messages.get("ERROR_DATABASE_NULL_USERDATAEXTENDED",null));
		} catch (Exception e) {
			throw new GeneralException(e.getLocalizedMessage());
		}
		
		return page;
	}
	@Override
	@Transactional(readOnly = true)
	public UserDataExtended findById(int id) {
		UserDataExtended udeInDB = null;
		try {
			udeInDB = udeDao.findById(id).orElse(null);
		} catch(DataAccessException dae) {
			throw new MyDataAccessException(messages.get("ERROR_DATABASE_GET_USERDATAEXTENDED",null));
		} catch (Exception e) {
			throw new GeneralException(messages.get("ERROR_DATABASE_GET_USERDATAEXTENDED",null));
		}

		// FGS 20/11/19. corto la recursividad a través de userData
		if(udeInDB != null) {
		udeInDB.getUserData().setUserDataExtended(null);
		udeInDB.getAddresses().forEach(address -> address.setUserdataextended(null));
		// Corto la recursividad con roles a través de userData
		udeInDB.getUserData().getRoles().forEach(role -> role.setUsersData(new HashSet<>()));

		}
		return udeInDB;
	}

	@Override
	@Transactional
	public UserDataExtended save(UserDataExtended userDataExtended) {
		//FGS 10/12/19 Ahora nif/nie/pasaporte son tres  campos separados
		/*if (userDataExtended.getIdentityCard()!= null &&
				userDataExtended.getIdentityCard().length()<9) {
			userDataExtended.setIdentityCard(null);
			userDataExtended.setIdentityCardType(0);
		}*/
		if (userDataExtended.getSsNumber() != null &&
				userDataExtended.getSsNumber().length() < 12)
			userDataExtended.setSsNumber(null);
		if (userDataExtended.getIbanNumber()!=null &&
				userDataExtended.getIbanNumber().length() < 24)
			userDataExtended.setIbanNumber(null);
		
		return udeDao.save(userDataExtended);
	}

	@Override
	@Transactional
	public void delete(int id) {
		udeDao.deleteById(id);
	}

	/**
	 * 
	 * @author FGS
	 * @since  12/11/2019
	 * 
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean isNifAlreadyInUse(String idCard) {
		boolean isIdCardInDb = false;
		 
		if (idCard!= null && udeDao.findByNif(idCard.toUpperCase()) != null) 
			isIdCardInDb = true;

		return isIdCardInDb;
	}
	/**
	 * 
	 * @author FGS
	 * @since  24/12/2019
	 * 
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean isNifAlreadyInUse(String idCard, int userId) {
		boolean isIdCardInDB = false;
		UserDataExtended userInDB = null;
		
		if (idCard != null ) {
			userInDB = udeDao.findByNif(idCard.toUpperCase());			
		}
		if (userInDB != null) { // Un usuario distinto está intentando tomar un NIF de otro			
			if (userInDB.getId() != userId) {				
				isIdCardInDB = true;
			} 				
		}	
		
		return isIdCardInDB;
	}
	/**
	 * 
	 * @author FGS
	 * @since  10/12/2019
	 * 
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean isNieAlreadyInUse(String idCard) {
		boolean isIdCardInDb = false;
		 
		if (idCard!= null && udeDao.findByNie(idCard.toUpperCase()) != null) 
			isIdCardInDb = true;

		return isIdCardInDb;
	}
	
	/**
	 * 
	 * @author FGS
	 * @since  24/12/2019
	 * 
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean isNieAlreadyInUse(String idCard, int userId) {
		boolean isIdCardInDB = false;
		UserDataExtended userInDB = null;
		
		if (idCard != null ) {
			userInDB = udeDao.findByNie(idCard.toUpperCase());			
		}
		if (userInDB != null) { // Un usuario distinto está intentando tomar un NIF de otro			
			if (userInDB.getId() != userId) {				
				isIdCardInDB = true;
			} 				
		}			
		return isIdCardInDB;
	}
	/**
	 * 
	 * @author FGS
	 * @since  12/11/2019
	 * 
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean isPassportAlreadyInUse(String idCard) {
		boolean isIdCardInDb = false;
		 
		if (idCard!= null && udeDao.findByPassport(idCard.toUpperCase()) != null) 
			isIdCardInDb = true;

		return isIdCardInDb;
	}
	/**
	 * 
	 * @author FGS
	 * @since  24/12/2019
	 * 
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean isPassportAlreadyInUse(String idCard, int userId) {
		boolean isIdCardInDB = false;
		UserDataExtended userInDB = null;
		
		if (idCard != null ) {
			userInDB = udeDao.findByPassport(idCard.toUpperCase());			
		}
		if (userInDB != null) { // Un usuario distinto está intentando tomar un NIF de otro			
			if (userInDB.getId() != userId) {				
				isIdCardInDB = true;
			} 				
		}	
		
		return isIdCardInDB;
	}
	/**
	 * 
	 * @author FGS
	 * @since  12/11/2019
	 * 
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean isSSNumberAlreadyInUse(String ssNumber) {
		boolean isSSNumberInDb = false;
		 
		if (ssNumber!= null && udeDao.findBySSNumber(ssNumber) != null) 
			isSSNumberInDb = true;

		return isSSNumberInDb;
	}

	/**
	 * 
	 * @author FGS
	 * @since  26/12/2019
	 * 
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean isSSNumberAlreadyInUse(String ssNumber, int userId) {
		boolean isSSNumberInDB = false;
		UserDataExtended userInDB = null;
		
		if (ssNumber != null ) {
			userInDB = udeDao.findBySSNumber(ssNumber);			
		}
		if (userInDB != null) { // Un usuario distinto está intentando tomar un Núm.SS de otro			
			if (userInDB.getId() != userId) {				
				isSSNumberInDB = true;
			} 				
		}				
		return isSSNumberInDB;
	}
	
	/**
	 * 
	 * @author FGS
	 * @since  11/12/2019
	 * 
	 */
	@Override
	public boolean isIbanNumberAlreadyInUse(String ibanNumber) {
		boolean isIbanNumberInDb = false;
		
		
		if (ibanNumber!= null && udeDao.findByIbanNumber(ibanNumber.toUpperCase()) != null) 
			isIbanNumberInDb = true;

		return isIbanNumberInDb;
	}
	
	/**
	 * 
	 * @author FGS
	 * @since  26/12/2019
	 * 
	 */
	@Override
	public boolean isIbanNumberAlreadyInUse(String ibanNumber, int userId) {
		boolean isIbanNumberInDB = false;
		//Contendrá el usuario que posee ese IBAN, o null si no existe el IBAN.
		UserDataExtended userInDB = null;
		
		if (ibanNumber!= null){
			userInDB = udeDao.findByIbanNumber(ibanNumber.toUpperCase());
		}
				
		if (userInDB != null) { // Un usuario distinto está intentando tomar un IBAN de otro			
			if (userInDB.getId() != userId) {				
				isIbanNumberInDB = true;
			} 				
		}		
		return isIbanNumberInDB;
	}
	
	@Override	
	public boolean isValidIbanNumber(String ibanNumber){

		boolean isValid = false;

		IbanConstraintValidator ibanValidator = new IbanConstraintValidator();
		isValid = ibanValidator.isValid(ibanNumber,null);
		
		return isValid;
	}

	@Override
	public Page<UserDataExtended> findAll(Pageable pageable) {
//		Page<UserDataExtended> page;
//		try {
//			page = (Page<UserDataExtended>) udeDao.search(pageable);
//		} catch (JDBCConnectionException jdbce) {
//			throw new GeneralException(messages.get("ERROR_DATABASE_GET_USERSDATAEXTENDED",null));
//		} catch (NullPointerException npe) {
//			throw new GeneralException(messages.get("ERROR_DATABASE_NULL_USERDATAEXTENDED",null));
//		} catch (Exception e) {
//			throw new GeneralException(messages.get("ERROR_DATABASE_GET_USERSDATAEXTENDED",null));
//		}
		
		return null;
	}
	
}

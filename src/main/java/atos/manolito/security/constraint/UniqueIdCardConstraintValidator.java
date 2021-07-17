package atos.manolito.security.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import atos.manolito.services.IUserDataExtendedService;

/**
 * Esta clase comprueba si el identificador recibido (NIF, NIE o pasaporte)
 * ya se encuentra en la Base de Datos y está, por tanto, siendo usado por
 * otro usuario. 
 * 
 * @author FGS
 * @since  12/11/2019
 * 
 *
 */
public class UniqueIdCardConstraintValidator 
	implements ConstraintValidator<UniqueIdCard, String> {
	
	@Autowired
	private IUserDataExtendedService udeService;
	
	@Override
	public boolean isValid(String idCard, ConstraintValidatorContext context) {
		System.out.println("UniqueIdCardConstraintValidator. Recibido idCard = " + idCard);
		return idCard != null 
				&& !udeService.isNifAlreadyInUse(idCard);
	}

}

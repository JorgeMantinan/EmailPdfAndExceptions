package atos.manolito.security.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import atos.manolito.services.IUserDataExtendedService;

/**
 * Esta clase comprueba si el número de la seguridad social recibido
 * ya se encuentra en la Base de Datos y está, por tanto, siendo usado por
 * otro usuario. 
 * 
 * @author FGS
 * @since  12/11/2019
 * 
 *
 */
public class UniqueSSNumberConstraintValidator 
	implements ConstraintValidator<UniqueSSNumber, String> {

	@Autowired
	private IUserDataExtendedService udeService;
	
	@Override
	public boolean isValid(String ssNumber, ConstraintValidatorContext context) {
		System.out.println("UniqueSSNumberConstraintValidator. Recibido ssnumber = " + ssNumber);
		return ssNumber != null 
				&& !udeService.isSSNumberAlreadyInUse(ssNumber);
	}
	

}

package atos.manolito.security.constraint;

import java.math.BigInteger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import atos.manolito.Constants;
import atos.manolito.utils.ValidationUtils;

/**
 * Validador del IBAN. Valida solo números de cuenta de España.
 * @author FGS
 * @since  08/11/2019
 * 
 * FORMATO DE IBAN EN ESPAÑA: 24 caracteres
 *  cod_pais DC cod_entidad cod_sucurs DC num_cuenta
 *   ES      98	   2038	    5778	   98 3000760236
 */
public class IbanConstraintValidator implements ConstraintValidator<ValidIban, String>  {
	
	// FGS indica si el atributo puede no estar presente. Si no está, no se intenta la validación.
	private boolean isOptional;
		
	@Override
    public void initialize(ValidIban constraint) {
		isOptional = constraint.optional();
    }
	
	/**
	 * 
	 * @author FGS
	 * @since  11/11/2019
	 */
	@Override
	public boolean isValid(String strIban, ConstraintValidatorContext context) {
		boolean isValid = false;
		// Índice del String donde comienzan los números
		BigInteger remainder = new BigInteger("0");
		int cd = 0;
		String strCD = "";
		BigInteger biAccountNumber = new BigInteger("0"); 

		
		// Compruebo que la longitud total sea correcta y cominence por "ES".
		if(strIban != null) { // FGS 10/12/19 cambios para corregir un error anterior no detectatod.
			if ( strIban.length() == 24 && strIban.substring(0,1).equalsIgnoreCase("E") 
					&& strIban.substring(1,2).equalsIgnoreCase("S")) {
				// Compruebo que el resto de caracteres sean números.
				isValid = ValidationUtils.isNumeric(strIban.substring(2));
				// Convierto el String a un número para calcular el DC.
				if(isValid) {
					// Se reemplazan las letras por números A=10,...,E=14,...S=28. Y los DC = 00.
					biAccountNumber = new BigInteger(strIban.substring(4,24) + "142800");
					// Se realiza la división por 97 y se toma el resto.
					remainder = biAccountNumber.mod(Constants.IBANNUMBER_DIVISOR);
					// Los DC salen de restar a 98 el resto de la división anterior.
					cd = Constants.IBANNUMBER_ADDEND.subtract(remainder).intValue();
					strCD = String.valueOf(cd);
				}	
				// Añado un cero para tener 2 dígitos.
				if(cd < 10) {
					strCD = "0" + strCD;
				} 
	
				// Comparo los dígitos de control calculado con los caracteres 2 y 3 de la cuenta recibida.
				if(strIban.substring(2,4).equals(strCD)) {
					isValid = true;
				} else {
					isValid = false;
				}
			}
		} else { //FGS 22/11/19. Corrección de error detectado tras entrega de código.
			// Si parámetro optional==true -> el identificador pasa la validación.
			isValid = isOptional;
		}
		
		return isValid;
	}


}

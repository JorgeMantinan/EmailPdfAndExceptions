package atos.manolito.security.constraint;

import java.math.BigInteger;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import atos.manolito.Constants;
import atos.manolito.utils.ValidationUtils;

public class SSNumberConstraintValidator 
		implements ConstraintValidator<ValidSSNumber, String>{
	
	// FGS indica si el atributo puede no estar presente. Si no está, no se intenta la validación.
	private boolean isOptional;
		
	@Override
    public void initialize(ValidSSNumber constraint) {
		isOptional = constraint.optional();
    }
	
	/**
	  * Validador del número de la Seguridad Social.
	  * Formato: 12 dígitos: COD_PROV(2) NUM_ASIGNADO(10) DC(2)
	  * Los dígitos de control(DC) se calculan a partir del código de
	  * provincia y del número asignado al afiliado.
	  * 
	  * Si el número de afiliado (na), < 10 millones, => DC = el resto de dividir entre 97 el resultado de esta operación:
	  * na + cp *10000000 . Siendo cp el código de provincia
	  * Si el número de afiliado (na), >= 10 millones => el resto de dividir entre 97 el concatenado de $a y $b:
	  * cp.concat(na)
	  * 
	  * @author FGS
	  * @since  08/11/2019
	  * @param strSSNumber Cadena de texto con el número de la SS cuya 
	  *     validez se va a comprobar.
	  * @return True si la cadena recibida contiene un número de la 
	  *    Seguridad Social correcto o False en otro caso.
	  */
	@Override
	public boolean isValid(String strSSNumber, ConstraintValidatorContext context) {
		boolean isValid = false;
		//Contendrá el número formado por el código de provincia y el número asignado.
		BigInteger biNumber = new BigInteger("0");
		// número del afiliado a la SS
		BigInteger biAffiliateNumber = null;
		int cd = 0; // dígitos de control en formato numérico.
		String strCD = null; //cadena con los dígitos de control
		
				
		// El número de la SS debe tener 12 dígitos.
		if(strSSNumber != null) 
		{//FGS 10/12/19 Cambiado porque con la anterior condición no validaba bien.
			if (strSSNumber.length() == 12 
					&& ValidationUtils.isNumeric(strSSNumber)) {
				//Tomo el código de provincia y el número para calcular DC.
				biAffiliateNumber = new BigInteger(strSSNumber.substring(2, 10));
				//Tomo el código de provincia y el número para calcular DC.
				if (biAffiliateNumber.compareTo(new BigInteger("10000000"))>0) {
					biNumber = new BigInteger(strSSNumber.substring(0,10));
				} else {
					biNumber = biAffiliateNumber.
							add(new BigInteger(strSSNumber.substring(0,2) + "0000000"));
				}
				//Calculo los dígitos de control.
				cd = biNumber.mod(Constants.SSNUMBER_DIVISOR).intValue();
				strCD = String.valueOf(cd);
				//Añado un cero por la izquierda para que el DC tenga 2 caracteres.
				if(cd < 10) {
					strCD = "0" + strCD;
				} 
				// Compruebo que el DC calculado coincida con el recibido.
				if(strSSNumber.substring(10,12).equals(strCD)) {
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
	
	public static boolean isValidCodProv(String strCodProv) {
		boolean isValid = false;
		
		if( ValidationUtils.isNumeric(strCodProv) && 
			(( 0 < Integer.parseInt(strCodProv) && Integer.parseInt(strCodProv) < 51) 
					||(Integer.parseInt(strCodProv)==53) ||(Integer.parseInt(strCodProv)==66))) 
		{
			isValid =true;
		}
		System.out.println("El código de provincia " + strCodProv + " es un código válido?: " + isValid);
		return isValid;
	}
}

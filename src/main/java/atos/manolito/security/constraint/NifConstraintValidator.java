package atos.manolito.security.constraint;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import atos.manolito.Constants;
import atos.manolito.utils.ValidationUtils;

public class NifConstraintValidator implements ConstraintValidator<ValidNif, String>  {
		// FGS indica si el atributo puede no estar presente. Si no está, no se intenta la validación.
		private boolean isOptional;
		// FGS 10/12/19 introducido para los mensajes de error de validación.
		private List<String> messages = null;
		
		@Override
	    public void initialize(ValidNif constraint) {	
			isOptional = constraint.optional();
			messages = new ArrayList<String>();
	    }
		
		 /**
		  * Validador del número de identificación del usuario.
		  * Puede ser un NIF, un NIE o un pasaporte.
		  * Formato (NIF).      9 Caracteres: 8 números y 1 letra.
		  * 
		  * @author FGS
		  * @since  11/11/2019
		  * @param strIdCard
		  * @param context
		  * @return
		  */
		@Override
		public boolean isValid(String strIdCard, ConstraintValidatorContext context) {
			boolean isValid = false;
			
			messages.clear();
			
	        //if (strIdCard != null && strIdCard.length()==9) {
			if (strIdCard != null ) {
				if (strIdCard.length()==9){
					strIdCard = strIdCard.toUpperCase(); 
		        	if (strIdCard.matches(Constants.NIF_REGEX)) 
		        		isValid = isValidDni(strIdCard);
			        else
			        	messages.add("El DNI no cumple el patrón (8 dígitos y una letra). Ej: 12345678Z");
			        		
		        } else {
		        	messages.add("La longitud es incorrecta. Deben ser 9 caracteres");
		        }
	        	    				
	        } else { //FGS 22/11/19. Corrección de error detectado tras entrega de código.
				// Si parámetro optional==true -> el identificador pasa la validación, ya que si no hay dato no se debe realizar validación.
				isValid = isOptional;
			}	
	        
	        if (isValid) {
	            return true;
	        } 
	        
	        String messageTemplate = messages.stream()
	            .collect(Collectors.joining(","));
	        System.out.println("**********LISTADO DE ERRORES DE VALIDACIÓN*********************");
	        System.out.println(messageTemplate);
	        if (context != null) {
	        	context.buildConstraintViolationWithTemplate(messageTemplate)
	        		.addConstraintViolation()
	        		.disableDefaultConstraintViolation();
	        }
			
	        return false;
		}
		 /**
		  * Este método comprueba si un DNI es válido, es decir, si la letra se 
		  * corresponde con los números.
		  * 
		  * @author FGS
		  * @since	11/11/2019
		  * @param strDni
		  * @return
		  */
		 //private static boolean isValidDni(String strDni) {
		private boolean isValidDni(String strDni) {
			 boolean isValidDni = false;
			 // Cadena auxiliar para determinar la letra correspondiente a un número
			 //de DNI.
			 String validLetters="TRWAGMYFPDXBNJZSQVHLCKE";
			 char calculatedLetter = ' ';
			 int indexLetter = 0;
			 
			 if (ValidationUtils.isNumeric(strDni.substring(0,8))) {
				 indexLetter = Integer.parseInt(strDni.substring(0,8)) % 23;
				 calculatedLetter = validLetters.charAt(indexLetter);
			    
				 isValidDni = strDni.substring(8,9).
		        			equalsIgnoreCase(Character.toString(calculatedLetter));
				 if (!isValidDni)
					 messages.add("La letra no se corresponde con los números.");
			 } else {
				 messages.add("No contiene 8 dígitos.");
			 }
			 
			 
			 return isValidDni;
		 }
}

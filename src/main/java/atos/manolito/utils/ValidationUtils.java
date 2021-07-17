package atos.manolito.utils;

public class ValidationUtils {

	 /**
	  * Comprueba si una cadena de texto contiene un número entero.
	  * 
	  * @author FGS
	  * @since  08/11/2019
	  * @param strSSNumber
	  * @return
	  */
	 public static boolean isNumeric(String strNumber) {
		 boolean isNumeric = false;
		 int asciiChar = 0;
		 int index = 0;
		 
		 if (strNumber !=null ) {
			 do {
				 asciiChar = strNumber.codePointAt(index);
				 // Tiene que ser un dígito. Código ASCII del 48 (0) al 57 (9)
				 isNumeric = (asciiChar > 47 && asciiChar < 58);
				 index++;
			 } while(index < strNumber.length() && isNumeric);	 
		 }		  
		 
		 return isNumeric;
	 }
}

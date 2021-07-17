package atos.manolito.exceptions;

public class ValidationFieldException extends RuntimeException{
	
	/**
	 * Excepcion personalizada cuando falla la validación de variables.
	 * @param messageException Sirve para pasarle un mensaje personalizado de error desde los throw.
	 * 
	 * 
	 * task U2009
	 * @author JMM
	 * @since 27/11/19
	 */
	private static final long serialVersionUID = 1L;
	public ValidationFieldException(String messageException) {
		super(messageException);
	}
}

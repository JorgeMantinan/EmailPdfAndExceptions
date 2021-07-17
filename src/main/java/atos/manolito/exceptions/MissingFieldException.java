package atos.manolito.exceptions;

public class MissingFieldException extends RuntimeException{
	
	/**
	 * Excepcion personalizada cuando no encuentra una variable.
	 * @param messageException Sirve para pasarle un mensaje personalizado de error desde los throw.
	 * 
	 * 
	 * task U2009
	 * @author JMM
	 * @since 27/11/19
	 */
	private static final long serialVersionUID = 1L;
	public MissingFieldException(String messageException) {
		super(messageException);
	}
}

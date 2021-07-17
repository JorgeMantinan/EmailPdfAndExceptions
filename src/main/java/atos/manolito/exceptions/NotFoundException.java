package atos.manolito.exceptions;

public class NotFoundException extends RuntimeException{
	
	/**
	 * Excepcion personalizada.
	 * @param messageException Sirve para pasarle un mensaje personalizado de error desde los throw.
	 * 
	 * 
	 * task U2009
	 * @author JMM
	 * @since 27/11/19
	 */
	private static final long serialVersionUID = 1L;
	public NotFoundException(String messageException) {
		super(messageException);
	}
}

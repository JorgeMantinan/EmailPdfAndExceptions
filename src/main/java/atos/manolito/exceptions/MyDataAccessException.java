package atos.manolito.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class MyDataAccessException extends RuntimeException{
	
	/**
	 * Excepcion personalizada para la captura de excepciones relacionadas con la validación de campos.
	 * @param messageException Sirve para pasarle un mensaje personalizado de error desde los throw.
	 * 
	 * 
	 * task U2009
	 * @author JMM
	 * @since 26/11/19
	 */
	private static final long serialVersionUID = 1L;
	public MyDataAccessException(String messageException) {
		super(messageException);
	}

}

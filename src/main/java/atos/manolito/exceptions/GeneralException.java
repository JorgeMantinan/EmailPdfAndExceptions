package atos.manolito.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class GeneralException extends RuntimeException{
	
	// U2009-JMM-26/11/19
	private static final long serialVersionUID = 1L;
	public GeneralException(String exception) {
		super(exception);
	}
		
}

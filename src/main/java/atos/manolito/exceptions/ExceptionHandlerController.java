package atos.manolito.exceptions;

import java.net.ConnectException;
import java.sql.SQLNonTransientConnectionException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.exception.JDBCConnectionException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import atos.manolito.Constants;
import atos.manolito.Messages;

/**
 * Controlador general de las excepciones de la aplicación.
 * 
 * @author JMM
 * @since  26/11/2019
 * 
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class ExceptionHandlerController {
	
	// Enlaza con los archivos de .properties
	@Autowired
    Messages messages;
	
	//private Logger logEx = LoggerFactory.getLogger(ExceptionHandlerController.class);
	private Logger logEx = (Logger) LogManager.getLogger(ExceptionHandlerController.class);
	
	//Para mostrar a que dia y hora fue la excepcion
	private static final DateTimeFormatter realTime = DateTimeFormatter.ofPattern("dd-MM-yyy HH:mm:ss");
	
	Map<String,Object>response = new HashMap<>();
	
	// Para controlar excepciones globales
	@ExceptionHandler({ GeneralException.class, Exception.class, SQLNonTransientConnectionException.class,
	    ConnectException.class, JDBCConnectionException.class, NullPointerException.class}) 
    public ResponseEntity<Map<String, Object>> generalException(GeneralException ex, WebRequest request) {
		String strExceptionCause = null;
		
	if (ex.getMessage() != null) {
		//System.out.println(ex.getCause().getMessage());
		System.err.println(ex.getMessage());
		if (ex.getCause() != null) {
			strExceptionCause = ex.getCause().getMessage();
		} else {
			strExceptionCause = "Causa desconocida";
		}
			
		logEx.error(Constants.ERROR.concat(ex.getMessage()).concat(strExceptionCause)
				.concat(Constants.TIME.concat(realTime.format(LocalDateTime.now()))));
	}
	response.put(Constants.MESSAGE,messages.get("ERROR_GENERIC",null).concat(Constants.TIME.concat(realTime.format(LocalDateTime.now()))));
	response.put(Constants.ERROR, ex.getMessage().concat(": ").concat(strExceptionCause));

	return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	@ExceptionHandler({ NotFoundException.class })
    public ResponseEntity<Map<String, Object>> userNotFound(NotFoundException ex, WebRequest request) {
	response.put(Constants.ERROR, ex.getLocalizedMessage());
	return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler({ MissingFieldException.class })
    public ResponseEntity<Map<String, Object>> missingFieldsException(MissingFieldException ex, WebRequest request) {
	response.put(Constants.ERROR, ex.getLocalizedMessage());
	return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler({ ValidationFieldException.class})
    public ResponseEntity<Map<String, Object>> validationFieldsException(ValidationFieldException ex, WebRequest request) {
		
		response.put(Constants.ERROR, ex.getLocalizedMessage());
		
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }
	
	// FGS 30/12/19 Prueba para ver si esto captura los errores cuando hay problemas de conexión con la BBDD
	/*@ExceptionHandler({ java.net.ConnectException.class })
    public ResponseEntity<Map<String, Object>> dbConnectionException(MethodArgumentNotValidException ex, WebRequest request) {
		System.out.println("************ERROR: " + ex.getLocalizedMessage());
		//response.put(Constants.ERROR, ex.getLocalizedMessage());
		response.put(Constants.ERROR, ex.getMessage());
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }*/
	
}
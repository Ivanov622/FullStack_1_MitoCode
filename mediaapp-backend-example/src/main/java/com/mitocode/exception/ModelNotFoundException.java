package com.mitocode.exception;

//@ResponseStatus(HttpStatus.NOT_FOUND) //con esto controlo mejor la salida y envía el código de la excepción
//la de arriba se comentó porque ya el manejo de la excepción se hace en la clase ResponseExceptionHandler, 
//se refiere al HttpStatus.NOT_FOUND
public class ModelNotFoundException extends RuntimeException {
	
	
	private static final long serialVersionUID = 1L;

	//constructor	
	public ModelNotFoundException (String message) {
		super(message);
	}

}

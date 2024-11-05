package com.mitocode.exception;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

//Entonces ya en esta clase se meten todas las excepciones que queramos controlar o sea 
//se crean más métodos como handleModelNotFoundException
// El extends ResponseEntityExceptionHandler es para manejar la excepción por un @Valid x ejemplo en PatientController
@RestControllerAdvice  //intercepta cualquier excepción en el proyecto, evita estar haciendo try-catch en cada método
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
	
	// Así se manejaba antes de springboot 3	
	
	@ExceptionHandler(ModelNotFoundException.class) //para manejar la excepción ModelNotFoundException
	public ResponseEntity<CustomErrorResponse> handleModelNotFoundException(ModelNotFoundException ex, WebRequest request){
		//request.getDescription(false) es una descripción más abreviada que con el true
		CustomErrorResponse err = new CustomErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<> (err, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
		
	/*
	// Así con Springboot 3 aparece Problemdetail, también en sb3 se puede hacer con ErrorResponse
	@ExceptionHandler(ModelNotFoundException.class)
	public ProblemDetail handleModelNotFoundException(ModelNotFoundException ex, WebRequest request){
		
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());		
		problemDetail.setTitle("Model Not Found");
		problemDetail.setType(URI.create("/not-found"));
		problemDetail.setProperty("extra1", "extra_value"); //puedo poner los setproperty que quiera con los valores que quiera y necesite
		problemDetail.setProperty("extra2", 52);
		return problemDetail;
	}
	
	
	//Otra manera de hacerlo en springboot3
	@ExceptionHandler(ModelNotFoundException.class)
	public ErrorResponse handleModelNotFoundException(ModelNotFoundException ex, WebRequest request){
		return ErrorResponse.builder(ex, HttpStatus.NOT_FOUND, ex.getMessage())
				.title("Model Not Found")
				.type(URI.create(request.getContextPath()))
				.property("extra1", "extra_value1")
				.property("extra2", 52)
				.build();    //el build debe ir para que construya todo		
	}
	
	*/
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
		//con este msg hacemos más legible el error 
		//MethodArgumentNotValidException: por ejemplo si enviamos un null a un campo que debería existir
		String msg = ex.getBindingResult().getFieldErrors().stream().map(
	                e -> e.getField().concat(":").concat(e.getDefaultMessage().concat(" "))
	     ).collect(Collectors.joining());

	     CustomErrorResponse err = new CustomErrorResponse(LocalDateTime.now(), msg, request.getDescription(false));
	     return  new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
	}
	
	 @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        CustomErrorResponse err = new CustomErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return  new ResponseEntity<>(err, HttpStatus.BAD_REQUEST);
    }
	
	//para manejar todas las demás excepciones excepto ModelNotFoundException y handleMethodArgumentNotValid
	@ExceptionHandler(Exception.class) 
	public ResponseEntity<CustomErrorResponse> handleAllException(Exception ex, WebRequest request){
		//request.getDescription(false) es una descripción más abreviada que con el true
		CustomErrorResponse err = new CustomErrorResponse(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
		return new ResponseEntity<> (err, HttpStatus.NOT_FOUND);
	}
	

}

package com.mitocode.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//clase para personalizar la salida del error
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomErrorResponse {
	
	private LocalDateTime dateTime;
	private String message;
	private String details;

}

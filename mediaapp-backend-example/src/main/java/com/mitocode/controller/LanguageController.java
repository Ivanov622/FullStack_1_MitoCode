package com.mitocode.controller;


import java.util.Locale;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;


// crea este servicio para no estar recompilando cada vez que cambia el idioma
// inyecta LocaleResolver
@RestController
@RequestMapping("/languages")
@RequiredArgsConstructor
public class LanguageController {
	
	private final LocaleResolver localeResolver;
	private final HttpServletRequest httpServletRequest;
	private final HttpServletResponse httpServletResponse;
	
	//para definir el idioma del messages
	@GetMapping("/locale/{loc}")
	public ResponseEntity<Void> changeLocale(@PathVariable("loc") String loc){
		Locale userLocale = switch(loc) {
			case "en" -> Locale.ENGLISH;
			case "fr" -> Locale.FRENCH;
			default -> Locale.ROOT;
		};
		localeResolver.setLocale(httpServletRequest, httpServletResponse, userLocale);
		return new ResponseEntity<>(HttpStatus.OK);
	}	

}

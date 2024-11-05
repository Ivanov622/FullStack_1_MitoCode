package com.mitocode.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

// se crea esta clase para el manejo de idiomas en los archivos messagesxxx.properties
// al crearse, y estar anotada, spring la crga desde el inicio
@Configuration
public class MessageConfig {
	
	//carga las properties
	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:messages"); //carga los archivos con nombre base "messages"
		return messageSource;
	}
	
	//establecer un default locale
	//acá el problema es que tengo que recompilar cada vez que cambie el idioma
	//para no tenr este problema se crea la clase language controller
	public LocaleResolver localeResolver() {
		SessionLocaleResolver slr = new SessionLocaleResolver();
		slr.setDefaultLocale(Locale.ROOT); //asume como idioma el actual que no tenga extension
		return slr;
		
	}
	
	//para resolver las variables del archivo messages que es el que se eligió en localeResolver()
	@Bean
	public LocalValidatorFactoryBean getValidator() {
		
		LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
		bean.setValidationMessageSource(messageSource());
		return bean;		
	}
	
	

}

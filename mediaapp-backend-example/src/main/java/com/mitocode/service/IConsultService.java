package com.mitocode.service;


import java.util.List;

import com.mitocode.model.Consult;
import com.mitocode.model.Exam;

//similar a lo que se hizo con los IxxxRepo, se saca aparte en ICRUD todos los métodos comunes para centralizar:
//save, findAll, findById, delete, update y se maneja con genéricos
public interface IConsultService extends ICRUD<Consult, Integer> {
	
	//recibe el dto en el controler, este método para desagregarlo y enviarlo a la capa de repo
	Consult saveTransactional(Consult consult, List<Exam> exams); 
	
	
	
	
	
	
}

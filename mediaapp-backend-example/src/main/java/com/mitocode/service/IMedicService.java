package com.mitocode.service;


import com.mitocode.model.Medic;

//similar a lo que se hizo con los IxxxRepo, se saca aparte en ICRUD todos los métodos comunes para centralizar:
//save, findAll, findById, delete, update y se maneja con genéricos
public interface IMedicService extends ICRUD<Medic, Integer> {
	
	
	
}

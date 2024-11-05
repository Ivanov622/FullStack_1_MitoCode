package com.mitocode.service;


import com.mitocode.model.Menu;

//similar a lo que se hizo con los IxxxRepo, se saca aparte en ICRUD todos los métodos comunes para centralizar:
//save, findAll, findById, delete, update y se maneja con genéricos
public interface IMenuService extends ICRUD<Menu, Integer> {
	
	
	
}

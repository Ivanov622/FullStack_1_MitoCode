package com.mitocode.service.impl;

import java.util.List;

import com.mitocode.exception.ModelNotFoundException;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.service.ICRUD;

//Para manejar los métodos comunes con similar comportamiento de los servicios y con genéricos
public abstract class CRUDImpl<T,ID> implements ICRUD<T, ID>{
	
	// toca este para poder usarlo y como es abstracto la clase se vuelve abstracta
	//al ser un genérico para servicios hay un llamado a la interfaz de un repor, pero, como
	// esta interfaz es genérica meto el repo abstracto genérico para poder usarlo
	protected abstract IGenericRepo<T, ID> getRepo();

	@Override
	public T save(T t) {
		return getRepo().save(t);
	}

	@Override
	public T update(T t, ID id) {
		//primero valida que exista y luego si actualiza
		getRepo().findById(id).orElseThrow(() -> new ModelNotFoundException("ID NOT FOUND: "+id));

		return getRepo().save(t);
	}

	@Override
	public List<T> findAll() {

		return getRepo().findAll();
	}

	@Override
	public T findById(ID id) {
		// se captura en una excepción para informar que no se encontraron datos
		//Toca con un lambda o saca error
		return getRepo().findById(id).orElseThrow(() -> new ModelNotFoundException("ID NOT FOUND: "+id));
	}

	@Override
	public void delete(ID id) {
		//primero valida que exista y luego si borra
		getRepo().findById(id).orElseThrow(() -> new ModelNotFoundException("ID NOT FOUND: "+id));		
		getRepo().deleteById(id);		
	}

}

package com.mitocode.service.impl;

import org.springframework.stereotype.Service;

import com.mitocode.model.Patient;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.IPatientRepo;
import com.mitocode.service.IPatientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor  //con esta anotación de lombok hace la inyección de dependencia generando el constructor,
//eso sí exige el final en el atributo
//similar a lo que se hizo con IxxxRepo e IxxxService, 
//saca los métodos comunes , y con similar comportamiento, a una interface y con genéricos ==> CRUDImpl
public class PatientServiceImpl extends CRUDImpl<Patient, Integer> implements IPatientService {
	

	private final IPatientRepo repo;

	@Override
	protected IGenericRepo<Patient, Integer> getRepo() {

		return repo;
	}	
	

}

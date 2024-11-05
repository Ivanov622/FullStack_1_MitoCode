package com.mitocode.service.impl;

import org.springframework.stereotype.Service;

import com.mitocode.model.Specialty;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.ISpecialtyRepo;
import com.mitocode.service.ISpecialtyService;

import lombok.RequiredArgsConstructor;

//ISpecialtyService para el repo y CRUDImpl para el service?
@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl extends CRUDImpl<Specialty, Integer> implements ISpecialtyService {
	

	private final ISpecialtyRepo repo;

	@Override
	protected IGenericRepo<Specialty, Integer> getRepo() {
		
		return repo;
	}
	
}

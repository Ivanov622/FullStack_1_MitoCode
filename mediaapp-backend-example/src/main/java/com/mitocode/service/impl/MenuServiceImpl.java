package com.mitocode.service.impl;

import org.springframework.stereotype.Service;

import com.mitocode.model.Menu;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.IMenuRepo;
import com.mitocode.service.IMenuService;

import lombok.RequiredArgsConstructor;

//IMenuService para el repo y CRUDImpl para el service?
@Service
@RequiredArgsConstructor
public class MenuServiceImpl extends CRUDImpl<Menu, Integer> implements IMenuService {
	

	private final IMenuRepo repo;

	@Override
	protected IGenericRepo<Menu, Integer> getRepo() {
		
		return repo;
	}
	
}

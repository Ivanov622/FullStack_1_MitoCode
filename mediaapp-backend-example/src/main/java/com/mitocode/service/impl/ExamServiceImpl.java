package com.mitocode.service.impl;

import org.springframework.stereotype.Service;

import com.mitocode.model.Exam;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.IExamRepo;
import com.mitocode.service.IExamService;

import lombok.RequiredArgsConstructor;

//IExamService para el repo y CRUDImpl para el service?
@Service
@RequiredArgsConstructor
public class ExamServiceImpl extends CRUDImpl<Exam, Integer> implements IExamService {
	

	private final IExamRepo repo;

	@Override
	protected IGenericRepo<Exam, Integer> getRepo() {
		
		return repo;
	}
	
}

package com.mitocode.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mitocode.model.Consult;
import com.mitocode.model.Exam;
import com.mitocode.repo.IGenericRepo;
import com.mitocode.repo.IConsultExamRepo;
import com.mitocode.repo.IConsultRepo;
import com.mitocode.service.IConsultService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

//IConsultService para el repo y CRUDImpl para el service?
@Service
@RequiredArgsConstructor
public class ConsultServiceImpl extends CRUDImpl<Consult, Integer> implements IConsultService {
	

	private final IConsultRepo consultRepo;
	private final IConsultExamRepo consultExamRepo;

	@Override
	protected IGenericRepo<Consult, Integer> getRepo() {
		
		return consultRepo;
	}

	//Como acá inserta en varias tablas, osea es el origen de toda la transacción, se debe poner el transactional porque
	// en caso de fallo pueda hacer el rollback en TODAS las tablas
	@Transactional
	@Override
	public Consult saveTransactional(Consult consult, List<Exam> exams) {

		 consultRepo.save(consult); //hasta aquí se ha guardado el maestro-detalle Consult-DetailConsult.
		 exams.forEach(ex->consultExamRepo.saveConsultExam(consult.getIdConsult(),ex.getIdExam())); //guardar consult +lista exámenes
		 
		 return consult;
		 
		 
	}
	
}

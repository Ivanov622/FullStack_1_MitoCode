package com.mitocode.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mitocode.model.ConsultExam;

import jakarta.transaction.Transactional;

public interface IConsultExamRepo extends IGenericRepo<ConsultExam, Integer> {
	
	
	//el @Query permite ejecutar el query cuando se llama el método
	//el nativeQuery=true indica que debe ser un sql normal de base de datos
	//en el param el nombre debe ser igual al parámetro.
	//sentencia de modificación @Modifying, se pone en insert,update y delete ya que
	//ya que se sin el modifying espera que retorne un entero porque se usa en select
	//el modifyin y el transactional van juntos. 
	//El Transactional confirma los cambios
	//pero si lo pongo acá solo hace el rollback sobre esta tabla y como estoy insertando en varias
	//las otras no hacen el rollback y queda mal. Se comenta acá y se pone en 
	//ConsultServiceImpl que es el origen de toda la transacción
	@Transactional
	@Modifying
	@Query(value ="INSERT INTO consult_exam(id_consult,id_exam) VALUES(:idConsult,:idExam)", nativeQuery = true)
	Integer saveConsultExam(@Param("idConsult") Integer id_Consult, @Param("idExam") Integer id_Exam);
	
	

}

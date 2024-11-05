package com.mitocode.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@IdClass(ConsultExamPK.class)  //para poder usar esa clase (ConsultExamPK) para resolver la relación
public class ConsultExam {
	
	// No toma los id's de cada clase sino toma la clase como tal y en ConsultExamPk está para 
	//resolver las llaves primarias de esta tabla
	@Id
	private Consult consult;
	
	@Id
	private Exam exam;

}

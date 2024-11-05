package com.mitocode.model;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.EqualsAndHashCode;

//En las entidades las nuevas especificaciones de JPA ya traen el serializable incluído en la notación Entity
@Embeddable // para que la tome ConsultExam
@EqualsAndHashCode  //para que tome la llave primaria como compuesta, es decir las comparaciones
//se hagan tomando los 2 campos
public class ConsultExamPK implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@ManyToOne
	@JoinColumn(name = "id_consult")
	private Consult consult;
	
	@ManyToOne
	@JoinColumn(name = "id_exam")
	private Exam exam;
}

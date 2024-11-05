package com.mitocode.model;

import jakarta.persistence.ForeignKey;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class ConsultDetail {
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idConsultDetail;
	
	@ManyToOne
	@JoinColumn(name = "id_consult",nullable = false, foreignKey = @ForeignKey(name = "FK_CONSULT_DETAIL"))
	private Consult consult;  // este nombre "consult" es el que se mapea desde la clase Consult onetomany mappedBy
	
	@Column(nullable = false, length = 70)
	private String diagnosis;
	
	@Column(nullable = false, length = 300)
	private String treatment;

}

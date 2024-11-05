package com.mitocode.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) //si voy a comparar objetos que lo haga por el marcado(aquí id) y no por referencia
@Entity
public class Medic {
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idMedic;
	
	@Column(nullable = false, length = 70)
	private String firstName;
	
	@Column(nullable = false, length = 70)
	private String lastName;
	
	//cmp cedula de los medicos en perú
	@Column(nullable = false, length = 12)
	private String cmp;
	
	private String photoUrl;
	

}

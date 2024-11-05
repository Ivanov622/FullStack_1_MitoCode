package com.mitocode.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
public class Role {
	
	//aquí no usa el autoincremental para que coincida cuando se trabaje en seguridad, es por el ejercicio no más
	@Id
	@EqualsAndHashCode.Include
	private Integer idRole;	
	
	@Column(nullable = false, length = 50)
	private String name;
	
	@Column(nullable = false, length = 100)
	private String description;

}

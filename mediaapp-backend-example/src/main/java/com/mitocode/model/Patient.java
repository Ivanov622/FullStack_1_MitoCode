package com.mitocode.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;

//En las entidades las nuevas especificaciones de JPA ya traen el serializable incluído en la notación Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) //si voy a comparar objetos que lo haga por el marcado(aquí id) y no por referencia
@Entity
public class Patient {
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idPatient;
	
	@Column(length = 70 , nullable = false)
	private String firstName;
	
	@Column(length = 70 , nullable = false)
	private String lastName;
	
	@Column(length = 12 , nullable = false)
	private String dni;
	
	@Column(length =150, nullable = false)
	private String address;
	
	@Column(length = 12, nullable = false)
	private String phone;
	
	@Column(length = 55, nullable = false)
	private String email;

}

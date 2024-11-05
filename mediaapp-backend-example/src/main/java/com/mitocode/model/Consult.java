package com.mitocode.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Consult {
	
	@Id
	@EqualsAndHashCode.Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer idConsult;
	
	//join column es la columna de esta tabla, Consult, que es la foránea hacia la de paciente
	@ManyToOne //FK . JoinColumn para nombre con fk y tablas foreignKey = @ForeignKey(name = "FK_CONSULT_PATIENT")
	@JoinColumn(name = "id_patient", nullable = false, foreignKey = @ForeignKey(name = "FK_CONSULT_PATIENT"))
	private Patient patient;
	
	@ManyToOne //FK . JoinColumn para nombre con fk y tablas foreignKey = @ForeignKey(name = "FK_CONSULT_MEDIC")
	@JoinColumn(name = "id_medic", nullable = false, foreignKey = @ForeignKey(name = "FK_CONSULT_MEDIC"))
	private Medic medic;

	@ManyToOne //FK . JoinColumn para nombre con fk y tablas foreignKey = @ForeignKey(name = "FK_CONSULT_Specialty")
	@JoinColumn(name = "id_specialty", nullable = false, foreignKey = @ForeignKey(name = "FK_CONSULT_SPECIALTY"))
	private Specialty specialty;
	
	@Column(nullable = false, length = 3)
	private String numConsult;
	
	@Column(nullable = false)
	private LocalDateTime consultDate; 
	
	//Cuando es maestro detalle la relación se debe hacer bidireccional, a este lado onetomany y en el otro manytoone
	//está bidireccionalidad puede generar loop infinito que se rompe con dto
	// En el onetomany hay que mapear al nombre en la otra clase(ConsultDetail) el nombre del objeto , es decir, consult en ConsultDetail
	//En onetomany hay que poner el tipo cascade para que lo que le pase al maestro le pase al detalle
	//el cascadetype.all para que también insert, borre, actualice en el detalle
	//orphanremoval es dejar algunos huerfanos, poder eliminar uno de la lista o se pueda dejar por ahí
	//fetch como esta variable no es de bd se puede llenar al comienzo y ya después (con lo cual pudiera no traer datos)
	@OneToMany(mappedBy = "consult",cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	private List<ConsultDetail> details;
}

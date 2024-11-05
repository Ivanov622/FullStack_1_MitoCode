package com.mitocode.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ConsultDTO {
	
	@EqualsAndHashCode.Include
	private Integer idConsult;
	
	@NotNull
	// se puede usar de las 2 formas por el integer idpatient o con el patientdto
	// si se hace con Integer debe llamarse igual que en el model Patient:idPatient
	//private Integer idPatient;
	private PatientDTO patient;
	
	@NotNull
	private MedicDTO medic;
	
	@NotNull
	private SpecialtyDTO specialty;
	
	// número del consultorio
	@NotNull
	private String numConsult;
	
	@NotNull
	private LocalDateTime consultDate;
	
	//acá le dice que esta clase maneja la referencia de ConsultDetailDTO
	//esto evita que se entre en un bucle infinito de referencias entre uno y otro
	//y ordena quién va hacia quien
	@JsonManagedReference
	@NotNull
	private List<ConsultDetailDTO> details;	

}

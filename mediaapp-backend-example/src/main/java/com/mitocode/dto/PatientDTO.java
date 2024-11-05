package com.mitocode.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

// A partir de Java 17 se puede trabajar con record en lugar de clases para DTO
// Acá no se pueden hacer las validaciones que se hacen en las entidades ya que esas son JPA, para hacerlo acá toca
// ingresar una dependencia spring-starter-validation e importar las clases acá que vaya a usar. Entonces ya se ve que se 
//valida el noempty, notnull,size, pattern, min, max, past etc
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PatientDTO {
	
	
	@EqualsAndHashCode.Include
	private Integer idPatient;
	
	@NotEmpty
	@NotNull
	@Size(min = 3, max = 70, message = "{firstname.size}")
	private String firstName;
	
	@NotEmpty
	@NotNull
	@Size(min = 3, max = 70, message = "{lastname.size}")
	private String lastName;
	
	private String dni;	
	
	@Pattern(regexp = "[0-9]+")
	private String phone;
	
	@Email
	private String email;
	
	private String address;
	
}

package com.mitocode.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class MedicDTO {
	
	@EqualsAndHashCode.Include
	private Integer idMedic;
	
	@NotEmpty
	@NotNull
	@Size(min = 3)
	private String primaryName;
	
	@NotEmpty
	@NotNull
	@Size(min = 3)
	private String surname;
	
	//cmp cedula de los medicos en perú
	@NotEmpty
	@NotNull
	@Size(min = 3, max=12)
	private String cmpMedic;
	
	@NotNull
	@NotEmpty
	private String photo;

}

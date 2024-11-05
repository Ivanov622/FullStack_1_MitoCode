package com.mitocode.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SpecialtyDTO {
	
	@EqualsAndHashCode.Include
	private Integer idSpecialty;
	
	@NotNull
	@NotEmpty
	private String nameSpecialty;
	
	@NotNull
	@NotEmpty
	private String descriptionSpecialty;
	
	

}

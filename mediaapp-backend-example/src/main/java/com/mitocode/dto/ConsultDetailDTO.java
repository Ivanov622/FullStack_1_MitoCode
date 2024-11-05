package com.mitocode.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ConsultDetailDTO {
	
	@EqualsAndHashCode.Include
	private Integer idDetail;
	
	
	//esto toca arreglarlo porque hay doble referencia, un bucle entre esta clase y ConsultDTO ya que
	//allá también hay una referncia hacia acá
	//Este hace la referencia al maestro que lo está llamando
	//el jasonbackreference le indica que su referencia está en otro lado, en este caso en ConsultDTO
	//en ConsultDTO está el @JsonManagedReference. Evita llamados infinitos
	@JsonBackReference
	private ConsultDTO consult;
	
	@NotNull
	private String diagnosis;
	
	@NotNull
	private String treatment;
	
	

}

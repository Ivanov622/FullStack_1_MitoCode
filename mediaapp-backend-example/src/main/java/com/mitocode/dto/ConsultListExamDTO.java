package com.mitocode.dto;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultListExamDTO {
	
	
	@NotNull
	private ConsultDTO consult;
	
	@NotNull
	private List<ExamDTO> lstExam;

}
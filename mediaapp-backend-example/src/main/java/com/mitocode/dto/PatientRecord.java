package com.mitocode.dto;

public record PatientRecord(
		Integer idPatient,
		String firstName,
		String lastName,
		String dni,
		String phone,
		String email,
		String address) {

}

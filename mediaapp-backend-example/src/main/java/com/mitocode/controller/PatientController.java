package com.mitocode.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.mitocode.dto.PatientDTO;
import com.mitocode.dto.PatientRecord;
import com.mitocode.model.Patient;
import com.mitocode.service.IPatientService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
//@CrossOrigin(origins = "*") ya no se necesita porque se captura en la clase util.CROS.java que se creó
@RequestMapping("/patients")
public class PatientController {
	
	//Inyecciones de dependencia
	private final IPatientService service; // Acá Spring lo encuentra por estereotipo
	
	@Qualifier("defaultMapper")
	private final ModelMapper modelMapper; //entonces acá inyecta el modelmapper . Acá lo encuentra por bean, spring lo busca

	
	/*
	 * 
	 * Se comenta para usar mapper
	@GetMapping("/{id}")
	public ResponseEntity<Patient> findById(@PathVariable("id") Integer id) {

		Patient obj = service.findById(id);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}
	
	*/
	
	@GetMapping("/{id}")
	public ResponseEntity<PatientDTO> findById(@PathVariable("id") Integer id) {
		//PatientDTO patientDto =  modelMapper.map(service.findById(id), PatientDTO.class); usar nuevos metodos y no repetir
		PatientDTO patientDto =  this.convertToDto(service.findById(id));
		return new ResponseEntity<>(patientDto, HttpStatus.OK);
	}

	
	/*
	 * Hay dependencia con el model directamente, se cambia por eso a DTO
	@GetMapping
	public ResponseEntity<List<Patient>> findAll() {
		List<Patient> list = service.findAll();
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	*/
	
	// quitar dependencia del model con DTO
	// se comenta para usar record para implementar el DTO(es un patrón) en lugar de la clase
	/*
	@GetMapping
	public ResponseEntity<List<PatientDTO>> findAll() {
		List<PatientDTO> list = service.findAll().stream().map(e->{
			PatientDTO patientDto = new PatientDTO();
			patientDto.setIdPatient(e.getIdPatient());
			patientDto.setFirstName(e.getFirstName());
			patientDto.setLastName(e.getLastName());
			patientDto.setDni(e.getDni());
			patientDto.setPhone(e.getPhone());
			patientDto.setEmail(e.getEmail());
			patientDto.setAddress(e.getAddress());	
			
			return patientDto;
		}).collect(Collectors.toList());
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	*/
	
	//Para no depender del model y no usar clase para implementar el dto sino usar record para implementar el dto
	//El record genera un objeto inmutable, no se puede cambiar, no tiene set, en cambio la clase es más flexible
	/*
	@GetMapping
	public ResponseEntity<List<PatientRecord>> findAll() {
		List<PatientRecord> list = service.findAll().stream().map(e->
			new PatientRecord(
			e.getIdPatient(),e.getFirstName(),e.getLastName(),e.getDni(),e.getPhone(),e.getEmail(),e.getAddress()
			)			
		).collect(Collectors.toList());
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	*/
	
	// Manejando el DTO con la libreria ModelMapper
	@GetMapping
	public ResponseEntity<List<PatientDTO>> findAll() {
		//para que cada vez que se llame no cree una instancia de modelmapping se crea la clase modelconfig
//ModelMapper modelMapper = new ModelMapper(); se comenta para hacerlo por inyección de dependencia private final ModelMapper modelMapper
		/*
		 * Se comenta para no estar llamando el modelMapper.map en todo lado y usar los metodos private creado
		List<PatientDTO> list = service.findAll().stream().map(e->
			modelMapper.map(e , PatientDTO.class)).collect(Collectors.toList());
		*/
		
		//acá utiliza métodos de referencia(this::convertToDto) esto métodos solo se pueden usar en lambdas, por eso en los
		//otros metodos no se usa
		List<PatientDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
		return new ResponseEntity<>(list, HttpStatus.OK);
	}
	
	/*
	 * Se comenta para hacerlo con Mapper más abajo
	@PostMapping
	public ResponseEntity<Patient> save(@RequestBody Patient patient) {
		Patient obj = service.save(patient);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdPatient())
				.toUri();
		return ResponseEntity.created(location).build();
	}
	*/
	
	//El @Valid es para que use las validaciones que se pusieron en PatienDTO como ej: @NotNull, @NotEmpy, @Email
	//estas validaciones las usa en el save y update ya que ahí es cuando el cliente envía datos que toca validar
	@PostMapping
	public ResponseEntity<Patient> save(@Valid @RequestBody PatientDTO patientDto) {
		// Acá se hace al revés, que el modelmapper transforme de un Dto a Patient
	//	Patient obj = service.save(modelMapper.map(patientDto, Patient.class)); se comenta para abreviar con los nuevos metodos
		
		Patient obj = service.save(this.convertToEntity(patientDto));
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdPatient())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	/*
	 * Se comenta para hacerlo con Mapper más abajo
	@PutMapping("/{id}")
	public ResponseEntity<Patient> update(@PathVariable Integer id, @RequestBody Patient patient) {

		patient.setIdPatient(id);
		Patient obj = service.update(patient,id);
		return new ResponseEntity<>(obj, HttpStatus.ACCEPTED);
	}
	*/
	
	//El @Valid es para que use las validaciones que se pusieron en PatienDTO como ej: @NotNull, @NotEmpy, @Email
	@PutMapping("/{id}")
	public ResponseEntity<PatientDTO> update(@Valid @PathVariable Integer id, @RequestBody PatientDTO patientDto) {
		
		patientDto.setIdPatient(id);
		Patient obj = service.update(this.convertToEntity(patientDto), id);
		return new ResponseEntity<>(this.convertToDto(obj), HttpStatus.ACCEPTED);
	}

	// Acá no habría que cambiar nada por dto
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
		service.delete(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	/*
	 * Se comenta para hacerlo con Mapper más abajo
	@GetMapping("/hateoas/{id}")
	public EntityModel<Patient> findByIdHateOas(@PathVariable("id") Integer id) {
		EntityModel<Patient> resource = EntityModel.of(service.findById(id));
		
		//linkTo para parte informativa únicamente,no está ejecutando el método
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
		resource.add(link1.withRel("patient-info"));
		
		return resource;
	}
	*/
	
	@GetMapping("/hateoas/{id}")
	public EntityModel<PatientDTO> findByIdHateOas(@PathVariable("id") Integer id) {
		EntityModel<PatientDTO> resource = EntityModel.of(this.convertToDto(service.findById(id)));
		
		//linkTo para parte informativa únicamente,no está ejecutando el método
		WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
		resource.add(link1.withRel("patient-info"));
		
		return resource;
	}
	
	//para no estar repitiendo las mismas sentencias en los métodos
	private PatientDTO convertToDto (Patient patient) {
		return modelMapper.map(patient, PatientDTO.class);
	}
	
	//para no estar repitiendo las mismas sentencias en los métodos
	private Patient convertToEntity (PatientDTO patientDto) {
		return modelMapper.map(patientDto, Patient.class);
	}

}

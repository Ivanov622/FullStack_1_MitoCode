package com.mitocode.controller;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

import com.mitocode.dto.ConsultDTO;
import com.mitocode.dto.ConsultListExamDTO;
import com.mitocode.model.Consult;
import com.mitocode.model.Exam;
import com.mitocode.service.IConsultService;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/consults")
@RequiredArgsConstructor
public class ConsultController {

	//Inyecciones de dependencia
		private final IConsultService service; // Acá Spring lo encuentra por estereotipo
		
		//como Qualifier hace parte de Spring y no de lombok se genera error al hacer la Inyección de deps por constructor
		//por lo anterior se crea el archivo lombok.config en el raiz para adicionarle el Qualifier 
		@Qualifier("defaultMapper")
		private final ModelMapper modelMapper; //entonces acá inyecta el modelmapper . Acá lo encuentra por bean, spring lo busca

	
		@GetMapping("/{id}")
		public ResponseEntity<ConsultDTO> findById(@PathVariable("id") Integer id) {
			//ConsultDTO consultDto =  modelMapper.map(service.findById(id), ConsultDTO.class); usar nuevos metodos y no repetir
			ConsultDTO consultDto =  this.convertToDto(service.findById(id));
			return new ResponseEntity<>(consultDto, HttpStatus.OK);
		}

		
		// Manejando el DTO con la libreria ModelMapper
		@GetMapping
		public ResponseEntity<List<ConsultDTO>> findAll() {
			//para que cada vez que se llame no cree una instancia de modelmapping se crea la clase modelconfig
			//ModelMapper modelMapper = new ModelMapper(); se comenta para hacerlo por inyección de 
			//dependencia private final ModelMapper modelMapper
			/*
			 * Se comenta para no estar llamando el modelMapper.map en todo lado y usar los metodos private creado
			List<ConsultDTO> list = service.findAll().stream().map(e->
				modelMapper.map(e , ConsultDTO.class)).collect(Collectors.toList());
			*/
			
			//acá utiliza métodos de referencia(this::convertToDto) esto métodos solo se pueden usar en lambdas, por eso en los
			//otros metodos no se usa
			List<ConsultDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
			return new ResponseEntity<>(list, HttpStatus.OK);
		}
		
		/*
		 * Se comenta para hacerlo con Mapper más abajo
		@PostMapping
		public ResponseEntity<Consult> save(@RequestBody Consult consult) {
			Consult obj = service.save(consult);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdConsult())
					.toUri();
			return ResponseEntity.created(location).build();
		}
		*/
		
		//El @Valid es para que use las validaciones que se pusieron en PatienDTO como ej: @NotNull, @NotEmpy, @Email
		//estas validaciones las usa en el save y update ya que ahí es cuando el cliente envía datos que toca validar
		//antes se hizo con ConsultDTO, pero por la lista de exámenes se requiere con ConsultListExamDTO
		/*
		@PostMapping
		public ResponseEntity<Consult> save(@Valid @RequestBody ConsultDTO consultDto) {
			// Acá se hace al revés, que el modelmapper transforme de un Dto a Consult
		//	Consult obj = service.save(modelMapper.map(consultDto, Consult.class)); se comenta para abreviar con los nuevos metodos
			
			Consult obj = service.save(this.convertToEntity(consultDto));
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdConsult())
					.toUri();
			return ResponseEntity.created(location).build();
		}
		*/
		
		@PostMapping
		public ResponseEntity<Consult> save(@Valid @RequestBody ConsultListExamDTO consultListExamDTO) {	
			
			Consult cons = this.convertToEntity(consultListExamDTO.getConsult());
			
			// 2 maneras de sacar la lista de exámenes
	//		List<Exam> exams = consultListExamDTO.getLstExam().stream().map(e-> modelMapper.map(e, Exam.class))
	//				.collect(Collectors.toList());						
			List<Exam> exams = modelMapper.map(consultListExamDTO.getLstExam(), new TypeToken<List<Exam>>(){}.getType());			
			
			Consult obj = service.saveTransactional(cons, exams);

			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdConsult())
					.toUri();
			return ResponseEntity.created(location).build();
		}

		/*
		 * Se comenta para hacerlo con Mapper más abajo
		@PutMapping("/{id}")
		public ResponseEntity<Consult> update(@PathVariable Integer id, @RequestBody Consult consult) {

			consult.setIdConsult(id);
			Consult obj = service.update(consult,id);
			return new ResponseEntity<>(obj, HttpStatus.ACCEPTED);
		}
		*/
		
		//El @Valid es para que use las validaciones que se pusieron en PatienDTO como ej: @NotNull, @NotEmpy, @Email
		@PutMapping("/{id}")
		public ResponseEntity<ConsultDTO> update(@Valid @PathVariable Integer id, @RequestBody ConsultDTO consultDto) {
			
			consultDto.setIdConsult(id);
			Consult obj = service.update(this.convertToEntity(consultDto), id);
			return new ResponseEntity<>(this.convertToDto(obj), HttpStatus.ACCEPTED);
		}

		// Acá no habría que cambiar nada por dto
		@DeleteMapping("/{id}")
		public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
			service.delete(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		
		@GetMapping("/hateoas/{id}")
		public EntityModel<ConsultDTO> findByIdHateOas(@PathVariable("id") Integer id) {
			EntityModel<ConsultDTO> resource = EntityModel.of(this.convertToDto(service.findById(id)));
			
			//linkTo para parte informativa únicamente,no está ejecutando el método
			WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
			resource.add(link1.withRel("consult-info"));
			
			return resource;
		}
		
		//para no estar repitiendo las mismas sentencias en los métodos
		private ConsultDTO convertToDto (Consult consult) {
			return modelMapper.map(consult, ConsultDTO.class);
		}
		
		//para no estar repitiendo las mismas sentencias en los métodos
		private Consult convertToEntity (ConsultDTO consultDto) {
			return modelMapper.map(consultDto, Consult.class);
		}

}


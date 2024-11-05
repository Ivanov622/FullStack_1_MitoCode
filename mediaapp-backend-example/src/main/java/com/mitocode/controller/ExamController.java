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

import com.mitocode.dto.ExamDTO;
import com.mitocode.model.Exam;
import com.mitocode.service.IExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/exams")
@RequiredArgsConstructor
public class ExamController {

	//Inyecciones de dependencia
		private final IExamService service; // Acá Spring lo encuentra por estereotipo
		
		//como Qualifier hace parte de Spring y no de lombok se genera error al hacer la Inyección de deps por constructor
		//por lo anterior se crea el archivo lombok.config en el raiz para adicionarle el Qualifier 
		@Qualifier("defaultMapper")
		private final ModelMapper modelMapper; //entonces acá inyecta el modelmapper . Acá lo encuentra por bean, spring lo busca

	
		@GetMapping("/{id}")
		public ResponseEntity<ExamDTO> findById(@PathVariable("id") Integer id) {

			ExamDTO examDto =  this.convertToDto(service.findById(id));
			return new ResponseEntity<>(examDto, HttpStatus.OK);
		}

		
		// Manejando el DTO con la libreria ModelMapper
		@GetMapping
		public ResponseEntity<List<ExamDTO>> findAll() {
			//para que cada vez que se llame no cree una instancia de modelmapping se crea la clase modelconfig
	//ModelMapper modelMapper = new ModelMapper(); se comenta para hacerlo por inyección de dependencia private final ModelMapper modelMapper
			/*
			 * Se comenta para no estar llamando el modelMapper.map en todo lado y usar los metodos private creado
			List<ExamDTO> list = service.findAll().stream().map(e->
				modelMapper.map(e , ExamDTO.class)).collect(Collectors.toList());
			*/
			
			//acá utiliza métodos de referencia(this::convertToDto) esto métodos solo se pueden usar en lambdas, por eso en los
			//otros metodos no se usa
			List<ExamDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
			return new ResponseEntity<>(list, HttpStatus.OK);
		}
		
		/*
		 * Se comenta para hacerlo con Mapper más abajo
		@PostMapping
		public ResponseEntity<Exam> save(@RequestBody Exam exam) {
			Exam obj = service.save(exam);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdExam())
					.toUri();
			return ResponseEntity.created(location).build();
		}
		*/
		
		//El @Valid es para que use las validaciones que se pusieron en PatienDTO como ej: @NotNull, @NotEmpy, @Email
		//estas validaciones las usa en el save y update ya que ahí es cuando el cliente envía datos que toca validar
		@PostMapping
		public ResponseEntity<Exam> save(@Valid @RequestBody ExamDTO examDto) {
			// Acá se hace al revés, que el modelmapper transforme de un Dto a Exam
		//	Exam obj = service.save(modelMapper.map(examDto, Exam.class)); se comenta para abreviar con los nuevos metodos
			
			Exam obj = service.save(this.convertToEntity(examDto));
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdExam())
					.toUri();
			return ResponseEntity.created(location).build();
		}

		/*
		 * Se comenta para hacerlo con Mapper más abajo
		@PutMapping("/{id}")
		public ResponseEntity<Exam> update(@PathVariable Integer id, @RequestBody Exam exam) {

			exam.setIdExam(id);
			Exam obj = service.update(exam,id);
			return new ResponseEntity<>(obj, HttpStatus.ACCEPTED);
		}
		*/
		
		//El @Valid es para que use las validaciones que se pusieron en PatienDTO como ej: @NotNull, @NotEmpy, @Email
		@PutMapping("/{id}")
		public ResponseEntity<ExamDTO> update(@Valid @PathVariable Integer id, @RequestBody ExamDTO examDto) {
			
			examDto.setIdExam(id);
			Exam obj = service.update(this.convertToEntity(examDto), id);
			return new ResponseEntity<>(this.convertToDto(obj), HttpStatus.ACCEPTED);
		}

		// Acá no habría que cambiar nada por dto
		@DeleteMapping("/{id}")
		public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
			service.delete(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		
		@GetMapping("/hateoas/{id}")
		public EntityModel<ExamDTO> findByIdHateOas(@PathVariable("id") Integer id) {
			EntityModel<ExamDTO> resource = EntityModel.of(this.convertToDto(service.findById(id)));
			
			//linkTo para parte informativa únicamente,no está ejecutando el método
			WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
			resource.add(link1.withRel("exam-info"));
			
			return resource;
		}
		
		//para no estar repitiendo las mismas sentencias en los métodos
		private ExamDTO convertToDto (Exam exam) {
			return modelMapper.map(exam, ExamDTO.class);
		}
		
		//para no estar repitiendo las mismas sentencias en los métodos
		private Exam convertToEntity (ExamDTO examDto) {
			return modelMapper.map(examDto, Exam.class);
		}

}


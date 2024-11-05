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

import com.mitocode.dto.SpecialtyDTO;
import com.mitocode.model.Specialty;
import com.mitocode.service.ISpecialtyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/specialties")
@RequiredArgsConstructor
public class SpecialtyController {

	//Inyecciones de dependencia
		private final ISpecialtyService service; // Acá Spring lo encuentra por estereotipo
		
		//como Qualifier hace parte de Spring y no de lombok se genera error al hacer la Inyección de deps por constructor
		//por lo anterior se crea el archivo lombok.config en el raiz para adicionarle el Qualifier 
		@Qualifier("defaultMapper")
		private final ModelMapper modelMapper; //entonces acá inyecta el modelmapper . Acá lo encuentra por bean, spring lo busca

	
		@GetMapping("/{id}")
		public ResponseEntity<SpecialtyDTO> findById(@PathVariable("id") Integer id) {

			SpecialtyDTO specialtyDto =  this.convertToDto(service.findById(id));
			return new ResponseEntity<>(specialtyDto, HttpStatus.OK);
		}

		
		// Manejando el DTO con la libreria ModelMapper
		@GetMapping
		public ResponseEntity<List<SpecialtyDTO>> findAll() {
			//para que cada vez que se llame no cree una instancia de modelmapping se crea la clase modelconfig
	//ModelMapper modelMapper = new ModelMapper(); se comenta para hacerlo por inyección de dependencia private final ModelMapper modelMapper
			/*
			 * Se comenta para no estar llamando el modelMapper.map en todo lado y usar los metodos private creado
			List<SpecialtyDTO> list = service.findAll().stream().map(e->
				modelMapper.map(e , SpecialtyDTO.class)).collect(Collectors.toList());
			*/
			
			//acá utiliza métodos de referencia(this::convertToDto) esto métodos solo se pueden usar en lambdas, por eso en los
			//otros metodos no se usa
			List<SpecialtyDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
			return new ResponseEntity<>(list, HttpStatus.OK);
		}
		
		/*
		 * Se comenta para hacerlo con Mapper más abajo
		@PostMapping
		public ResponseEntity<Specialty> save(@RequestBody Specialty specialty) {
			Specialty obj = service.save(specialty);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdSpecialty())
					.toUri();
			return ResponseEntity.created(location).build();
		}
		*/
		
		//El @Valid es para que use las validaciones que se pusieron en PatienDTO como ej: @NotNull, @NotEmpy, @Email
		//estas validaciones las usa en el save y update ya que ahí es cuando el cliente envía datos que toca validar
		@PostMapping
		public ResponseEntity<Specialty> save(@Valid @RequestBody SpecialtyDTO specialtyDto) {
			// Acá se hace al revés, que el modelmapper transforme de un Dto a Specialty
		//	Specialty obj = service.save(modelMapper.map(specialtyDto, Specialty.class)); se comenta para abreviar con los nuevos metodos
			
			Specialty obj = service.save(this.convertToEntity(specialtyDto));
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdSpecialty())
					.toUri();
			return ResponseEntity.created(location).build();
		}

		/*
		 * Se comenta para hacerlo con Mapper más abajo
		@PutMapping("/{id}")
		public ResponseEntity<Specialty> update(@PathVariable Integer id, @RequestBody Specialty specialty) {

			specialty.setIdSpecialty(id);
			Specialty obj = service.update(specialty,id);
			return new ResponseEntity<>(obj, HttpStatus.ACCEPTED);
		}
		*/
		
		//El @Valid es para que use las validaciones que se pusieron en PatienDTO como ej: @NotNull, @NotEmpy, @Email
		@PutMapping("/{id}")
		public ResponseEntity<SpecialtyDTO> update(@Valid @PathVariable Integer id, @RequestBody SpecialtyDTO specialtyDto) {
			
			specialtyDto.setIdSpecialty(id);
			Specialty obj = service.update(this.convertToEntity(specialtyDto), id);
			return new ResponseEntity<>(this.convertToDto(obj), HttpStatus.ACCEPTED);
		}

		// Acá no habría que cambiar nada por dto
		@DeleteMapping("/{id}")
		public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
			service.delete(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		
		@GetMapping("/hateoas/{id}")
		public EntityModel<SpecialtyDTO> findByIdHateOas(@PathVariable("id") Integer id) {
			EntityModel<SpecialtyDTO> resource = EntityModel.of(this.convertToDto(service.findById(id)));
			
			//linkTo para parte informativa únicamente,no está ejecutando el método
			WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
			resource.add(link1.withRel("specialty-info"));
			
			return resource;
		}
		
		//para no estar repitiendo las mismas sentencias en los métodos
		private SpecialtyDTO convertToDto (Specialty specialty) {
			return modelMapper.map(specialty, SpecialtyDTO.class);
		}
		
		//para no estar repitiendo las mismas sentencias en los métodos
		private Specialty convertToEntity (SpecialtyDTO specialtyDto) {
			return modelMapper.map(specialtyDto, Specialty.class);
		}

}


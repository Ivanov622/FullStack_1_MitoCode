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

import com.mitocode.dto.MedicDTO;
import com.mitocode.model.Medic;
import com.mitocode.service.IMedicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/medics")
@RequiredArgsConstructor
public class MedicController {

	//Inyecciones de dependencia
		private final IMedicService service; // Acá Spring lo encuentra por estereotipo
		
		//como Qualifier hace parte de Spring y no de lombok se genera error al hacer la Inyección de deps por constructor
		//por lo anterior se crea el archivo lombok.config en el raiz para adicionarle el Qualifier 
		@Qualifier("medicMapper")
		private final ModelMapper modelMapper; //entonces acá inyecta el modelmapper . Acá lo encuentra por bean, spring lo busca

	
		@GetMapping("/{id}")
		public ResponseEntity<MedicDTO> findById(@PathVariable("id") Integer id) {
			//MedicDTO medicDto =  modelMapper.map(service.findById(id), MedicDTO.class); usar nuevos metodos y no repetir
			MedicDTO medicDto =  this.convertToDto(service.findById(id));
			return new ResponseEntity<>(medicDto, HttpStatus.OK);
		}

		
		// Manejando el DTO con la libreria ModelMapper
		@GetMapping
		public ResponseEntity<List<MedicDTO>> findAll() {
			//para que cada vez que se llame no cree una instancia de modelmapping se crea la clase modelconfig
	//ModelMapper modelMapper = new ModelMapper(); se comenta para hacerlo por inyección de dependencia private final ModelMapper modelMapper
			/*
			 * Se comenta para no estar llamando el modelMapper.map en todo lado y usar los metodos private creado
			List<MedicDTO> list = service.findAll().stream().map(e->
				modelMapper.map(e , MedicDTO.class)).collect(Collectors.toList());
			*/
			
			//acá utiliza métodos de referencia(this::convertToDto) esto métodos solo se pueden usar en lambdas, por eso en los
			//otros metodos no se usa
			List<MedicDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
			return new ResponseEntity<>(list, HttpStatus.OK);
		}
		
		/*
		 * Se comenta para hacerlo con Mapper más abajo
		@PostMapping
		public ResponseEntity<Medic> save(@RequestBody Medic medic) {
			Medic obj = service.save(medic);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdMedic())
					.toUri();
			return ResponseEntity.created(location).build();
		}
		*/
		
		//El @Valid es para que use las validaciones que se pusieron en PatienDTO como ej: @NotNull, @NotEmpy, @Email
		//estas validaciones las usa en el save y update ya que ahí es cuando el cliente envía datos que toca validar
		@PostMapping
		public ResponseEntity<Medic> save(@Valid @RequestBody MedicDTO medicDto) {
			// Acá se hace al revés, que el modelmapper transforme de un Dto a Medic
		//	Medic obj = service.save(modelMapper.map(medicDto, Medic.class)); se comenta para abreviar con los nuevos metodos
			
			Medic obj = service.save(this.convertToEntity(medicDto));
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdMedic())
					.toUri();
			return ResponseEntity.created(location).build();
		}

		/*
		 * Se comenta para hacerlo con Mapper más abajo
		@PutMapping("/{id}")
		public ResponseEntity<Medic> update(@PathVariable Integer id, @RequestBody Medic medic) {

			medic.setIdMedic(id);
			Medic obj = service.update(medic,id);
			return new ResponseEntity<>(obj, HttpStatus.ACCEPTED);
		}
		*/
		
		//El @Valid es para que use las validaciones que se pusieron en PatienDTO como ej: @NotNull, @NotEmpy, @Email
		@PutMapping("/{id}")
		public ResponseEntity<MedicDTO> update(@Valid @PathVariable Integer id, @RequestBody MedicDTO medicDto) {
			
			medicDto.setIdMedic(id);
			Medic obj = service.update(this.convertToEntity(medicDto), id);
			return new ResponseEntity<>(this.convertToDto(obj), HttpStatus.ACCEPTED);
		}

		// Acá no habría que cambiar nada por dto
		@DeleteMapping("/{id}")
		public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
			service.delete(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		
		@GetMapping("/hateoas/{id}")
		public EntityModel<MedicDTO> findByIdHateOas(@PathVariable("id") Integer id) {
			EntityModel<MedicDTO> resource = EntityModel.of(this.convertToDto(service.findById(id)));
			
			//linkTo para parte informativa únicamente,no está ejecutando el método
			WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
			resource.add(link1.withRel("medic-info"));
			
			return resource;
		}
		
		//para no estar repitiendo las mismas sentencias en los métodos
		private MedicDTO convertToDto (Medic medic) {
			return modelMapper.map(medic, MedicDTO.class);
		}
		
		//para no estar repitiendo las mismas sentencias en los métodos
		private Medic convertToEntity (MedicDTO medicDto) {
			return modelMapper.map(medicDto, Medic.class);
		}

}


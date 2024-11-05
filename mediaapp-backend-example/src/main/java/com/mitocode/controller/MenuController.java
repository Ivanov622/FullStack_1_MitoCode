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

import com.mitocode.dto.MenuDTO;
import com.mitocode.model.Menu;
import com.mitocode.service.IMenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/menus")
@RequiredArgsConstructor
public class MenuController {

	//Inyecciones de dependencia
		private final IMenuService service; // Acá Spring lo encuentra por estereotipo
		
		//como Qualifier hace parte de Spring y no de lombok se genera error al hacer la Inyección de deps por constructor
		//por lo anterior se crea el archivo lombok.config en el raiz para adicionarle el Qualifier 
		@Qualifier("defaultMapper")
		private final ModelMapper modelMapper; //entonces acá inyecta el modelmapper . Acá lo encuentra por bean, spring lo busca

	
		@GetMapping("/{id}")
		public ResponseEntity<MenuDTO> findById(@PathVariable("id") Integer id) {
			//MenuDTO menuDto =  modelMapper.map(service.findById(id), MenuDTO.class); usar nuevos metodos y no repetir
			MenuDTO menuDto =  this.convertToDto(service.findById(id));
			return new ResponseEntity<>(menuDto, HttpStatus.OK);
		}

		
		// Manejando el DTO con la libreria ModelMapper
		@GetMapping
		public ResponseEntity<List<MenuDTO>> findAll() {
			//para que cada vez que se llame no cree una instancia de modelmapping se crea la clase modelconfig
	//ModelMapper modelMapper = new ModelMapper(); se comenta para hacerlo por inyección de dependencia private final ModelMapper modelMapper
			/*
			 * Se comenta para no estar llamando el modelMapper.map en todo lado y usar los metodos private creado
			List<MenuDTO> list = service.findAll().stream().map(e->
				modelMapper.map(e , MenuDTO.class)).collect(Collectors.toList());
			*/
			
			//acá utiliza métodos de referencia(this::convertToDto) esto métodos solo se pueden usar en lambdas, por eso en los
			//otros metodos no se usa
			List<MenuDTO> list = service.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
			return new ResponseEntity<>(list, HttpStatus.OK);
		}
		
		/*
		 * Se comenta para hacerlo con Mapper más abajo
		@PostMapping
		public ResponseEntity<Menu> save(@RequestBody Menu menu) {
			Menu obj = service.save(menu);
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdMenu())
					.toUri();
			return ResponseEntity.created(location).build();
		}
		*/
		
		//El @Valid es para que use las validaciones que se pusieron en PatienDTO como ej: @NotNull, @NotEmpy, @Email
		//estas validaciones las usa en el save y update ya que ahí es cuando el cliente envía datos que toca validar
		@PostMapping
		public ResponseEntity<Menu> save(@Valid @RequestBody MenuDTO menuDto) {
			// Acá se hace al revés, que el modelmapper transforme de un Dto a Menu
		//	Menu obj = service.save(modelMapper.map(menuDto, Menu.class)); se comenta para abreviar con los nuevos metodos
			
			Menu obj = service.save(this.convertToEntity(menuDto));
			URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getIdMenu())
					.toUri();
			return ResponseEntity.created(location).build();
		}

		/*
		 * Se comenta para hacerlo con Mapper más abajo
		@PutMapping("/{id}")
		public ResponseEntity<Menu> update(@PathVariable Integer id, @RequestBody Menu menu) {

			menu.setIdMenu(id);
			Menu obj = service.update(menu,id);
			return new ResponseEntity<>(obj, HttpStatus.ACCEPTED);
		}
		*/
		
		//El @Valid es para que use las validaciones que se pusieron en PatienDTO como ej: @NotNull, @NotEmpy, @Email
		@PutMapping("/{id}")
		public ResponseEntity<MenuDTO> update(@Valid @PathVariable Integer id, @RequestBody MenuDTO menuDto) {
			
			menuDto.setIdMenu(id);
			Menu obj = service.update(this.convertToEntity(menuDto), id);
			return new ResponseEntity<>(this.convertToDto(obj), HttpStatus.ACCEPTED);
		}

		// Acá no habría que cambiar nada por dto
		@DeleteMapping("/{id}")
		public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
			service.delete(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		
		@GetMapping("/hateoas/{id}")
		public EntityModel<MenuDTO> findByIdHateOas(@PathVariable("id") Integer id) {
			EntityModel<MenuDTO> resource = EntityModel.of(this.convertToDto(service.findById(id)));
			
			//linkTo para parte informativa únicamente,no está ejecutando el método
			WebMvcLinkBuilder link1 = linkTo(methodOn(this.getClass()).findById(id));
			resource.add(link1.withRel("menu-info"));
			
			return resource;
		}
		
		//para no estar repitiendo las mismas sentencias en los métodos
		private MenuDTO convertToDto (Menu menu) {
			return modelMapper.map(menu, MenuDTO.class);
		}
		
		//para no estar repitiendo las mismas sentencias en los métodos
		private Menu convertToEntity (MenuDTO menuDto) {
			return modelMapper.map(menuDto, Menu.class);
		}

}


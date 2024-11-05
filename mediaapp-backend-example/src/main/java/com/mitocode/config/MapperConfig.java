package com.mitocode.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.modelmapper.TypeMap;

import com.mitocode.dto.MedicDTO;
import com.mitocode.model.Medic;

// al ser marcada como @configuration es casi 100% seguro que va a haber la anotación @Bean internamente
// se crea esta clase para manejar la instancia de ModelMapper en el controlador y no usar new ModelMapper
@Configuration
public class MapperConfig {
	
	// este se deja por defecto cuando los campos del dto coinciden con los del model
	// meto el primary porque no sirvió solo con el nombre del bean y generaba error
	@Bean("defaultMapper")
	@Primary
	public ModelMapper modelMapper() {
		//esta instancia por defecto es singleton
		// cada vez que quiera modelmapper se hace por acá
		return new ModelMapper();
	}
	
	//creamos este ya que los campos del dto MedicDto no coinciden con los model Medic
	@Bean("medicMapper")
	public ModelMapper medicMapper() {
		
		ModelMapper mapper = new ModelMapper();
		
		//Esta parte es para ESCRITURA, cuando grabo
		
		TypeMap<MedicDTO, Medic> typeMap1 = mapper.createTypeMap(MedicDTO.class, Medic.class);		
		// Acá manejo las 2 noticiones: con el lambda y con método de referenciación
		// el método de referenciación se puede hacer porque el source se usa así mismo como en los 2 primeros
		typeMap1.addMapping(source -> source.getPrimaryName(), (dest, v) -> dest.setFirstName((String) v));
		typeMap1.addMapping(source -> source.getSurname(), (dest, v) -> dest.setLastName((String) v));
		typeMap1.addMapping(MedicDTO::getCmpMedic, (dest, v) -> dest.setCmp((String) v));
		typeMap1.addMapping(MedicDTO::getPhoto, (dest, v) -> dest.setPhotoUrl((String) v));
		
		
		//Esta parte es para LECTURA, cuando consulto, cambia el sentido
		
		TypeMap<Medic, MedicDTO> typeMap2 = mapper.createTypeMap(Medic.class, MedicDTO.class);		
		// Acá manejo las 2 noticiones: con el lambda y con método de referenciación
		// el método de referenciación se puede hacer porque el source se usa así mismo como en los 2 primeros
		typeMap2.addMapping(source -> source.getFirstName(), (dest, v) -> dest.setPrimaryName((String) v));
		typeMap2.addMapping(source -> source.getLastName(), (dest, v) -> dest.setSurname((String) v));
		typeMap2.addMapping(Medic::getCmp, (dest, v) -> dest.setCmpMedic((String) v));
		typeMap2.addMapping(Medic::getPhotoUrl, (dest, v) -> dest.setPhoto((String) v));

		return mapper;
	}

}

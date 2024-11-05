package com.mitocode.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

//con esta(@NoRepositoryBean) no se genera el error creating bean with name 'IGenericRepo' defined in
//es decir para que el parámetro T no genere error
//sin esto se genera error No managed type:java.lang.object
//como aca no hay un @Entity, el object no hay un @Entity, en el T se genera el error para 
//que no trate de generar un bean porque en este momento no se sabe de qué va a ser la T
@NoRepositoryBean 
public interface IGenericRepo<T, ID> extends JpaRepository<T, ID>{
	
	

}

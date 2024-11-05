package com.mitocode.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true) //si voy a comparar objetos que lo haga por el marcado(aquí id) y no por referencia
@Entity
@Table(name = "user_data") //le cambia el nombre ya que User es una palabra reservada en postgress
public class User {
	
	//este idUser también lo va a poner como valor en la tabla que crea automática user_role
	@Id
	@EqualsAndHashCode.Include
	private Integer idUser; // no lo autoincrementa para que todos en la clase manejemos los mismos id
	
	@Column(nullable = false, length = 60, unique = true) //unique para que no metan un username repetido
	private String userName;
	
	@Column(nullable = false, length = 60) // le pone 60 ya que como la cifra,encripta, entonces pueden ser muchoas caracteres
	private String password;
	
	@Column(nullable = false)
	private boolean enabled;
	
	//Acá tenemos muchos a muchos entre User y Role
	//aca estamos usando la otra forma de manejar relaciones manyTomany, mas automática. 
	//En ConsultaExam y ConsultExamPK se maneja manual
	//esta manera es más limitada, en la otra es más fácil la flexibilidad, más ordenada y menos confusa
	//si hay más columnas se llena de más anotaciones
	// Acá es automática y con la notación @JoinTable el crea la tabla intermedia por nosotros
	// Los joincolumns son las columnas que él va a crear en esa tabla
	//el valor idUser lo mete acá también, es el referencedColumnName
	//el inverseJoinColumns es el de la otra tabla(Role) el idRole(el nombre del campo en la otra tabla)
	//el eager es para que al momento de iniciar sesión le carguen los roles y no lleguen vacíos y al ser pocos regs es fácil
	//Este ManyToMany pudiera ir en cualquiera de los 2 lados de la relacíon, se hizo acá, pero se pudo hacer en Role
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "user_role",
				joinColumns = @JoinColumn(name = "id_user", referencedColumnName = "idUser" ),
				inverseJoinColumns = @JoinColumn(name = "id_role", referencedColumnName = "idRole"))
	private List<Role> roles;
	

}

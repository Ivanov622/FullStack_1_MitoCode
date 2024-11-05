package com.mitocode.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Menu {
	
	@Id
	@EqualsAndHashCode.Include
	private Integer idMenu;
	
	@Column(nullable = false, length = 20)
	private String icon;
	
	@Column(nullable = false, length = 20)
	private String name;
	
	@Column(nullable = false, length = 30)
	private String url;
	
	//mejor hacer lo de llaves foráneas con el otro método como en ConsultExamPK
	// Ver explicación en la clase User
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "menu_role",
				joinColumns = @JoinColumn(name = "id_menu", referencedColumnName = "idMenu" ), //llave foránea, esta está acá en esta clase
				inverseJoinColumns = @JoinColumn(name = "id_role", referencedColumnName = "idRole"))//llave foránea, esta no está acá
	private List<Role> roles;

}

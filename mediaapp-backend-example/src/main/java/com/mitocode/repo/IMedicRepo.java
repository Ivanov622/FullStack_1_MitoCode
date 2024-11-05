package com.mitocode.repo;



import com.mitocode.model.Medic;


//varias interfaces IxxxxRepo heredarían de JPARepository. Esto haría no eficiente un cambio por ejemplo a una bd NoSql
//tocaría entrar una por una a cambiar el extends para acomodarlos
//por ello se crea una interfaz central, IGenericRepo, que hereda de JpaRepositury y las demás heredan de ella
public interface IMedicRepo extends IGenericRepo<Medic, Integer> {
	

}

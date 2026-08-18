package com.epiis.app.dto;

import java.util.List;

import com.epiis.app.generic.DtoGeneric;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoCategory extends DtoGeneric {
	

	private String idCategory;
	private String name;
	private String type;
	private String idUser;
	private List<DtoTransaction> listTransacion;
	private List<DtoBudget> listBudget;
	
	
	

}

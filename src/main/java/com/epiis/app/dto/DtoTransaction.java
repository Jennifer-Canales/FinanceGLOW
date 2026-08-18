package com.epiis.app.dto;


import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.epiis.app.generic.DtoGeneric;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoTransaction extends DtoGeneric {
	

	private String idTransaction;
	private Double amount;
	private String description;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateTransaction;
	
	private String nameCategory;

	
	private String idCategory;
	private String idUser;
	
	

}

package com.epiis.app.dto;

import java.util.List;

import com.epiis.app.generic.DtoGeneric;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoUser extends DtoGeneric{
	

	private String idUser;
	private String firstName;
	private String password;
    private Boolean isActive; 
    private List<DtoCategory> listCategory;
    private List<DtoTransaction> listTransaction;
    private List<DtoBudget> listBudget;
}

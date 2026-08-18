package com.epiis.app.dto;



import com.epiis.app.generic.DtoGeneric;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoBudget extends DtoGeneric{


    private String idBudget; 
    private Double limitAmount;
    private Integer month; 
    private Integer year;
    private String nameCategory;
    private Double spent;
    private Double remaining;
    private Double percentage;
    
    private String idUser; 
    private String idCategory; 
}

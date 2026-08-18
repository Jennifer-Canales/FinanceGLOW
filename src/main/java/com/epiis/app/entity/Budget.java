	package com.epiis.app.entity;


import com.epiis.app.generic.EntityGeneric;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="tbudget")
public class Budget extends EntityGeneric {

    @Id
    @Column(name="idBudget")
    private String idBudget; 

    @ManyToOne
	@JoinColumn(name="idUser")
	private User parentUser;
    
    @ManyToOne
    @JoinColumn(name="idCategory")
    private Category parentCategory;
    

    @Column(name="limitAmount")
    private Double limitAmount;

    @Column(name="month")
    private Integer month; 

    @Column(name="year")
    private Integer year; 
    
 
    
    


}

package com.epiis.app.entity;

import java.util.List;

import com.epiis.app.generic.EntityGeneric;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name ="tuser")
public class User extends EntityGeneric {
	@Id
	@Column(name ="idUser")
	private String idUser;
	
	
	@Column(name="firstName")
	private String firstName;
	
	@Column(name="password")
	private String password;
	
	@Column(name="isActive")
    private Boolean isActive;   
	
	
	@OneToMany(mappedBy = "parentUser")
	private List<Category> listCategory;
	
	@OneToMany(mappedBy = "parentUser")
	private List<Transaction> listTransaction;
	
	@OneToMany(mappedBy = "parentUser")
	private List<Budget> listBudget;
	
	
}

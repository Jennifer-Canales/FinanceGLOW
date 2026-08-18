package com.epiis.app.entity;



import java.util.List;

import com.epiis.app.generic.EntityGeneric;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Setter
@Table(name="tcategory")
public class Category extends EntityGeneric{
	@Id
	@Column(name="idCategory")
	private String idCategory;
	
	@ManyToOne
	@JoinColumn(name="idUser")
	private User parentUser;
	
	@Column(name="name")
	private String name;
	
	@Column(name="type")
	private String type;
	
	@OneToMany(mappedBy = "parentCategory")
	private List<Transaction> listTransacion;
	
	@OneToMany(mappedBy = "parentCategory")
	private List<Budget> listBudget;
	
	
	


}

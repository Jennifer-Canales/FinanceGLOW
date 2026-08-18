package com.epiis.app.entity;





import java.sql.Date;

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
@Table(name="ttransaction")
public class Transaction  extends EntityGeneric{
	
	@Id
	@Column(name="idTransaction")
	private String idTransaction;
	
	@ManyToOne
	@JoinColumn(name ="idUser")
	private User parentUser;
	
	@ManyToOne
	@JoinColumn(name="idCategory")
	private Category parentCategory;
	
	
	@Column(name="amount")
	private Double amount;
	
	@Column(name ="description")
	private String description;
	
	@Column(name="dateTransaction")
	private Date dateTransaction;

}

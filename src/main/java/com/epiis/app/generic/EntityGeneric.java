package com.epiis.app.generic;



import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class EntityGeneric {
    
	@Column(name = "createdAt")
	private Date createdAt;
	
	@Column(name ="updatedAt")
	private Date updatedAt;
}

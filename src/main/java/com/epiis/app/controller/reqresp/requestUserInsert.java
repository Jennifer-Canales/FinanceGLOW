package com.epiis.app.controller.reqresp;

import com.epiis.app.dto.DtoUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class requestUserInsert {
	
	@Getter
	@Setter
	
	public class Dto{
		private DtoUser user;
	}
	
	private Dto dto=new Dto();

}

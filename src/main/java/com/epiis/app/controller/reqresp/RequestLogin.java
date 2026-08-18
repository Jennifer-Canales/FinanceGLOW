package com.epiis.app.controller.reqresp;
import com.epiis.app.dto.DtoUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestLogin {
	@Getter
	@Setter
	public class Dto {
		private DtoUser dtoUser;
	}
	
	private Dto dto = new Dto();
}

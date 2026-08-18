package com.epiis.app.controller.reqresp;

import com.epiis.app.dto.DtoBudget;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class requestBudgetInsert {
	
	@Getter
	@Setter
	
	public class Dto{
		private DtoBudget budget;
	}
	
	private Dto dto=new Dto();

}

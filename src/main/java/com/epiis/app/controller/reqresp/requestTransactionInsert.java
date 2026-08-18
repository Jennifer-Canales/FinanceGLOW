package com.epiis.app.controller.reqresp;

import com.epiis.app.dto.DtoTransaction;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class requestTransactionInsert {
	@Getter
	@Setter
	
	public  class Dto{
		private DtoTransaction transaction;
	}
	
	private Dto dto=new Dto();
}

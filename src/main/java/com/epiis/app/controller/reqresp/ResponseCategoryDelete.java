package com.epiis.app.controller.reqresp;

import com.epiis.app.generic.ResponseGeneric;

public class ResponseCategoryDelete extends ResponseGeneric {
	 private long totalTransactions;

	 public ResponseCategoryDelete() {
	    super();
	 }

	 public long getTotalTransactions() {
	    return totalTransactions;
	  }

	 public void setTotalTransactions(long totalTransactions) {
	    this.totalTransactions = totalTransactions;
	 }
}

package com.epiis.app.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.epiis.app.business.BudgetBusiness;
import com.epiis.app.controller.reqresp.ResponseBudgetDelete;
import com.epiis.app.controller.reqresp.ResponseBudgetInsert;
import com.epiis.app.controller.reqresp.requestBudgetInsert;
import com.epiis.app.dto.DtoBudget;

@Controller

@RequestMapping("budget")
public class BudgetController {
	
	@Autowired
	private BudgetBusiness budgetBusiness;
	
	
	@PostMapping(path = "insert", consumes = "multipart/form-data")
	public ResponseEntity<ResponseBudgetInsert> insert(@ModelAttribute requestBudgetInsert request, Authentication authentication) {
		String idUser=authentication.getName();
		System.out.println("AUTH NAME de transaccion = " + authentication.getName());
		
		String idCategory=request.getDto().getBudget().getIdCategory();
		ResponseBudgetInsert response = new ResponseBudgetInsert();
		boolean ok=this.budgetBusiness.insert(request.getDto().getBudget(), idUser, idCategory);
		

		if (ok) {
            response.success();
            response.listMessage.add("presupuesto realizada correctamente");
        } else {
            response.error();
            response.listMessage.add("Ya existe un presupuesto con esa fecha");
        }
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	 @GetMapping(path = "getAll")
	 public ResponseEntity<List<DtoBudget>> getAll(Authentication authentication) {
	    String idUser=authentication.getName();
	    System.out.println("AUTH NAME = " + authentication.getName());

	    return new ResponseEntity<>(this.budgetBusiness.getAll(idUser), HttpStatus.OK);
	  }
	 
	 @DeleteMapping(path = "delete/{idBudget}")
	 public ResponseEntity<ResponseBudgetDelete> delete(@PathVariable String idBudget,Authentication authentication) {

	     String idUser = authentication.getName();
	     ResponseBudgetDelete response = new ResponseBudgetDelete();

	     boolean deleted = this.budgetBusiness.delete(idBudget, idUser);

	     if (deleted) {
	         response.success();
	         response.listMessage.add("presupuesto eliminada correctamente");
	     } else {
	         response.error();
	         response.listMessage.add("No se pudo eliminar el presupuesto");
	     }

	     return ResponseEntity.ok(response);
	 }

	
	 @PutMapping(path = "update/{idBudget}", consumes = "multipart/form-data")
	 public ResponseEntity<ResponseBudgetInsert> updateById(@PathVariable String idBudget, @ModelAttribute requestBudgetInsert request,Authentication authentication) {
		 String idUser=authentication.getName();
		 System.out.println("AUTH NAME = " + authentication.getName());
	     boolean updated = this.budgetBusiness.update(idBudget, request.getDto().getBudget(), idUser);

	     ResponseBudgetInsert response = new ResponseBudgetInsert();

	    	if (!updated) {
	    	    response.error();
	    	    response.listMessage.add("No se pudo actualizar la transacción");
	    	    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    	}

	    	response.success();
	    	response.listMessage.add("Transacción actualizada correctamente");
	    	return ResponseEntity.ok(response);

	 }
	 @GetMapping("report/pdf")
	 public ResponseEntity<byte[]> downloadBudgetPdf(Authentication auth) throws Exception {
	     String idUser = auth.getName();
	     List<DtoBudget> budgets = budgetBusiness.getAll(idUser);
	     byte[] pdf = budgetBusiness.generatePdf(budgets);

	     return ResponseEntity.ok()
	         .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=presupuesto.pdf")
	         .contentType(MediaType.APPLICATION_PDF)
	         .body(pdf);
	 }



}
	
	
	


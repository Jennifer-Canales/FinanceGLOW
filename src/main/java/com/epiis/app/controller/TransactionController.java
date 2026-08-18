package com.epiis.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.epiis.app.business.TransactionBusiness;
import com.epiis.app.controller.reqresp.ResponseTransactionInsert;
import com.epiis.app.controller.reqresp.ResponseTransactionalDelete;
import com.epiis.app.controller.reqresp.requestTransactionInsert;
import com.epiis.app.dto.DtoTransaction;

@RestController
@RequestMapping("transaction")
public class TransactionController {

	@Autowired
	private TransactionBusiness transactionBusiness;

	@PostMapping(path = "insert", consumes = "multipart/form-data")
	public ResponseEntity<ResponseTransactionInsert> insert(@ModelAttribute requestTransactionInsert request, Authentication authentication) {
		String idUser=authentication.getName();
		System.out.println("AUTH NAME de transaccion = " + authentication.getName());
		
		String idCategory=request.getDto().getTransaction().getIdCategory();
		ResponseTransactionInsert response = new ResponseTransactionInsert();
		
		boolean ok=this.transactionBusiness.insert(request.getDto().getTransaction(), idUser, idCategory);
		

		if (ok) {
            response.success();
            response.listMessage.add("transaccion realizada correctamente");
        } else {
            response.error();
            response.listMessage.add("Ya existe una transaccion con esa fecha");
        }

		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	 @GetMapping(path = "getAll")
	 public ResponseEntity<List<DtoTransaction>> getAll(Authentication authentication) {
	   String idUser=authentication.getName();
	   System.out.println("AUTH NAME = " + authentication.getName());

	   return new ResponseEntity<>(this.transactionBusiness.getAll(idUser), HttpStatus.OK);
	 }
	 
	 @DeleteMapping(path = "delete/{idTransactional}")
	 public ResponseEntity<ResponseTransactionalDelete> delete(@PathVariable String idTransactional,Authentication authentication) {

	     String idUser = authentication.getName();
	     ResponseTransactionalDelete response = new ResponseTransactionalDelete();

	     boolean deleted = transactionBusiness.delete(idTransactional, idUser);

	     if (deleted) {
	         response.success();
	         response.listMessage.add("Transacción eliminada correctamente");
	     } else {
	         response.error();
	         response.listMessage.add("No se pudo eliminar la transacción");
	     }

	     return ResponseEntity.ok(response);
	 }

	
	 @PutMapping(path = "update/{idTransaction}", consumes = "multipart/form-data")
	 public ResponseEntity<ResponseTransactionInsert> updateById(
	         @PathVariable String idTransaction,
	         @ModelAttribute requestTransactionInsert request,
	         Authentication authentication) {

	     boolean updated = transactionBusiness.update(
	    	        idTransaction,
	    	        request.getDto().getTransaction(),
	    	        authentication.getName()
	    	);

	    	ResponseTransactionInsert response = new ResponseTransactionInsert();

	    	if (!updated) {
	    	    response.error();
	    	    response.listMessage.add("No se pudo actualizar la transacción");
	    	    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	    	}

	    	response.success();
	    	response.listMessage.add("Transacción actualizada correctamente");
	    	return ResponseEntity.ok(response);

	 }
	 
	 @GetMapping("/income-expense")
	    public List<Map<String, Object>> ingresosVsGastos(Authentication authentication) {
		 String idUser = authentication.getName();
	        return transactionBusiness.ingresosVsGastosPorMes(idUser);
	    }
	 
	 @GetMapping("/total-income")
	 public ResponseEntity<?> totalIncome(Authentication authentication) {

	     String idUser = authentication.getName(); 
	     Double total = transactionBusiness.getTotalIncome(idUser);

	     return ResponseEntity.ok(total);
	 }

	
	
}

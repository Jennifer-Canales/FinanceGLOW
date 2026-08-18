package com.epiis.app.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.*;

import com.epiis.app.business.CategoryBusiness;
import com.epiis.app.controller.reqresp.ResponseCategoryDelete;
import com.epiis.app.controller.reqresp.ResponseCategoryInsert;
import com.epiis.app.controller.reqresp.requestCategoryInsert;
import com.epiis.app.dto.DtoCategory;

@RestController
@RequestMapping("category")
public class CategoryController {

    private final CategoryBusiness categoryBusiness;

    public CategoryController(CategoryBusiness categoryBusiness) {
        this.categoryBusiness = categoryBusiness;
    }

    @PostMapping(path = "insert", consumes = "multipart/form-data")
    public ResponseEntity<ResponseCategoryInsert> insert(@ModelAttribute requestCategoryInsert request,Authentication authentication) {
    	String idUser=authentication.getName();
    	ResponseCategoryInsert response = new ResponseCategoryInsert();
    	System.out.println("AUTH NAME = " + authentication.getName());

        boolean ok = this.categoryBusiness.insert(request.getDto().getCategory(),idUser);

        if (ok) {
            response.success();
            response.listMessage.add("Operación realizada correctamente");
        } else {
            response.error();
            response.listMessage.add("Ya existe una categoría con ese nombre");
        }

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "getAll")
    public ResponseEntity<List<DtoCategory>> getAll(Authentication authentication) {
    	String idUser=authentication.getName();
    	System.out.println("AUTH NAME = " + authentication.getName());

    	return new ResponseEntity<>(this.categoryBusiness.getAll(idUser), HttpStatus.OK);
    }

   @DeleteMapping(path = "delete/{idCategory}")
    public ResponseEntity<ResponseCategoryDelete> deleteById(@PathVariable String idCategory,@RequestParam(defaultValue = "false") boolean force,Authentication authentication) {
	   String idUser=authentication.getName();
	   
	   ResponseCategoryDelete response =categoryBusiness.deleteCategory(idCategory, idUser, force);
	    if ("warning".equals(response.getType())) {
	        return ResponseEntity.ok(response);
	    }

	    
	    if ("error".equals(response.getType())) {
	        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
	    }

	    return ResponseEntity.ok(response);
	}
   
   @PutMapping(path = "updateById/{idCategory}",consumes = "multipart/form-data")
   public ResponseEntity<ResponseCategoryInsert> updateById(@PathVariable String idCategory,@ModelAttribute requestCategoryInsert request,Authentication authentication) {
	   String idUser=authentication.getName();
	   ResponseCategoryInsert response = new ResponseCategoryInsert();
       boolean update = categoryBusiness.updateById(idCategory,request.getDto().getCategory(),idUser);

       if (!update) {
           response.error();
           response.listMessage.add("No se logro actualizar");
           return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
       }

       response.success();
       response.listMessage.add("Categoría actualizada correctamente");
       return new ResponseEntity<>(response, HttpStatus.OK);
   }
   
   
   @GetMapping(path = "getAllName/{name}")
   public ResponseEntity<List<DtoCategory>> getAllName(@PathVariable String name,Authentication authentication) {
   	String idUser=authentication.getName();
   	System.out.println("AUTH NAME = " + authentication.getName());

   	return new ResponseEntity<>(this.categoryBusiness.getAllName(name, idUser), HttpStatus.OK);
   }
   
   @GetMapping(path="count")
	public ResponseEntity<Long> count(Authentication authentication) {
	   String idUser=authentication.getName();
	   System.out.println("AUTH NAME = " + authentication.getName());
	   long total = this.categoryBusiness.count(idUser);
	return new ResponseEntity<>(total, HttpStatus.OK);
	}
   @GetMapping(path = "getAllType")
   public ResponseEntity<List<DtoCategory>> getAllType(Authentication authentication) {
   	String idUser=authentication.getName();
   	System.out.println("AUTH NAME = " + authentication.getName());
   	System.out.println("se listaron las categorias por tipos");
   	return new ResponseEntity<>(this.categoryBusiness.getAllType(idUser), HttpStatus.OK);
   }

}

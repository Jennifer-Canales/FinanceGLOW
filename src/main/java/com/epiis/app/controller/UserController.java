package com.epiis.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.epiis.app.JwtService;
import com.epiis.app.business.UserBusiness;
import com.epiis.app.controller.reqresp.RequestLogin;
import com.epiis.app.controller.reqresp.ResponseLogin;
import com.epiis.app.controller.reqresp.ResponseUserInsert;
import com.epiis.app.controller.reqresp.requestUserInsert;
import com.epiis.app.dto.DtoUser;


@RestController
@RequestMapping("user")
public class UserController {
		
		@Autowired
	    private UserBusiness userBusiness;
		@Autowired
		private JwtService jwtService;
	    
	    @PostMapping(path = "register", consumes = "multipart/form-data")
	    public ResponseEntity<ResponseUserInsert> register(@ModelAttribute requestUserInsert request) {
	    	 ResponseUserInsert response = new ResponseUserInsert();

	    	    boolean registrado = this.userBusiness.register(request.getDto().getUser());

	    	    if (!registrado) {
	    	        response.error();
	    	        response.listMessage.add("El usuario ya existe");
	    	        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	    	    }

	    	    response.success();
	    	    response.listMessage.add("Usuario registrado correctamente");

	    	    return new ResponseEntity<>(response, HttpStatus.OK);
	       
	    }

	    @PostMapping(path = "login", consumes = "multipart/form-data")
	    public ResponseEntity<ResponseLogin> login(@ModelAttribute RequestLogin request) {
	        ResponseLogin responseLogin = new ResponseLogin();
	        
	        DtoUser dtoUser=this.userBusiness.login(request.getDto().getDtoUser().getFirstName(), request.getDto().getDtoUser().getPassword());
	        
	        if(dtoUser==null) {
	        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	        }
	        
	        String token=this.jwtService.generateToken(dtoUser);
	        
	        responseLogin.setToken(token);
	        
	        responseLogin.success();
	        responseLogin.listMessage.add("operacion realizada correctamente");
	        
	        return new ResponseEntity<>(responseLogin,HttpStatus.OK);
	        
	    }
	    @GetMapping("/name")
	    public ResponseEntity<?> getUserName(Authentication authentication) {

	        String idUser = authentication.getName(); // UUID desde el token
	        String name = userBusiness.getUserNameById(idUser);

	        return ResponseEntity.ok(name);
	    }


	    
	   
    
}

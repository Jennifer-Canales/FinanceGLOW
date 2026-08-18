package com.epiis.app.business;


import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epiis.app.dataaccess.UserRepository;
import com.epiis.app.dto.DtoUser;
import com.epiis.app.entity.User;

@Service
public class UserBusiness {
	@Autowired
	private UserRepository userRepository;
	
	public boolean register(DtoUser dtoUser) {
		
		if(dtoUser==null ) {
			return false;
		}
		if(this.userRepository.existsByFirstName(dtoUser.getFirstName())) {
			return false;
		}
		
		if(dtoUser.getFirstName()==null || dtoUser.getFirstName().trim().isEmpty()) {
			return false;
		}
		
		if(dtoUser.getPassword()==null || dtoUser.getPassword().trim().isEmpty()) {
			return false;
		}
		
		if(dtoUser.getFirstName().length()<3 || dtoUser.getPassword().length()<3) {
			return false;
		}
		
		dtoUser.setIdUser(UUID.randomUUID().toString());
		dtoUser.setCreatedAt(new Date());
		dtoUser.setUpdatedAt(dtoUser.getCreatedAt());
		
		User user = new User();
		
		user.setIdUser(dtoUser.getIdUser());
		user.setFirstName(dtoUser.getFirstName());
		user.setPassword(dtoUser.getPassword());
		user.setIsActive(true);
		user.setCreatedAt(new java.sql.Timestamp(dtoUser.getCreatedAt().getTime()));
		user.setUpdatedAt(new java.sql.Timestamp(dtoUser.getUpdatedAt().getTime()));
		
		this.userRepository.save(user);
		
		return true;
	}
	
	public DtoUser login(String firstName, String password) {
        Optional<User> userOptional = this.userRepository.findByFirstName(firstName);
        
        if(userOptional.isEmpty()) {
        	return null;
        }
        
        User user=userOptional.get();
        if(!user.getPassword().equals(password)) {
        	return null;
        }
        if(!Boolean.TRUE.equals(user.getIsActive())) {
        	return null;
        }
        
        DtoUser dtoUser=new DtoUser();
        dtoUser.setIdUser(user.getIdUser());
        dtoUser.setFirstName(user.getFirstName());
        dtoUser.setIsActive(user.getIsActive());
        
        return dtoUser;
    }


	public User getById(String idUser) {
	    return userRepository.findById(idUser).orElse(null);
	}
	public String getUserNameById(String idUser) {

        User user = userRepository.findById(idUser).orElse(null);

        if (user == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        return user.getFirstName(); 
    }
	
	


}

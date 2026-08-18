package com.epiis.app.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.epiis.app.controller.reqresp.ResponseCategoryDelete;
import com.epiis.app.dataaccess.CategoryRepository;
import com.epiis.app.dataaccess.TransactionRepository;
import com.epiis.app.dataaccess.UserRepository;
import com.epiis.app.dto.DtoCategory;
import com.epiis.app.entity.Category;
import com.epiis.app.entity.User;

import jakarta.transaction.Transactional;
@Service
public class CategoryBusiness {

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private TransactionRepository transactionRepository;

    public boolean insert(DtoCategory dtoCategory,String idUser) {
    	User user=userRepository.findById(idUser).orElse(null);
    	if(user == null) {
    		return false;
    	}
    	if(categoryRepository.existsByNameAndParentUser_IdUser(dtoCategory.getName().trim(), idUser)) {
    		return false;
    	}
    	if(dtoCategory.getName()==null || dtoCategory.getName().trim().isEmpty()) {
    		throw new IllegalArgumentException("el nombre es obligatorio");
    	}
    	
    	if (dtoCategory.getName().length() < 3 || dtoCategory.getName().length() > 30) {
    	   throw new IllegalArgumentException("El nombre debe tener entre 3 y 30 caracteres");
    	}

    	if (!"EXPENSE".equals(dtoCategory.getType()) && !"INCOME".equals(dtoCategory.getType())) {
    	    throw new IllegalArgumentException("El tipo debe ser EXPENSE o INCOME");
    	}
    	
        dtoCategory.setIdCategory(UUID.randomUUID().toString());
        dtoCategory.setCreatedAt(new Date());
        dtoCategory.setIdUser(idUser);
        dtoCategory.setUpdatedAt(dtoCategory.getCreatedAt());

        Category category = new Category();
        
        category.setIdCategory(dtoCategory.getIdCategory());
        category.setParentUser(user);
        
        category.setName(dtoCategory.getName());
        category.setType(dtoCategory.getType());
        category.setCreatedAt(new java.sql.Timestamp(dtoCategory.getCreatedAt().getTime()));
        category.setUpdatedAt(new java.sql.Timestamp(dtoCategory.getUpdatedAt().getTime()));

        try {
            categoryRepository.save(category);
            return true;
        } catch (DataIntegrityViolationException e) {
            return false; 
        }

    }

    public List<DtoCategory> getAll(String idUser) {
        List<Category>listCategory = this.categoryRepository.findByParentUser_idUser(idUser);

        List<DtoCategory> listDtoCategory = new ArrayList<>();
        
        for (Category item : listCategory) {
        	
            DtoCategory dto = new DtoCategory();
            dto.setIdCategory(item.getIdCategory());
            dto.setName(item.getName());
            dto.setType(item.getType());
            
            listDtoCategory.add(dto);
        }

        return listDtoCategory;
    }
    @Transactional
    public ResponseCategoryDelete deleteCategory(String idCategory,String idUser,boolean force) {

        ResponseCategoryDelete response = new ResponseCategoryDelete();

        User user = userRepository.findById(idUser).orElse(null);
        if (user == null) {
            response.error();
            response.listMessage.add("Usuario no válido");
            return response;
        }

        Category category = categoryRepository.findById(idCategory).orElse(null);
        if (category == null) {
            response.error();
            response.listMessage.add("Categoría no encontrada");
            return response;
        }

        if (!category.getParentUser().getIdUser().equals(user.getIdUser())) {
            response.error();
            response.listMessage.add("No autorizado");
            return response;
        }

        long totalTransactions =transactionRepository.countByParentCategory_IdCategory(idCategory);

        if (totalTransactions > 0 && !force) {
            response.warning();
            response.setTotalTransactions(totalTransactions);
            response.listMessage.add(
                "La categoría tiene " + totalTransactions + " transacciones asociadas"
            );
            return response;
        }

        transactionRepository.deleteByParentCategory_IdCategory(idCategory);
        categoryRepository.delete(category);

        response.success();
        response.listMessage.add("Categoría eliminada correctamente");
        response.setTotalTransactions(0);

        return response;
    }

   
   public boolean updateById(String idCategory,DtoCategory dtoCategory,String idUser) {
	   Category category = categoryRepository.findById(idCategory).orElse(null);
       if (category == null) return false;
       if (dtoCategory == null) {
           throw new IllegalArgumentException("Datos de la categoría no enviados");
       }
       
       if (!category.getParentUser().getIdUser().equals(idUser)) return false;
       
       if(dtoCategory.getName() == null || dtoCategory.getName().trim().isEmpty()) {
           throw new IllegalArgumentException("El nombre es obligatorio");
       }
       if(dtoCategory.getName().length() < 3 || dtoCategory.getName().length() > 30) {
           throw new IllegalArgumentException("El nombre debe tener entre 3 y 30 caracteres");
       }
       if(!"EXPENSE".equals(dtoCategory.getType()) && !"INCOME".equals(dtoCategory.getType())) {
           throw new IllegalArgumentException("El tipo debe ser EXPENSE o INCOME");
       }
       
       
       category.setName(dtoCategory.getName());
       category.setType(dtoCategory.getType());
       category.setUpdatedAt(new java.sql.Timestamp(new Date().getTime()));
       
       categoryRepository.save(category);
       return true;
       
   }
   public List<DtoCategory> getAllName(String name,String idUser) {
       List<Category>listCategory = this.categoryRepository.findByNameStartingWithAndParentUser(name, idUser);

       List<DtoCategory> listDtoCategory = new ArrayList<>();
       for (Category item : listCategory) {
       	
           DtoCategory dto = new DtoCategory();
           dto.setIdCategory(item.getIdCategory());
           dto.setName(item.getName());
           dto.setType(item.getType());
           
           listDtoCategory.add(dto);
       }

       return listDtoCategory;
   }
   
   public long count(String idUser){
		return this.categoryRepository.countByParentUser_IdUser(idUser);
	}

   public List<DtoCategory> getAllType(String idUser) {
       List<Category>listCategory = this.categoryRepository.findExpenseCategoriesByUser(idUser);

       List<DtoCategory> listDtoCategory = new ArrayList<>();
       
       for (Category item : listCategory) {
       	
           DtoCategory dto = new DtoCategory();
           dto.setIdCategory(item.getIdCategory());
           dto.setName(item.getName());
           
           listDtoCategory.add(dto);
       }

       return listDtoCategory;
   }

}

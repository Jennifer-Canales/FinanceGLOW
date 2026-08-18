package com.epiis.app.business;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.epiis.app.dataaccess.CategoryRepository;
import com.epiis.app.dataaccess.TransactionRepository;
import com.epiis.app.dataaccess.UserRepository;
import com.epiis.app.dto.DtoTransaction;
import com.epiis.app.entity.Category;
import com.epiis.app.entity.Transaction;
import com.epiis.app.entity.User;

@Service
public class TransactionBusiness {
	
	@Autowired
	private TransactionRepository transactionRepository;
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	
	public boolean insert(DtoTransaction dtoTransaction,String idUser,String idCategory) {
		
		User user=this.userRepository.findById(idUser).orElse(null);
		Category category=this.categoryRepository.findById(idCategory).orElse(null);
    	if(user == null || category==null) {
    		return false;
    	}
    	System.out.println("idUser=" + idUser + ", idCategory=" + idCategory);


    	if (dtoTransaction.getAmount() == null || dtoTransaction.getAmount() <= 0) {
    	    throw new IllegalArgumentException("El monto debe ser mayor a 0");
    	}

        if (dtoTransaction.getDateTransaction() == null) {
            throw new IllegalArgumentException("La fecha de transacción es obligatoria");
        }

        LocalDate fechaTransaccion = dtoTransaction.getDateTransaction()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        if (fechaTransaccion.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha no puede ser futura");
        }


        if (dtoTransaction.getIdCategory() == null || dtoTransaction.getIdCategory().isBlank()) {
            throw new IllegalArgumentException("Debe seleccionar una categoría");
        }
    
    
    	dtoTransaction.setIdTransaction(UUID.randomUUID().toString());
    	dtoTransaction.setIdCategory(idCategory);
    	dtoTransaction.setIdUser(idUser);
    	dtoTransaction.setCreatedAt(new Date());
    	dtoTransaction.setUpdatedAt(dtoTransaction.getCreatedAt());

        Transaction transaction = new Transaction();
        transaction.setIdTransaction(dtoTransaction.getIdTransaction());
        transaction.setParentUser(user);
        transaction.setParentCategory(category);
        
        
        transaction.setAmount(dtoTransaction.getAmount());
        transaction.setDescription(dtoTransaction.getDescription());
        transaction.setDateTransaction(new java.sql.Date(dtoTransaction.getDateTransaction().getTime()));
        
        transaction.setCreatedAt(new java.sql.Timestamp(dtoTransaction.getCreatedAt().getTime()));
        transaction.setUpdatedAt(new java.sql.Timestamp(dtoTransaction.getUpdatedAt().getTime()));

        transactionRepository.save(transaction);
        return true;
	}
	
	
	public List<DtoTransaction> getAll(String idUser) {
		List<Transaction> listTransaction = this.transactionRepository.findByUserWithCategory(idUser);
		
		List<DtoTransaction> listDtoTransaction = new ArrayList<>();
		
		for(Transaction item: listTransaction) {
			
			DtoTransaction dtoTransactionTemp = new DtoTransaction();
			
			dtoTransactionTemp.setIdTransaction(item.getIdTransaction());
			dtoTransactionTemp.setAmount(item.getAmount());
			dtoTransactionTemp.setDateTransaction(item.getDateTransaction());
			dtoTransactionTemp.setDescription(item.getDescription());
			
			dtoTransactionTemp.setIdCategory(item.getParentCategory().getIdCategory());
			dtoTransactionTemp.setNameCategory(item.getParentCategory().getName());
			listDtoTransaction.add(dtoTransactionTemp);
		}
		
		return listDtoTransaction;
	}
	
	public boolean delete(String idTransaction, String idUser) {

	    Transaction transaction = transactionRepository.findById(idTransaction).orElse(null);
	    if (transaction == null) return false;

	    if (!transaction.getParentUser().getIdUser().equals(idUser)) return false;

	    transactionRepository.delete(transaction);
	    return true;
	}

	public boolean update(String idTransaction,DtoTransaction dtoTransaction,String idUser) {
		   Transaction transaction = this.transactionRepository.findById(idTransaction).orElse(null);
		   if (transaction == null) return false;
		   
		   Category category = categoryRepository.findById(dtoTransaction.getIdCategory()) .orElse(null);
		   if (category == null) return false;

	       
	       if (!transaction.getParentUser().getIdUser().equals(idUser)) return false;
	       
	       if (dtoTransaction.getAmount() == null || dtoTransaction.getAmount() <= 0) {
		        throw new IllegalArgumentException("El monto debe ser mayor a 0");
	       }
		    if (dtoTransaction.getDateTransaction() == null) {
		        throw new IllegalArgumentException("La fecha es obligatoria");
		    }
		    LocalDate fecha = dtoTransaction.getDateTransaction().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		    if (fecha.isAfter(LocalDate.now())) {
		        throw new IllegalArgumentException("La fecha no puede ser futura");
		    }
	       
		   transaction.setParentCategory(category);
	       transaction.setAmount(dtoTransaction.getAmount());
	       transaction.setDescription(dtoTransaction.getDescription());
	       transaction.setDateTransaction(new java.sql.Date(dtoTransaction.getDateTransaction().getTime()));
	   	   transaction.setUpdatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
	       
	       transactionRepository.save(transaction);
	       return true;
	       
	   }

    public List<Map<String, Object>> ingresosVsGastosPorMes(String idUser) {

        List<Object[]> data = transactionRepository.ingresosVsGastosPorMes(idUser);
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : data) {

            Map<String, Object> item = new HashMap<>();
            item.put("month", row[0]);
            item.put("income", row[1]);
            item.put("expense", row[2]);

            response.add(item);
        }

        return response;
    }
    public Double getTotalIncome(String idUser) {
        return transactionRepository.totalIncomeByUser(idUser);
    }

}

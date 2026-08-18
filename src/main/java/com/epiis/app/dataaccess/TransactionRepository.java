package com.epiis.app.dataaccess;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.epiis.app.entity.Transaction;

public interface  TransactionRepository extends JpaRepository<Transaction,String>{
	
	@Query("""
			select transaction
			from Transaction transaction
			left join fetch transaction.parentCategory category
			where transaction.parentUser.idUser= :idUser
			""")List<Transaction>findByUserWithCategory(String idUser);
	
	
	@Query("""
		    SELECT COALESCE(SUM(t.amount), 0)
		    FROM Transaction t
		    WHERE t.parentCategory.idCategory = :idCategory
		    AND t.parentUser.idUser = :idUser
		    AND MONTH(t.dateTransaction) = :month
		    AND YEAR(t.dateTransaction) = :year
		""")
		Double getTotalSpentByCategoryAndMonth(
		    String idUser,
		    String idCategory,
		    int month,
		    int year
		);
	
	long countByParentCategory_IdCategory(String idCategory);

    void deleteByParentCategory_IdCategory(String idCategory);
   
    @Query("""
    		SELECT 
    		   MONTH(t.dateTransaction),
    		   SUM(CASE WHEN t.parentCategory.type = 'INCOME' THEN t.amount ELSE 0 END),
    		   SUM(CASE WHEN t.parentCategory.type = 'EXPENSE' THEN t.amount ELSE 0 END)
    		FROM Transaction t
    		WHERE t.parentUser.idUser = :idUser
    		  AND YEAR(t.dateTransaction) = YEAR(CURRENT_DATE())
    		GROUP BY MONTH(t.dateTransaction)
    		ORDER BY MONTH(t.dateTransaction)
    		""")
    		List<Object[]> ingresosVsGastosPorMes(String idUser);
    		@Query("""
    				SELECT COALESCE(SUM(t.amount), 0)
    				FROM Transaction t
    				WHERE t.parentUser.idUser = :idUser
    				AND t.parentCategory.type = 'INCOME'
    				""")
    				Double totalIncomeByUser(String idUser);

}

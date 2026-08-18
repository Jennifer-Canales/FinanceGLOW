package com.epiis.app.dataaccess;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.epiis.app.entity.Budget;


public interface  BudgetRepository extends JpaRepository<Budget, String> {
	
	@Query("""
			select budget
			from Budget budget
			left join fetch budget.parentCategory category
			where budget.parentUser.idUser= :idUser
			""")List<Budget>findByUserWithCategory(String idUser);
}

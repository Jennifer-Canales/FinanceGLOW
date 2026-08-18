package com.epiis.app.dataaccess;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.epiis.app.entity.Category;

public interface  CategoryRepository  extends JpaRepository<Category,String>{
	
	@Query("""
			select category
			from Category category
			where category.parentUser.idUser= :idUser
			""")List<Category>findByParentUser_idUser(String idUser);
	
	boolean existsByNameAndParentUser_IdUser(String name, String idUser);
	
	@Query("""
		    select c
		    from Category c
		    where lower(c.name) like lower(concat(:name, '%'))
		      and c.parentUser.idUser = :idUser
		""")
		List<Category> findByNameStartingWithAndParentUser(String name,String idUser);
	
	@Query("""
		    select count(c)
		    from Category c
		    where c.parentUser.idUser = :idUser
		""")
		long countByParentUser_IdUser(String idUser);
	@Query("""
			  SELECT c
			  FROM Category c
			  WHERE c.parentUser.idUser = :idUser
			  AND c.type = 'EXPENSE'
			""")
			List<Category> findExpenseCategoriesByUser(String idUser);

}

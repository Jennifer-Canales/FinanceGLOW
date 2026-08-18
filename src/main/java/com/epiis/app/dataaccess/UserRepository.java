package com.epiis.app.dataaccess;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.epiis.app.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
	Optional<User> findByFirstName(String firstName);

    boolean existsByFirstName(String firstName);
	@Query("""
			select user 
			from User user
			left join fetch user.listCategory category
			""")
	List<User> getAll();
	
	
	

}

package com.sapient.userapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sapient.userapi.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
	
	@Query("SELECT u FROM User u " +
	           "WHERE LOWER(u.firstName) LIKE LOWER(CONCAT('%', :text, '%')) " +
	           "OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :text, '%')) " +
	           "OR LOWER(u.ssn) LIKE LOWER(CONCAT('%', :text, '%'))")
	    List<User> searchUsers(@Param("text") String text);
}

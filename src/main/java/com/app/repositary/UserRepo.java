package com.app.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.app.entity.User1;
@Repository
public interface UserRepo extends JpaRepository<User1, Integer> {
	
	@Query("select u from User1 u where u.email = :email")
	public User1 getUserByUserName(@Param("email") String email);
}

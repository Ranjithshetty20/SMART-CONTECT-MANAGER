package com.app.repositary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;
import com.app.entity.Contect;

public interface ContectRepo extends JpaRepository<Contect, Integer> {

	@Query("from Contect as c where c.user1.id=:userId")
	public List<Contect> findContectsByUser(@Param("userId") int userId);
}

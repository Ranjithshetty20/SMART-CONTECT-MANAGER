package com.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.entity.User1;
import com.app.repositary.UserRepo;

@Service
public class UserService {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	public void addUser(User1 user1)
	{
		user1.setPassword(passwordEncoder.encode(user1.getPassword()));
		userRepo.save(user1);
		
	}
	
}

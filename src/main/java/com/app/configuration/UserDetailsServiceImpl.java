package com.app.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.app.entity.User1;
import com.app.repositary.UserRepo;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserRepo repo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //fecthing user from data base
		User1 user1=repo.getUserByUserName(username);
		
		if(user1==null)
		{
			throw new UsernameNotFoundException("could not found user name");
		}
		CustomUserDetails customUserDetails=new CustomUserDetails(user1);
				
		return customUserDetails;
	}

}

package com.asdc.smarticle.security.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.asdc.smarticle.user.User;
import com.asdc.smarticle.user.UserRepository;

/**
 * 
 * @author Sarthak Patel
 * @version 1.0
 * @since 2022-02-22
 */

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User appUser = userRepository.findByUserName(username);

		if (appUser == null) {
			System.out.println("User not found! " + username);
			throw new UsernameNotFoundException("User " + username + " was not found in the database");
		}

		System.out.println("Found User: " + appUser);

		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		GrantedAuthority authority = new SimpleGrantedAuthority("USER");
		grantList.add(authority);

		UserDetails userDetails = (UserDetails) new org.springframework.security.core.userdetails.User(
				appUser.getUserName(), appUser.getPswd(), grantList);

		return userDetails;
	}
}
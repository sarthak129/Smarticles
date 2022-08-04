package com.asdc.smarticle;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.asdc.smarticle.security.service.UserDetailsServiceImpl;
import com.asdc.smarticle.user.User;
import com.asdc.smarticle.user.UserRepository;
import com.asdc.smarticle.user.exception.ArticleException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserDetailsServiceSpringUnitTest {

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;
	
	@MockBean
	UserRepository userRepository;
	
	@MockBean
	UserDetails userDetails;
	
	@MockBean
	User user;
	
	@MockBean
	GrantedAuthority authority;
	
	@Test
	void testLoadUserByUserName() {
		String username = "sarthak";
		String password = "password";
		Mockito.when(user.getUserName()).thenReturn(username);
		Mockito.when(user.getPswd()).thenReturn(password);
		Mockito.when(userRepository.findByUserName(username)).thenReturn(user);
		List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
		authority = new SimpleGrantedAuthority("USER");
		grantList.add(authority);
		userDetails = (UserDetails) new org.springframework.security.core.userdetails.User(
				user.getUserName(), user.getPswd(), grantList);
		Assert.assertEquals(userDetails, userDetailsServiceImpl.loadUserByUsername(username));
		
		Mockito.when(userRepository.findByUserName(username)).thenReturn(null);
		Assert.assertThrows(UsernameNotFoundException.class, ()->userDetailsServiceImpl.loadUserByUsername(username));
	}
}

package com.asdc.smarticle;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.asdc.smarticle.security.JwtAuthTokenFilter;
import com.asdc.smarticle.security.JwtUtils;
import com.asdc.smarticle.security.UsernamePasswordAuthenticationTokenFactory;
import com.asdc.smarticle.security.service.UserDetailsServiceImpl;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JwtAuthTokenFilterUnitTest {
	
	@Autowired
	JwtAuthTokenFilter jwtAuthTokenFilter;
	
	@MockBean
	JwtUtils jwtUtils;

	@MockBean
	HttpServletRequest httpServletRequest;
	
	@MockBean
	UserDetails userDetails;
	
	@MockBean
	UserDetailsServiceImpl userDetailsService;
	
	@MockBean
	UsernamePasswordAuthenticationToken authenticationToken;
	
	@MockBean
	UsernamePasswordAuthenticationTokenFactory authenticationTokenFactory;
	
	@MockBean
	FilterChain filterChain;
	
	@MockBean
	HttpServletResponse response;
	
	@Test
	void testParseJwt(){
		Mockito.when(jwtUtils.getJwtTokenFromCookies(httpServletRequest)).thenReturn("jwt");
		Assert.assertEquals("jwt", jwtAuthTokenFilter.parseJwt(httpServletRequest));
	}
	
	@Test
	void testDoFilterInternal() throws Exception {
		String jwt = "jwt";
		String userName = "sarthak";
		Mockito.doReturn(jwt).when(Mockito.spy(jwtAuthTokenFilter)).parseJwt(httpServletRequest);
		Mockito.when(jwtUtils.validateJwt("jwt")).thenReturn(true);
		Mockito.when(jwtUtils.getUserNameFromJwt(jwt)).thenReturn(userName);
		Mockito.when(userDetailsService.loadUserByUsername(jwt)).thenReturn(userDetails);
		Mockito.when(authenticationTokenFactory.getUsernamePasswordAuthenticationTokenFactory(userDetails)).thenReturn(authenticationToken);
		authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
		SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		filterChain.doFilter(httpServletRequest, response);
		jwtAuthTokenFilter.doFilterInternal(httpServletRequest, response, filterChain);
	}
}

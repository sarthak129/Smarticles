package com.asdc.smarticle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.asdc.smarticle.articletag.Tag;
import com.asdc.smarticle.mailing.EmailService;
import com.asdc.smarticle.security.JwtUtils;
import com.asdc.smarticle.token.Token;
import com.asdc.smarticle.token.TokenService;
import com.asdc.smarticle.user.User;
import com.asdc.smarticle.user.UserService;
import com.asdc.smarticle.user.userVo.UserProfileRequestVo;
import com.asdc.smarticle.user.userVo.UserProfileRespVo;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	UserService userService;

	@MockBean
	ResponseCookie cookie;

	@MockBean
	JwtUtils jwtUtils;

	@MockBean
	User user;

	@MockBean
	Token token;

	@MockBean
	TokenService tokenService;

	@MockBean
	EmailService emailService;
	
	@MockBean
	HttpHeaders http;
	
	@MockBean
	UserProfileRespVo profileRespVo;
	
	@MockBean
	UserProfileRequestVo profileRequestVo;
	
	@MockBean
	UserDetails userDetails;
	
	@MockBean
	AuthenticationManager authenticationManager;

	@MockBean
	Authentication authentication;
	
	@MockBean
	Exception exception;
	
	@Test
	void testActivateUser() throws Exception {

		// Mockito.verify(userService.verifyUser("token"));
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/smarticleapi/user/activateAccount?{}")
				.contentType(MediaType.APPLICATION_JSON).param("token", "token");

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
	}

	@Test
	void testLogoutUser() throws Exception {

		Mockito.when(jwtUtils.getCleanJwtTokenCookie()).thenReturn(cookie);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/smarticleapi/user/logout")
				.contentType(MediaType.APPLICATION_JSON);

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();

		Mockito.when(jwtUtils.getCleanJwtTokenCookie()).thenReturn(null);
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
	}

	@Test
	void testRegisterUser() throws Exception {
		Mockito.when(tokenService.createToken(user)).thenReturn(token);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/smarticleapi/user/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(user));
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
	}
	
	@Test
	void testForgotPassword() throws Exception {
		Mockito.when(user.getEmailID()).thenReturn("test@gmail.com");
		Mockito.when(userService.getUserByEmailID(user.getEmailID())).thenReturn(user);
		
		Mockito.when(user.getUserName()).thenReturn("sarthak");
		Mockito.when(jwtUtils.generateJwtTokenCookie(user.getUserName())).thenReturn(cookie);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/smarticleapi/user/forgotPassword")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(user));
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();

		Mockito.when(userService.getUserByEmailID(user.getEmailID())).thenReturn(null);
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
	}
	
	@Test
	void testResetPassword() throws Exception {
		
		Mockito.when(http.getFirst("jwt-token")).thenReturn("sarthakjwt");
		Mockito.when(jwtUtils.getUserNameFromJwt("sarthakjwt")).thenReturn("sarthak");
		
		Mockito.when(user.getPswd()).thenReturn("pswd");
		Mockito.when(userService.updateUserPassword("sarthak",user.getPswd())).thenReturn(user);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/smarticleapi/user/resetPassword")
				.contentType(MediaType.APPLICATION_JSON)
				.header("jwt-token", "sarthakjwt")
				.content(new ObjectMapper().writeValueAsString(user));
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		Mockito.when(userService.getUserByEmailID(user.getEmailID())).thenReturn(null);
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		Mockito.when(http.getFirst("jwt-token")).thenReturn(null);
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		Mockito.when(http.getFirst("jwt-token")).thenReturn("sarthakjwt");
		Mockito.when(jwtUtils.getUserNameFromJwt("sarthakjwt")).thenReturn("sarthak");
		Mockito.when(user.getPswd()).thenReturn("pswd");
		Mockito.when(userService.updateUserPassword("sarthak",user.getPswd())).thenReturn(null);
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		MockHttpServletRequestBuilder mockRequestCatch = MockMvcRequestBuilders.post("/smarticleapi/user/resetPassword")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(user));
		mockMvc.perform(mockRequestCatch).andExpect(status().isOk()).andReturn();
	}
	
	@Test
	void testSaveUserTag() throws Exception {
		
		Set<Tag> tagList = new HashSet<Tag>();
		
		Mockito.when(http.getFirst("jwt-token")).thenReturn("sarthakjwt");
		Mockito.when(jwtUtils.getUserNameFromJwt("sarthakjwt")).thenReturn("sarthak");
		Mockito.when(userService.saveUserPrefTags("sarthak",tagList)).thenReturn(user);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/smarticleapi/user/saveUserTagPref")
				.contentType(MediaType.APPLICATION_JSON)
				.header("jwt-token", "sarthakjwt")
				.content(new ObjectMapper().writeValueAsString(tagList));
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		Mockito.when(http.getFirst("jwt-token")).thenReturn("sarthakjwt");
		Mockito.when(jwtUtils.getUserNameFromJwt("sarthakjwt")).thenReturn("sarthak");
		Mockito.when(userService.saveUserPrefTags("sarthak",tagList)).thenReturn(null);
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		
		MockHttpServletRequestBuilder mockRequestCatch = MockMvcRequestBuilders.post("/smarticleapi/user/saveUserTagPref")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(tagList));
		mockMvc.perform(mockRequestCatch).andExpect(status().isOk()).andReturn();
	}
	
	@Test
	void testGetUserProfile() throws Exception {
		Mockito.when(http.getFirst("jwt-token")).thenReturn("sarthakjwt");
		Mockito.when(jwtUtils.getUserNameFromJwt("sarthakjwt")).thenReturn("sarthak");
		Mockito.when(userService.getUserDetails("sarthak",null)).thenReturn(profileRespVo);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/smarticleapi/user/getUserProfile")
				.contentType(MediaType.APPLICATION_JSON)
				.header("jwt-token", "sarthakjwt");
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		Mockito.when(http.getFirst("jwt-token")).thenReturn("sarthakjwt");
		Mockito.when(jwtUtils.getUserNameFromJwt("sarthakjwt")).thenReturn("sarthak");
		Mockito.when(userService.getUserDetails("sarthak",null)).thenReturn(null);
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		MockHttpServletRequestBuilder mockRequestCatch = MockMvcRequestBuilders.get("/smarticleapi/user/getUserProfile")
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(mockRequestCatch).andExpect(status().isOk()).andReturn();
	}
	
	@Test
	void testUpdateUserProfile() throws Exception {
		Mockito.when(http.getFirst("jwt-token")).thenReturn("sarthakjwt");
		Mockito.when(jwtUtils.getUserNameFromJwt("sarthakjwt")).thenReturn("sarthak");
		Mockito.when(userService.updateUserProfile(profileRequestVo)).thenReturn(user);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/smarticleapi/user/updateUserProfile")
				.contentType(MediaType.APPLICATION_JSON)
				.header("jwt-token", "sarthakjwt")
				.content(new ObjectMapper().writeValueAsString(profileRequestVo));
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		Mockito.when(http.getFirst("jwt-token")).thenReturn("sarthakjwt");
		Mockito.when(jwtUtils.getUserNameFromJwt("sarthakjwt")).thenReturn("sarthak");
		Mockito.when(userService.updateUserProfile(profileRequestVo)).thenReturn(null);
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		MockHttpServletRequestBuilder mockRequestCatch = MockMvcRequestBuilders.post("/smarticleapi/user/updateUserProfile")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(profileRequestVo));
		mockMvc.perform(mockRequestCatch).andExpect(status().isOk()).andReturn();
	}
	
	@Test
	void testGetUserDetailsPostedArticle() throws Exception {
		List<Map<String, Object>> userDetailsOfPostedArticle = new ArrayList<>();
		Mockito.when(http.getFirst("jwt-token")).thenReturn("sarthakjwt");
		Mockito.when(jwtUtils.getUserNameFromJwt("sarthakjwt")).thenReturn("sarthak");
		Mockito.when(userService.getUsersPostedArticle()).thenReturn(userDetailsOfPostedArticle);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/smarticleapi/user/getUserDetailsPostedArticle")
				.contentType(MediaType.APPLICATION_JSON)
				.header("jwt-token", "sarthakjwt");
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		MockHttpServletRequestBuilder mockRequestCatch = MockMvcRequestBuilders.get("/smarticleapi/user/getUserDetailsPostedArticle")
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(mockRequestCatch).andExpect(status().isOk()).andReturn();
	}
	
	@Test
	void testAuthenticateUser() throws Exception {

		Mockito.when(user.getUserName()).thenReturn("vivek"); 
		Mockito.when(userService.isUserVerified("vivek")).thenReturn("verify");
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/smarticleapi/user/login")
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(user));

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		Mockito.when(userService.isUserVerified("vivek")).thenReturn("verified");
		Mockito.when(authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPswd())))
				.thenReturn(authentication);
		Mockito.when((UserDetails) authentication.getPrincipal()).thenReturn(userDetails);
		Mockito.when((jwtUtils.generateJwtTokenCookie(userDetails.getUsername()))).thenReturn(cookie);
		Mockito.when(cookie.getValue()).thenReturn("response");
		Mockito.when(userService.getUserByUserName("vivek")).thenReturn(user);

		Mockito.when(user.getUserName()).thenReturn("vivek");
		Mockito.when(user.getFirstName()).thenReturn("vivek");
		Mockito.when(user.getLastName()).thenReturn("patel");
	

		MockHttpServletRequestBuilder mockRequest2 = MockMvcRequestBuilders.post("/smarticleapi/user/login")
				.contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(user));

		mockMvc.perform(mockRequest2).andExpect(status().isOk()).andReturn();
	}
}

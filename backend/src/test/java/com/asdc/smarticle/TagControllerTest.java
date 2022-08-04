package com.asdc.smarticle;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.asdc.smarticle.articletag.Tag;
import com.asdc.smarticle.articletag.TagService;
import com.asdc.smarticle.security.JwtUtils;
import com.asdc.smarticle.user.User;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TagControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	TagService tagService;
	
	@MockBean
	HttpHeaders http;
	
	@MockBean
	JwtUtils jwtUtils;
	
	@MockBean
	User user;
	
	@Test
	void testGetUserTag() throws Exception {
		Mockito.when(http.getFirst("jwt-token")).thenReturn("sarthakjwt");
		Mockito.when(jwtUtils.isTokenEmpty("sarthakjwt")).thenReturn(true);
		Mockito.when(jwtUtils.isTokenUndefined("sarthakjwt")).thenReturn(true);
		Mockito.when(jwtUtils.getUserNameFromJwt("sarthakjwt")).thenReturn("sarthak");
		Mockito.when(tagService.getTags("sarthak")).thenReturn(null);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/smarticleapi/tag/retriveTags")
				.contentType(MediaType.APPLICATION_JSON)
				.header("jwt-token", "sarthakjwt");
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		Mockito.when(http.getFirst("jwt-token")).thenReturn("sarthakjwt");
		Mockito.when(jwtUtils.isTokenEmpty("sarthakjwt")).thenReturn(false);
		Mockito.when(jwtUtils.isTokenUndefined("sarthakjwt")).thenReturn(true);
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		Mockito.when(http.getFirst("jwt-token")).thenReturn("sarthakjwt");
		Mockito.when(jwtUtils.isTokenEmpty("sarthakjwt")).thenReturn(true);
		Mockito.when(jwtUtils.isTokenUndefined("sarthakjwt")).thenReturn(false);
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		Mockito.when(http.getFirst("jwt-token")).thenReturn("sarthakjwt");
		Mockito.when(jwtUtils.isTokenEmpty("sarthakjwt")).thenReturn(false);
		Mockito.when(jwtUtils.isTokenUndefined("sarthakjwt")).thenReturn(false);
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();	
	}
	
	@Test
	void testUpdateUserProfile() throws Exception {
		List<Tag> tagList = null;
		String tagName = "tag";
		Mockito.when(http.getFirst("jwt-token")).thenReturn("sarthakjwt");
		Mockito.when(tagService.createArticleTag(tagName)).thenReturn(tagList);
		
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/smarticleapi/tag/createArticleTag")
				.contentType(MediaType.APPLICATION_JSON)
				.header("jwt-token", "sarthakjwt")
				.content(new ObjectMapper().writeValueAsString(tagName));
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		Mockito.when(http.getFirst("jwt-token")).thenReturn("");
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		Mockito.when(http.getFirst("jwt-token")).thenReturn("sarthakjwt");
		Mockito.when(tagService.createArticleTag(tagName)).thenReturn(null);
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		MockHttpServletRequestBuilder mockRequestCatch = MockMvcRequestBuilders.post("/smarticleapi/tag/createArticleTag")
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(tagName));
		mockMvc.perform(mockRequestCatch).andExpect(status().isOk()).andReturn();
	}
	
	@Test
	void testGetTag() throws Exception {
		List<Tag> tagList = null;
		Mockito.when(tagService.retrieveAllTags()).thenReturn(tagList);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/smarticleapi/tag/retriveAllTags")
				.contentType(MediaType.APPLICATION_JSON)
				.header("jwt-token", "sarthakjwt");
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
	}
	
}

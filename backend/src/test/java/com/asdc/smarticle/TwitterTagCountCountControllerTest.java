package com.asdc.smarticle;

import com.asdc.smarticle.articletag.Tag;
import com.asdc.smarticle.security.JwtUtils;
import com.asdc.smarticle.twittertagcount.TwitterTagCountServiceImpl;
import com.asdc.smarticle.user.User;
import com.asdc.smarticle.user.UserServiceImpl;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.*;

/**
 * @author Rushi Patel
 * @version 1.0
 * @since 2022-03-18
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TwitterTagCountCountControllerTest {
	
	@Autowired
	private MockMvc mockMvc;

    @MockBean
    UserServiceImpl userService;

    @MockBean
    TwitterTagCountServiceImpl twitterTagCountService;
    
	@MockBean
	HttpHeaders http;
	
	@MockBean
	JwtUtils jwtUtils;
	
	@MockBean
	User user;
	
	@MockBean
	Set<Tag> userTags;
    
    @Test
    void testFetchUserTags() throws Exception {
    	List<Map<String,Object>> responseData = new ArrayList<>();
    	Mockito.when(http.getFirst("jwt-token")).thenReturn("sarthakjwt");
 		Mockito.when(jwtUtils.getUserNameFromJwt("sarthakjwt")).thenReturn("sarthak");
 		Mockito.when(userService.getUserByUserName("sarthak")).thenReturn(user);
 		Mockito.when(user.getTags()).thenReturn(userTags);
 		Mockito.when(twitterTagCountService.getTwitterTagCount(userTags)).thenReturn(responseData);
 		
 		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/smarticleapi/twittertagcount/getUserTags")
				.contentType(MediaType.APPLICATION_JSON)
				.header("jwt-token", "sarthakjwt");
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
		
		
		MockHttpServletRequestBuilder mockRequestCatch = MockMvcRequestBuilders.get("/smarticleapi/twittertagcount/getUserTags")
				.contentType(MediaType.APPLICATION_JSON);
		mockMvc.perform(mockRequestCatch).andExpect(status().isOk()).andReturn();
    }
    
}

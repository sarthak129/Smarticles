package com.asdc.smarticle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.asdc.smarticle.article.Article;
import com.asdc.smarticle.article.ArticleService;
import com.asdc.smarticle.article.FilterPojo;
import com.asdc.smarticle.comutil.AppConstant;
import com.asdc.smarticle.security.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
//@WebMvcTest(ArticleController.class)
public class ArticleControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	ArticleService articleService;

	@Mock
	ObjectMapper objectMapper;

	@MockBean
	FilterPojo filterPojo;

	@MockBean
	HttpHeaders http;

	@MockBean
	Article article;

	@MockBean
	JwtUtils jwtUtils;

	@Test
	void testRetrieveArticle() throws Exception {

		Page<Article> pro = Mockito.mock(Page.class);
		FilterPojo filter = new FilterPojo();
		Mockito.when(objectMapper.readValue("", FilterPojo.class)).thenReturn(filterPojo);
		Mockito.when(articleService.getArticle("1", null)).thenReturn(pro);
		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/smarticleapi/article/retrieveArticle")
				.contentType(MediaType.APPLICATION_JSON).param("visibility", "ALL").param("filterParam", new ObjectMapper().writeValueAsString(filter));

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
	}

	@Test 
	void testSaveArticle() throws Exception {

		Page<Article> pro = Mockito.mock(Page.class);

		Mockito.when(http.getFirst("jwt-token")).thenReturn("vkpatel4312");
		Mockito.when(jwtUtils.getUserNameFromJwt("vkpatel4312")).thenReturn("vivek");
		Mockito.when(articleService.getArticle("1", null)).thenReturn(pro);

		Article article = new Article();

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/smarticleapi/article/postarticle")
				.contentType(MediaType.APPLICATION_JSON).header("jwt-token", "vkpatel4312")
				.content(new ObjectMapper().writeValueAsString(article));

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
	}

	@Test
	void testgetArticleById() throws Exception {

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/smarticleapi/article/getArticleById")
				.contentType(MediaType.APPLICATION_JSON).header("jwt-token", "vkpatel4312").queryParam("id", "1");

		Mockito.when(articleService.getArticleById((long) 1)).thenReturn(article);
		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
	}

	@Test
	void testGetArticleByUser() throws Exception {

		Article article = new Article(); 
		List<Article> articleList = new ArrayList<>();
		articleList.add(article);

		Page<Article> articleListPage = new PageImpl<Article>(articleList);

		Mockito.when(http.getFirst("jwt-token")).thenReturn("vkpatel4312");
		Mockito.when(jwtUtils.getUserNameFromJwt("vkpatel4312")).thenReturn("vivek");
		Mockito.when(articleService.getArticleByUser("vivek", AppConstant.DEFAULT_PAGE, AppConstant.TOTAL_RECORDS)).thenReturn(articleListPage);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.get("/smarticleapi/article/getArticleByUser")
				.contentType(MediaType.APPLICATION_JSON).header("jwt-token", "vkpatel4312").param("page", String.valueOf(AppConstant.DEFAULT_PAGE))
				.param("totalPage", String.valueOf(AppConstant.TOTAL_RECORDS));

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
	}
  
	@Test
	void testGetTweetDatarById() throws Exception {

		List<Map<String, Object>> tweetData = new ArrayList<>();

		Mockito.when(articleService.getTwitterCountOfArticleTags((long) 1)).thenReturn(tweetData);

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
				.get("/smarticleapi/article/getTweetDataOfArticle").contentType(MediaType.APPLICATION_JSON)
				.header("jwt-token", "vkpatel4312").param("id", "1");

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
	}

	@Test
	void testSetLike() throws Exception {

		List<Map<String, Object>> tweetData = new ArrayList<>();
		Article article = new Article();
		Mockito.when(http.getFirst("jwt-token")).thenReturn("vkpatel4312");
		Mockito.when(jwtUtils.getUserNameFromJwt("vkpatel4312")).thenReturn("vivek");

		MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders.post("/smarticleapi/article/setLike")
				.contentType(MediaType.APPLICATION_JSON).header("jwt-token", "vkpatel4312")
				.content(new ObjectMapper().writeValueAsString(article));

		mockMvc.perform(mockRequest).andExpect(status().isOk()).andReturn();
	}

}

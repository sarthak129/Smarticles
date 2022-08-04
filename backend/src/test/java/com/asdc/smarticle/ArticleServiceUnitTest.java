package com.asdc.smarticle;

import com.asdc.smarticle.article.Article;
import com.asdc.smarticle.article.ArticleRepository;
import com.asdc.smarticle.article.ArticleService;
import com.asdc.smarticle.article.ArticleServiceImpl;
import com.asdc.smarticle.article.FilterPojo;
import com.asdc.smarticle.articletag.Tag;
import com.asdc.smarticle.comutil.AppConstant;
import com.asdc.smarticle.user.User;
import com.asdc.smarticle.user.UserRepository;
import com.asdc.smarticle.user.exception.ArticleException;

import twitter4j.Twitter;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;


@ExtendWith(SpringExtension.class)
@SpringBootTest 
public class ArticleServiceUnitTest {
	 	 
 

	@MockBean
	private ArticleRepository articleRepo;// Dependency is mocked.

	@MockBean
	private UserRepository userRepository;
	
	@Autowired
	private ArticleServiceImpl articleServiceImpl;
	
	@Autowired
	private ArticleService articleService;
	 
	
	@MockBean
	private Article article;
	
	@MockBean
	private User user;
	
	@MockBean
	private FilterPojo filterPojo;
	
	@Mock
	private List<User> users;
	
	@Mock
	Optional<Article> articleOptional;
	
	@Test 
	void testSaveArticle() throws ArticleException {
		
		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		
		article.setUserId(user);
		Mockito.when(article.getContent()).thenReturn(null);
		Mockito.when(article.getHeading()).thenReturn(null);
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));
		
		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn("");
		Mockito.when(article.getHeading()).thenReturn("");
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));
		
		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn("ARTIFICAL INTELLIGENCE");
		Mockito.when(article.getHeading()).thenReturn(null);
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));

		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn(null);
		Mockito.when(article.getHeading()).thenReturn("AI");
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));
		
		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn("ARTIFICAL INTELLIGENCE");
		Mockito.when(article.getHeading()).thenReturn("");
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));

		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn("");
		Mockito.when(article.getHeading()).thenReturn("AI");
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));
		
		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn(null);
		Mockito.when(article.getHeading()).thenReturn("ARTIFICAL INTELLIGENCE");
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));

		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn("AI");
		Mockito.when(article.getHeading()).thenReturn(null);
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));
		
		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn("");
		Mockito.when(article.getHeading()).thenReturn("ARTIFICAL INTELLIGENCE");
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));

		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn("AI");
		Mockito.when(article.getHeading()).thenReturn("");
		Assert.assertThrows(ArticleException.class, ()->articleService.saveArticle(article, "alen"));		
		
		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getContent()).thenReturn("ARTIFICAL INTELLIGENCE");
		Mockito.when(article.getHeading()).thenReturn("AI");
		Mockito.when(articleRepo.save(article)).thenReturn(article);
		Assert.assertEquals(article, articleService.saveArticle(article, "alen"));
	} 
	  
	@Test 
	void testIsHeadingAndContentEmpty() {
		
		Mockito.when(article.getContent()).thenReturn("");
		Mockito.when(article.getHeading()).thenReturn("");
		Assert.assertTrue(articleServiceImpl.isContentEmpty(article));
		Assert.assertTrue(articleServiceImpl.isHeadingEmpty(article));
		
		Mockito.when(article.getContent()).thenReturn(null);
		Mockito.when(article.getHeading()).thenReturn(null);
		
		Assert.assertTrue(articleServiceImpl.isContentEmpty(article));
		Assert.assertTrue(articleServiceImpl.isHeadingEmpty(article)); 
		
		
		Mockito.when(article.getContent()).thenReturn("BLOCKCHAIN"); 
		Mockito.when(article.getHeading()).thenReturn("AI");

		Assert.assertFalse(articleServiceImpl.isContentEmpty(article));
		Assert.assertFalse(articleServiceImpl.isHeadingEmpty(article)); 
		
	}
	 
	 
	@Test
	void testSetLike() {

		Set<User> users = new HashSet<>();

		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getId()).thenReturn((long) 1);
		Mockito.when(articleRepo.getById(article.getId())).thenReturn(article);
		Mockito.when(article.getLike()).thenReturn(users);

		article.setLike(article.getLike());
		Mockito.when(articleRepo. save(article)).thenReturn(article);
		articleService.setLike(article, "alen");
		
		users.add(user);
		Mockito.when(userRepository.findByUserName("alen")).thenReturn(user);
		Mockito.when(article.getId()).thenReturn((long) 1);
		Mockito.when(articleRepo.getById(article.getId())).thenReturn(article);
		Mockito.when(article.getLike()).thenReturn(users);
		article.setLike(article.getLike()); 
		Mockito.when(articleRepo. save(article)).thenReturn(article);
		articleService.setLike(article, "alen");
		
	}
	
	@Test
	void TestIsTagListEmpty() { 
		
		Tag tag=new Tag();
		tag.setId((long)1);
		tag.setTagName("BLOCKCHAIN");
		Set<Tag> tagSet=new HashSet<>();
		
		Mockito.when(filterPojo.getTagList()).thenReturn(null);
		Assert.assertTrue(articleService.isUserListEmpty(filterPojo));
		Mockito.when(filterPojo.getTagList()).thenReturn(tagSet);
		Assert.assertTrue(articleService.isUserListEmpty(filterPojo));
		tagSet.add(tag);
		Mockito.when(filterPojo.getTagList()).thenReturn(tagSet);
		Assert.assertFalse(articleService.isTagListEmpty(filterPojo));
		
	}   
	
	@Test
	void TestIsUserListEmpty() {

		List<Long> userIdList = new ArrayList<>();

		Mockito.when(filterPojo.getUserIdList()).thenReturn(null);
		Assert.assertFalse(articleService.isUserListEmpty(filterPojo));
		Mockito.when(filterPojo.getUserIdList()).thenReturn(userIdList);
		Assert.assertTrue(articleService.isUserListEmpty(filterPojo));
		userIdList.add((long) 1);
		Mockito.when(filterPojo.getUserIdList()).thenReturn(userIdList);
		Assert.assertFalse(articleService.isUserListEmpty(filterPojo));
	}
	
	
	//Test case to test with visibility ALL and and both list empty
	@Test
	void TestGetArticleScenario1() throws ArticleException {

		Page<Article> pro = Mockito.mock(Page.class);

		Pageable pagination = PageRequest.of(AppConstant.DEFAULT_PAGE, AppConstant.TOTAL_RECORDS, Sort.by("creationDate").descending());
		List<Long> userIdList = new ArrayList<>();
		Set<Tag> tagSet = new HashSet<>();

		Mockito.when(filterPojo.getPage()).thenReturn(AppConstant.DEFAULT_PAGE);
		Mockito.when(filterPojo.getTotalPage()).thenReturn(AppConstant.TOTAL_RECORDS);
		Mockito.when(filterPojo.getSortBy()).thenReturn("creationDate");
		Mockito.when(filterPojo.getTagList()).thenReturn(tagSet); 
		Mockito.when(filterPojo.getUserIdList()).thenReturn(userIdList);

		Mockito.doReturn(true).when(Mockito.spy(articleService)).isTagListEmpty(filterPojo);
		Mockito.doReturn(true).when(Mockito.spy(articleService)).isUserListEmpty(filterPojo);
		Mockito.when(articleRepo.findAll(pagination)).thenReturn(pro);

		articleService.getArticle("ALL", filterPojo);
		Mockito.verify(articleRepo, Mockito.times(1)).findAll(pagination);

	}
	
	//Test case to test with visibility ALL , user list empty and tag list non empty 
	@Test
	void TestGetArticleScenario2() throws ArticleException {

		Page<Article> pro = Mockito.mock(Page.class);

		Pageable pagination = PageRequest.of(AppConstant.DEFAULT_PAGE,AppConstant.TOTAL_RECORDS, Sort.by("creationDate").descending());
		List<Long> userIdList = new ArrayList<>();
		Set<Tag> tagSet = new HashSet<>();
		Tag tag=new Tag();
		tag.setId((long)1);
		tag.setTagName("BLOCKCHAIN");
		tagSet.add(tag);
		
		Mockito.when(filterPojo.getPage()).thenReturn(AppConstant.DEFAULT_PAGE);
		Mockito.when(filterPojo.getTotalPage()).thenReturn(AppConstant.TOTAL_RECORDS);
		Mockito.when(filterPojo.getSortBy()).thenReturn("creationDate");
		Mockito.when(filterPojo.getTagList()).thenReturn(tagSet);
		Mockito.when(filterPojo.getUserIdList()).thenReturn(userIdList);

		Mockito.doReturn(false).when(Mockito.spy(articleService)).isTagListEmpty(filterPojo);
		Mockito.doReturn(true).when(Mockito.spy(articleService)).isUserListEmpty(filterPojo);
		Mockito.when(articleRepo.findAllByTagIdIn(tagSet,pagination)).thenReturn(pro);

		articleService.getArticle("ALL", filterPojo);
		Mockito.verify(articleRepo, Mockito.times(1)).findAllByTagIdIn(tagSet,pagination);
 
	}
 
	//Test case to test with visibility ALL , user list non empty and tag list empty 
		@Test
		void TestGetArticleScenario3() throws ArticleException {

			Page<Article> pro = Mockito.mock(Page.class);

			Pageable pagination = PageRequest.of(AppConstant.DEFAULT_PAGE, AppConstant.TOTAL_RECORDS, Sort.by("creationDate").descending());
			List<Long> userIdList = new ArrayList<>();
			Set<Tag> tagSet = new HashSet<>();
			userIdList.add((long)1);
			
			Mockito.when(filterPojo.getPage()).thenReturn(AppConstant.DEFAULT_PAGE);
			Mockito.when(filterPojo.getTotalPage()).thenReturn(AppConstant.TOTAL_RECORDS);
			Mockito.when(filterPojo.getSortBy()).thenReturn("creationDate");
			Mockito.when(filterPojo.getTagList()).thenReturn(tagSet);
			Mockito.when(filterPojo.getUserIdList()).thenReturn(userIdList);

			Mockito.doReturn(false).when(Mockito.spy(articleService)).isTagListEmpty(filterPojo);
			Mockito.doReturn(true).when(Mockito.spy(articleService)).isUserListEmpty(filterPojo);
			Mockito.when(userRepository.findByIdIn(userIdList)).thenReturn(users);
			Mockito.when(articleRepo.findAllByUserIdIn(users,pagination)).thenReturn(pro);

			articleService.getArticle("ALL", filterPojo);
			Mockito.verify(articleRepo, Mockito.times(1)).findAllByUserIdIn(users,pagination);
	 
		}  
		
		// Test case to test with visibility ALL , user list and tag list non empty
		@Test
		void TestGetArticleScenario4() throws ArticleException {

			Page<Article> pro = Mockito.mock(Page.class);

			Pageable pagination = PageRequest.of(AppConstant.DEFAULT_PAGE, AppConstant.TOTAL_RECORDS, Sort.by("creationDate").descending());
			List<Long> userIdList = new ArrayList<>();
			Set<Tag> tagSet = new HashSet<>();
			userIdList.add((long) 1);
			Tag tag=new Tag();
			tag.setId((long)1);
			tag.setTagName("BLOCKCHAIN");
			tagSet.add(tag);

			Mockito.when(filterPojo.getPage()).thenReturn(AppConstant.DEFAULT_PAGE);
			Mockito.when(filterPojo.getTotalPage()).thenReturn(AppConstant.TOTAL_RECORDS);
			Mockito.when(filterPojo.getSortBy()).thenReturn("creationDate");
			Mockito.when(filterPojo.getTagList()).thenReturn(tagSet);
			Mockito.when(filterPojo.getUserIdList()).thenReturn(userIdList);

			Mockito.doReturn(false).when(Mockito.spy(articleService)).isTagListEmpty(filterPojo);
			Mockito.doReturn(true).when(Mockito.spy(articleService)).isUserListEmpty(filterPojo);
			Mockito.when(userRepository.findByIdIn(userIdList)).thenReturn(users);
			Mockito.when(articleRepo.findAllByUserIdInAndTagIdIn(users, tagSet, pagination)).thenReturn(pro);

			articleService.getArticle("ALL", filterPojo);
			Mockito.verify(articleRepo, Mockito.times(1)).findAllByUserIdInAndTagIdIn(users, tagSet, pagination);

		}
		
		//Test case to test with visibility 1 and and both list empty
		@Test
		void TestGetArticleScenario5() throws ArticleException {

			Page<Article> pro = Mockito.mock(Page.class);

			Pageable pagination = PageRequest.of(AppConstant.DEFAULT_PAGE, AppConstant.TOTAL_RECORDS, Sort.by("creationDate").descending());
			List<Long> userIdList = new ArrayList<>();
			Set<Tag> tagSet = new HashSet<>();

			Mockito.when(filterPojo.getPage()).thenReturn(AppConstant.DEFAULT_PAGE);
			Mockito.when(filterPojo.getTotalPage()).thenReturn(AppConstant.TOTAL_RECORDS);
			Mockito.when(filterPojo.getSortBy()).thenReturn("creationDate");
			Mockito.when(filterPojo.getTagList()).thenReturn(tagSet);
			Mockito.when(filterPojo.getUserIdList()).thenReturn(userIdList);

			Mockito.doReturn(true).when(Mockito.spy(articleService)).isTagListEmpty(filterPojo);
			Mockito.doReturn(true).when(Mockito.spy(articleService)).isUserListEmpty(filterPojo);
			Mockito.when(articleRepo.findAllByVisibility(true,pagination)).thenReturn(pro);

			articleService.getArticle("1", filterPojo);
			Mockito.verify(articleRepo, Mockito.times(1)).findAllByVisibility(true,pagination);

		}
	 
		// Test case to test with visibility 1 user list empty and tag list non empty 
		@Test
		void TestGetArticleScenario6() throws ArticleException {

			Page<Article> pro = Mockito.mock(Page.class);

			Pageable pagination = PageRequest.of(AppConstant.DEFAULT_PAGE, AppConstant.TOTAL_RECORDS, Sort.by("creationDate").descending());
			List<Long> userIdList = new ArrayList<>();
			Set<Tag> tagSet = new HashSet<>();
			Tag tag=new Tag();
			tag.setId((long)1);
			tag.setTagName("BLOCKCHAIN");
			tagSet.add(tag);

			Mockito.when(filterPojo.getPage()).thenReturn(AppConstant.DEFAULT_PAGE);
			Mockito.when(filterPojo.getTotalPage()).thenReturn(AppConstant.TOTAL_RECORDS);
			Mockito.when(filterPojo.getSortBy()).thenReturn("creationDate");
			Mockito.when(filterPojo.getTagList()).thenReturn(tagSet);
			Mockito.when(filterPojo.getUserIdList()).thenReturn(userIdList);

			Mockito.doReturn(true).when(Mockito.spy(articleService)).isTagListEmpty(filterPojo);
			Mockito.doReturn(true).when(Mockito.spy(articleService)).isUserListEmpty(filterPojo);
			Mockito.when(articleRepo.findAllByTagIdInAndVisibility(tagSet,true, pagination)).thenReturn(pro);

			
			articleService.getArticle("1", filterPojo);
			Mockito.verify(articleRepo, Mockito.times(1)).findAllByTagIdInAndVisibility(tagSet,true, pagination);

		}
		
		// Test case to test with visibility 1 user list non empty and tag list empty.
		@Test
		void TestGetArticleScenario7() throws ArticleException {

			Page<Article> pro = Mockito.mock(Page.class);

			Pageable pagination = PageRequest.of(AppConstant.DEFAULT_PAGE, AppConstant.TOTAL_RECORDS, Sort.by("creationDate").descending());
			List<Long> userIdList = new ArrayList<>();
			Set<Tag> tagSet = new HashSet<>();
			userIdList.add((long) 1);

			Mockito.when(filterPojo.getPage()).thenReturn(AppConstant.DEFAULT_PAGE);
			Mockito.when(filterPojo.getTotalPage()).thenReturn(AppConstant.TOTAL_RECORDS);
			Mockito.when(filterPojo.getSortBy()).thenReturn("creationDate");
			Mockito.when(filterPojo.getTagList()).thenReturn(tagSet);
			Mockito.when(filterPojo.getUserIdList()).thenReturn(userIdList);

			Mockito.doReturn(true).when(Mockito.spy(articleService)).isTagListEmpty(filterPojo);
			Mockito.doReturn(true).when(Mockito.spy(articleService)).isUserListEmpty(filterPojo);
			Mockito.when(userRepository.findByIdIn(userIdList)).thenReturn(users);
			Mockito.when(articleRepo.findAllByUserIdInAndVisibility(users, true, pagination)).thenReturn(pro);

			articleService.getArticle("1", filterPojo);
			Mockito.verify(articleRepo, Mockito.times(1)).findAllByUserIdInAndVisibility(users, true, pagination);
		}
		

		// Test case to test with visibility 1(public) , user list and tag list non empty
		@Test
		void TestGetArticleScenario8() throws ArticleException { 

			Page<Article> pro = Mockito.mock(Page.class);

			Pageable pagination = PageRequest.of(AppConstant.DEFAULT_PAGE, AppConstant.TOTAL_RECORDS, Sort.by("creationDate").descending());
			List<Long> userIdList = new ArrayList<>();
			Set<Tag> tagSet = new HashSet<>();
			userIdList.add((long) 1);
			Tag tag = new Tag();
			tag.setId((long) 1);
			tag.setTagName("BLOCKCHAIN");
			tagSet.add(tag);

			Mockito.when(filterPojo.getPage()).thenReturn(AppConstant.DEFAULT_PAGE);
			Mockito.when(filterPojo.getTotalPage()).thenReturn(AppConstant.TOTAL_RECORDS);
			Mockito.when(filterPojo.getSortBy()).thenReturn("creationDate");
			Mockito.when(filterPojo.getTagList()).thenReturn(tagSet);
			Mockito.when(filterPojo.getUserIdList()).thenReturn(userIdList);

			Mockito.doReturn(false).when(Mockito.spy(articleService)).isTagListEmpty(filterPojo);
			Mockito.doReturn(true).when(Mockito.spy(articleService)).isUserListEmpty(filterPojo);
			Mockito.when(userRepository.findByIdIn(userIdList)).thenReturn(users);
			Mockito.when(articleRepo.findAllByUserIdInAndTagIdInAndVisibility(users, tagSet, true, pagination))
					.thenReturn(pro);

			articleService.getArticle("1", filterPojo);
			Mockito.verify(articleRepo, Mockito.times(1)).findAllByUserIdInAndTagIdInAndVisibility(users, tagSet, true,
					pagination);

		}
		 
		@Test
		void testGetArticleByUser() {
			Page<Article> articleList = Mockito.mock(Page.class);
			Pageable pagination = PageRequest.of(AppConstant.DEFAULT_PAGE, AppConstant.TOTAL_RECORDS, Sort.by("creationDate"));
			Mockito.when(userRepository.findByUserName("vivek")).thenReturn(user);
			Mockito.when(articleRepo.findByUserId(user, pagination)).thenReturn(articleList);
			articleService.getArticleByUser("vivek", AppConstant.DEFAULT_PAGE, AppConstant.TOTAL_RECORDS);
			Mockito.verify(articleRepo, Mockito.times(1)).findByUserId(user, pagination);
		}
		
		@Test
		void testGetArticleById() {
			
			
			Mockito.when(articleRepo.findById((long)1)).thenReturn(articleOptional);
			Mockito.when(articleOptional.isPresent()).thenReturn(true);
			Mockito.when(articleOptional.get()).thenReturn(article);
			
			Assert.assertEquals(article,articleService.getArticleById((long)1));
			Mockito.when(articleOptional.isPresent()).thenReturn(false);
			Assert.assertEquals(null,articleService.getArticleById((long)1));
			
		}
		
		
	@Test
	public void TestNullGetTwitterCountOfArticleTags(){

		//Arrange 
		Long id = null;
		List<Map<String,Object>> emptyList = new ArrayList<Map<String,Object>>();

		//Act
		emptyList = articleServiceImpl.getTwitterCountOfArticleTags(id);

		//Assert
		Assert.assertTrue(emptyList.isEmpty());
	}

	/*
		To test that the id is not present in the system
	 */
	@Test
	public void TestNegativeGetTwitterCountOfArticleTags(){

		//Arrange
		Long id = Long.valueOf(0);
		List<Map<String,Object>> emptyList = new ArrayList<Map<String,Object>>();

		//Act
		emptyList = articleServiceImpl.getTwitterCountOfArticleTags(id);

		//Assert
		Assert.assertTrue(emptyList.isEmpty());
	}

	@Test
	public void TestPositiveGetTwitterCountOfArticleTags(){

		//Arrange
		Long id = Long.valueOf(1);
		Article article = new Article();
		Set<Tag> tags = new HashSet<>();
		Tag tag = new Tag();
		List<Map<String,Object>> tweetList = new ArrayList<Map<String,Object>>();

		//Act
		tag.setId(Long.valueOf(1));
		tag.setTagName("Web"); 
		tags.add(tag);
		article.setTagId(tags);
		Mockito.when(articleRepo.findById(id)).thenReturn(Optional.of(article));
		tweetList = articleServiceImpl.getTwitterCountOfArticleTags(id);
		int actualCount = tweetList.size();

		//Assert
		Assert.assertEquals(AppConstant.TEST_EXP_COUNT_SIZE,actualCount);
	}

	/*
	 Test to check if the twitter instance is created
	 */
	@Test
	public void TestTwitterAuthentication(){
		//Arrange
		Twitter twitter;

		//Act
		twitter	= ArticleServiceImpl.authentication();

		//Assert
		Assert.assertNotNull(twitter);
	}
}   

   
   
  

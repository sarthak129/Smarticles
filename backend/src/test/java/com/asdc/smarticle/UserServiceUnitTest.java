package com.asdc.smarticle;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.Assert;
import org.mockito.Mock;
import org.mockito.Mockito;
import com.asdc.smarticle.article.Article;
import com.asdc.smarticle.article.ArticleRepository;
import com.asdc.smarticle.articletag.Tag;
import com.asdc.smarticle.articletag.TagRepository;
import com.asdc.smarticle.pswdencrydecry.CipherConfigFactory;
import com.asdc.smarticle.token.Token;
import com.asdc.smarticle.token.TokenRepository;
import com.asdc.smarticle.token.TokenService;
import com.asdc.smarticle.user.PooledPBEStringFactory;
import com.asdc.smarticle.user.User;
import com.asdc.smarticle.user.UserRepository;
import com.asdc.smarticle.user.UserService;
import com.asdc.smarticle.user.userVo.UserProfileRequestVo;
import com.asdc.smarticle.user.userVo.UserProfileRespVo;
import com.asdc.smarticle.user.userVo.UserProfileRespVoFactory;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceUnitTest {

	/*
	 * @InjectMocks private UserServiceImpl userService;// system under test
	 */

	@Autowired
	private UserService userService;// system under test

	@MockBean
	private UserRepository userRepo;// Dependency is mocked.

	@MockBean
	private TokenRepository tokenRepository;

	@MockBean
	private ArticleRepository articleRepository;

	@MockBean
	TagRepository tagRepository;

	@MockBean
	private TokenService tokenService;

	@MockBean
	private User user;

	@MockBean
	PooledPBEStringFactory pooledPBEStringFactory;

	@MockBean
	private CipherConfigFactory cipherConfigFactory;

	@MockBean
	private SimpleStringPBEConfig simpleStringPBEConfig;

	@MockBean
	PooledPBEStringEncryptor pooledPBEStringEncryptor;

	@MockBean
	private Token token;

	@MockBean
	private UserProfileRespVo userProfileRespVo;

	@MockBean
	private UserProfileRequestVo userProfileRequestVo;

	@MockBean
	private UserProfileRespVoFactory userProfileRespVoFactory;

	@Mock
	private List<User> users;

	@Test
	void testIsEmailIdRegistered() {

		String emailIDString = "patelvivek221996@gmail.com";

		Optional<User> userEntity = Optional.of(user);

		Mockito.when(userRepo.findByEmailID(emailIDString)).thenReturn(userEntity);

		Assert.assertTrue(userService.isEmailIdRegistered(emailIDString));

		Mockito.when(userRepo.findByEmailID(emailIDString)).thenReturn(Optional.empty());

		Assert.assertFalse(userService.isEmailIdRegistered(emailIDString));
	}

	@Test 
	void testIsUsernameRegistered() {

		String userName = "vkpatel4312";  
 
		User userEntity = user;

		Mockito.when(userRepo.findByUserName(userName)).thenReturn(userEntity);

		Assert.assertTrue(userService.isUsernameRegistered(userName));

		Mockito.when(userRepo.findByUserName(userName)).thenReturn(null);

		Assert.assertFalse(userService.isUsernameRegistered(userName));
	}

	@Test
	void testVerifyUserInvalidData() {
		Long id = (long) 1;

		// Test null token string
		String tokenString = null;
		Assert.assertFalse(userService.verifyUser(tokenString));

		// Test empty token string
		tokenString = "";
		Assert.assertFalse(userService.verifyUser(tokenString));

		// Test token which does not exist
		Mockito.when(tokenRepository.findByToken(tokenString)).thenReturn(null);
		Assert.assertFalse(userService.verifyUser(tokenString));

		// Test expired token
		Mockito.when(tokenService.isTokenExpired(token)).thenReturn(true);
		Assert.assertFalse(userService.verifyUser(tokenString));

		// Test token for which user does not exist
		Mockito.when(userRepo.findById(id)).thenReturn(Optional.empty());
		Assert.assertFalse(userService.verifyUser(tokenString));

		Optional.of(user);

	}

	@Test
	void testVerifyUserValidData() {

		Long id = (long) 1;
		Optional.of(user);

		Token tokenDetailToken = new Token();
		User userDetail = new User();
		Optional<User> userDetailOptional = Optional.of(userDetail);

		String tokenString = "1b132cda7a92ad0f";
		Mockito.when(tokenRepository.findByToken(tokenString)).thenReturn(tokenDetailToken);
		Mockito.when(tokenService.isTokenExpired(token)).thenReturn(false);
		Mockito.when(userRepo.findById(id)).thenReturn(userDetailOptional);

		Mockito.when(token.getUser()).thenReturn(userDetail);
		Mockito.when(user.getId()).thenReturn(id);
		/*
		 * // Mockito.when(userEntity.get()).thenReturn(userDetailUser);
		 * Assert.assertTrue(userService.verifyUser(tokenString));
		 * 
		 * Mockito.verify(user).setVerified(true);
		 * 
		 * // Mockito.verify(userRepo).save(userDetail);
		 * Mockito.verify(tokenService).deleteToken(tokenDetailToken);
		 */
	}

	@Test
	void testEncodePaswd() {

		Mockito.when(cipherConfigFactory.getCipherConfigInstance()).thenReturn(simpleStringPBEConfig);
		Mockito.when(pooledPBEStringFactory.getPBEStrinInstance()).thenReturn(pooledPBEStringEncryptor);
		pooledPBEStringEncryptor.setConfig(simpleStringPBEConfig);
		Mockito.when(pooledPBEStringEncryptor.encrypt("123456")).thenReturn("abcdefg");
		Assert.assertEquals("abcdefg", userService.encodePswd("123456"));
	}

	/*
	 * This test cases test a method of user service to retrieve all posted articles
	 */
	@Test
	void TestPostedArticle() {

		List<Article> articleList = new ArrayList();
		List<Map<String, Object>> userDetails = new ArrayList<>();
		Mockito.when(articleRepository.findAll()).thenReturn(articleList);
		Assert.assertEquals(userDetails, userService.getUsersPostedArticle());

		User user = new User();
		user.setFirstName("vivek");
		user.setLastName("patel");
		user.setUserName("vkpatel4312");
		user.setId((long) 1);
		Article article = new Article();
		article.setUserId(user);
		articleList.add(article);
		Mockito.when(articleRepository.findAll()).thenReturn(articleList);
		Map<String, Object> details = new HashMap<>();
		details.put("firstName", article.getUserId().getFirstName());
		details.put("lastName", article.getUserId().getLastName());
		details.put("userName", article.getUserId().getUserName());
		details.put("id", article.getUserId().getId());
		userDetails.add(details);

		Assert.assertEquals(userDetails, userService.getUsersPostedArticle());

	}

	@Test
	void testSavePrefTag() {
		Tag tag = new Tag();
		tag.setId((long) 1);
		tag.setTagName("BLOCKCHAIN");

		List<Tag> tagList = new ArrayList();
		tagList.add(tag);

		Set<Tag> tagSet = new HashSet();
		tagSet.add(tag);
		List<Long> ids = new ArrayList<>();
		for (Tag set : tagSet) {
			ids.add(tag.getId());
		}

		Mockito.when(tagRepository.findByIdIn(ids)).thenReturn(tagList);
		Mockito.when(userRepo.findByUserName("vivek")).thenReturn(user);
		Mockito.when(userRepo.save(user)).thenReturn(user);
		Assert.assertEquals(user, userService.saveUserPrefTags("vivek", tagSet));

		user.setTags(tagSet);

	}

	@Test
	void testAddJwtToken() {
		Mockito.when(userRepo.findByUserName("sarthak")).thenReturn(user);
		user.setJwtToken("token");
		Mockito.when(userRepo.save(user)).thenReturn(user);
		userService.addJwtToken("sarthak", "token");

		Mockito.when(userRepo.findByUserName("sarthak")).thenReturn(null);
		userService.addJwtToken("sarthak", "token");
	}

	@Test
	void testRemoveJwtToken() {
		Mockito.when(userRepo.findByJwtToken("token")).thenReturn(user);
		user.setJwtToken("");
		Mockito.when(userRepo.save(user)).thenReturn(user);
		userService.removeJwtToken("token");

		Mockito.when(userRepo.findByJwtToken("token")).thenReturn(null);
		userService.removeJwtToken("token");
	}

	@Test
	void testGetUserByEmailID() {
		String emailIDString = "sarthak@gmail.com";

		Optional<User> userEntity = Optional.of(user);

		Mockito.when(userRepo.findByEmailID(emailIDString)).thenReturn(userEntity);

		Assert.assertEquals(user, userService.getUserByEmailID(emailIDString));

		Mockito.when(userRepo.findByEmailID(emailIDString)).thenReturn(Optional.empty());

		Assert.assertNull(userService.getUserByEmailID(emailIDString));
	}

	@Test
	void testUpdateUserPassword() {
		// Mockito.doReturn(user).when(userRepo.findByUserName("sarthak"));
		Mockito.when(cipherConfigFactory.getCipherConfigInstance()).thenReturn(simpleStringPBEConfig);
		Mockito.when(pooledPBEStringFactory.getPBEStrinInstance()).thenReturn(pooledPBEStringEncryptor);
		pooledPBEStringEncryptor.setConfig(simpleStringPBEConfig);
		Mockito.when(pooledPBEStringEncryptor.encrypt("123456")).thenReturn("abcdefg");
		Mockito.when(userRepo.findByUserName("sarthak")).thenReturn(user);
		user.setPswd("abcdefg");
		Mockito.when(userRepo.save(user)).thenReturn(user);
		Assert.assertEquals(user, userService.updateUserPassword("sarthak", "123456"));
	}

	@Test
	void testGetUserByUserName() {
		Mockito.when(userRepo.findByUserName("sarthakkkkk")).thenReturn(user);
		Assert.assertEquals(userService.getUserByUserName("sarthakkkkk"), user);
	}

	@Test
	void testGetUserDetailsWhenUser() {
		Mockito.when(userProfileRespVoFactory.getUserProfileRespVoInstance()).thenReturn(userProfileRespVo);
		Mockito.when(userRepo.findByUserName("sar")).thenReturn(user);
		Mockito.when(user.getEmailID()).thenReturn("sarthak@wobot.ai");
		Mockito.when(user.getFirstName()).thenReturn("Sarthak");
		Mockito.when(user.getLastName()).thenReturn("Patel");
		Mockito.when(user.getUserName()).thenReturn("sarthakp");
		userProfileRespVo.setEmailID(user.getEmailID());
		userProfileRespVo.setFirstName(user.getFirstName());
		userProfileRespVo.setLastName(user.getLastName());
		userProfileRespVo.setUserName(user.getUserName());
		Mockito.verify(userProfileRespVo, Mockito.times(1)).setEmailID(user.getEmailID());
		Mockito.verify(userProfileRespVo, Mockito.times(1)).setFirstName(user.getFirstName());
		Mockito.verify(userProfileRespVo, Mockito.times(1)).setLastName(user.getLastName());
		Mockito.verify(userProfileRespVo, Mockito.times(1)).setUserName(user.getUserName());
		Assert.assertEquals(userProfileRespVo, userService.getUserDetails("sar", null));
	}

	@Test
	void testGetUserDetailsWhenUserNull() {
		Mockito.when(userProfileRespVoFactory.getUserProfileRespVoInstance()).thenReturn(userProfileRespVo);

		Mockito.when(userRepo.findByUserName("sar")).thenReturn(null);
		Assert.assertEquals(userProfileRespVo, userService.getUserDetails("sar", null));

	}

	@Test
	void testUpdateUserProfile() {
		Mockito.when(userRepo.findByUserName(userProfileRequestVo.getUserName())).thenReturn(user);
		Mockito.when(userRepo.save(user)).thenReturn(user);
		Assert.assertEquals(user, userService.updateUserProfile(userProfileRequestVo));

		Mockito.when(userRepo.findByUserName(userProfileRequestVo.getUserName())).thenReturn(null);
		Assert.assertEquals(null, userService.updateUserProfile(userProfileRequestVo));
	}

	@Test
	void testGetUserList() {
		List<Long> userId = new ArrayList<Long>();
		userId.add(1L);
		Mockito.when(userRepo.findByIdIn(userId)).thenReturn(users);
		Assert.assertEquals(users, userService.getUserList(userId));
	}

	@Test
	void testIsUserVerified() {
		String userName = "sarthak";
		Mockito.when(userRepo.findByUserName(userName)).thenReturn(user);
		Mockito.when(user.isVerified()).thenReturn(true);
		Assert.assertEquals("verified", userService.isUserVerified(userName));

		Mockito.when(userRepo.findByUserName(userName)).thenReturn(null);
		Mockito.when(user.isVerified()).thenReturn(true);
		Assert.assertEquals("User does not exist", userService.isUserVerified(userName));

		Mockito.when(userRepo.findByUserName(userName)).thenReturn(user);
		Mockito.when(user.isVerified()).thenReturn(false);
		Assert.assertEquals("Account not yet verified. Please verify it and try again.",
				userService.isUserVerified(userName));

	}

}

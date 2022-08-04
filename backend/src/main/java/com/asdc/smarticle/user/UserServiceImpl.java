package com.asdc.smarticle.user;

import java.util.*;
import java.util.stream.Collectors;

import com.asdc.smarticle.article.Article;
import com.asdc.smarticle.article.ArticleRepository;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.asdc.smarticle.articletag.Tag;
import com.asdc.smarticle.articletag.TagRepository;
import com.asdc.smarticle.comutil.ApiError;
import com.asdc.smarticle.pswdencrydecry.CipherConfig;
import com.asdc.smarticle.token.Token;
import com.asdc.smarticle.token.TokenRepository;
import com.asdc.smarticle.token.TokenService;
import com.asdc.smarticle.user.exception.UserExistException;
import com.asdc.smarticle.user.userVo.UserProfileRequestVo;
import com.asdc.smarticle.user.userVo.UserProfileRespVo;
import com.asdc.smarticle.user.userVo.UserProfileRespVoFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Services for user entity.
 *
 * @author Vivekkumar Patel
 * @version 1.0
 * @since 2022-02-19
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	CipherConfig cipherCf;

	@Autowired
	TokenRepository tokenRepository;

	@Autowired
	TokenService tokenService;

	@Autowired
	TagRepository tagRepository;

	@Autowired
	ArticleRepository articleRepository;

	@Autowired
	PooledPBEStringFactory pooledPBEStringFactory;

	@Autowired
	UserProfileRespVoFactory userProfileRespVoFactory;

	/**
	 * Verify if the email is registered .
	 *
	 * @param email request header
	 * @return true if email is registered.
	 */
	@Override
	public boolean isEmailIdRegistered(String email) {

		boolean userExist = false;

		Optional<User> user = userRepository.findByEmailID(email);

		if (user.isPresent()) {
			userExist = true;
		}

		return userExist;
	}

	/**
	 * Verify if the username is registered .
	 *
	 * @param userName request header
	 * @return true if username is registered.
	 */
	@Override
	public boolean isUsernameRegistered(String userName) {

		boolean userNameTaken = false;

		User user = userRepository.findByUserName(userName);
		if (user != null) {
			userNameTaken = true;
		}

		return userNameTaken;
	}

	/**
	 * Verify if the user is verified .
	 *
	 * @param userName request header
	 * @return true if user is verified.
	 */
	@Override
	public String isUserVerified(String userName) {
		String status = "";
		User user = userRepository.findByUserName(userName);
		if (user != null && user.isVerified())
			status = "verified";
		else if (user == null) {
			status = "User does not exist";
		} else {
			status = "Account not yet verified. Please verify it and try again.";
		}
		return status;

	}

	/**
	 * Register new users .
	 *
	 * @param user User object
	 * @return User object which is registered.
	 */
	@Override
	public User registerUser(User user) throws UserExistException {

		if (isEmailIdRegistered(user.getEmailID())) {
			throw new UserExistException(ApiError.EMAILID_ALREADY_REGISTERED);
		}

		if (isUsernameRegistered(user.getUserName())) {
			throw new UserExistException(ApiError.USERNAME_NOT_AVAILABLE);
		}

		String encPswd = encodePswd(user.getPswd());
		user.setPswd(encPswd);

		return userRepository.save(user);

	}

	/**
	 * Encode password for security purpose .
	 *
	 * @param pswd Password
	 * @return encrypted password.
	 */
	@Override
	public String encodePswd(String pswd) {

		SimpleStringPBEConfig config = cipherCf.getCipherConfig();
		PooledPBEStringEncryptor cipher = pooledPBEStringFactory.getPBEStrinInstance();
		cipher.setConfig(config);

		return cipher.encrypt(pswd);

	}

	/**
	 * Check if the user is verified .
	 *
	 * @param token token to verify if the session is activated
	 * @return true if user is verified.
	 */
	@Override
	public boolean verifyUser(String token) {

		boolean isAccountActivated = true;

		if (token == null || token.equals("")) {
			isAccountActivated = false;
		}

		Token tokenDetail = tokenRepository.findByToken(token);
		if (tokenDetail == null || tokenService.isTokenExpired(tokenDetail)) {
			isAccountActivated = false;
		} else {

			Optional<User> user = userRepository.findById(tokenDetail.getUser().getId());

			if (user.isPresent()) {
				user.get().setVerified(true);
				userRepository.save(user.get());
				tokenService.deleteToken(tokenDetail);
			} else {
				isAccountActivated = false;
			}
		}

		return isAccountActivated;
	}

	/**
	 * Set the token for session authentication .
	 *
	 * @param username username of the user
	 * @param value token value
	 */
	@Override
	public void addJwtToken(String username, String value) {
		User user = userRepository.findByUserName(username);
		if (user != null) {
			user.setJwtToken(value);
			userRepository.save(user);
		}
	}

	/**
	 * Check if the user is verified .
	 *
	 * @param value token to verify if the session is activated
	 */
	@Override
	public void removeJwtToken(String value) {
		User user = userRepository.findByJwtToken(value);
		if (user != null) {
			user.setJwtToken("");
			userRepository.save(user);
		}
	}

	/**
	 * Fetch user by their email id .
	 *
	 * @param emailID Email id of the user
	 * @return User object.
	 */
	@Override
	public User getUserByEmailID(String emailID) {
		Optional<User> user = userRepository.findByEmailID(emailID);
		if (user.isPresent()) {
			return user.get();
		}
		return null;
	}

	/**
	 * Update user's password .
	 *
	 * @param userName username of the user
	 * @param password password of the user
	 * @return User object with updated password
	 */
	@Override
	public User updateUserPassword(String userName, String password) {
		User user = userRepository.findByUserName(userName);
		//System.out.println("User pass - " + user.getUserName() + user.getPswd());
		user.setPswd(encodePswd(password));
		userRepository.save(user);
		return user;
	}

	/**
	 * Fetch user by their username.
	 *
	 * @param userName Username of the user
	 * @return User object.
	 */
	@Override
	public User getUserByUserName(String userName) {
		User user = userRepository.findByUserName(userName);
		return user;
	}

	/**
	 * Save tags as per users preference.
	 *
	 * @param userName Username of the user
	 * @param tagIdList ids of the tags
	 * @return User object.
	 */
	@Override
	public User saveUserPrefTags(String userName, Set<Tag> tagIdList) {
		List<Long> ids = new ArrayList<>();
		for (Tag tag : tagIdList) {
			ids.add(tag.getId());
		}
		List<Tag> tagList = tagRepository.findByIdIn(ids);

		User user = userRepository.findByUserName(userName);
		user.setTags(tagList.stream().collect(Collectors.toSet()));
		user = userRepository.save(user);

		return user;
	}

	/**
	 * @author Vivekkumar Patel Get user details such as firstname,lastname,username
	 *         etc
	 * @param 'username' whose details to be retrieved and object mapper to map
	 *                   reuqestVO to dto.
	 * @return UserProfileRespVo containing userdetails
	 */
	@Override
	public UserProfileRespVo getUserDetails(String userName, ObjectMapper mapper) {

		UserProfileRespVo userProfileRespVo = userProfileRespVoFactory.getUserProfileRespVoInstance();
		User user = userRepository.findByUserName(userName);

		if (user != null) {
			//userProfileRespVo = new UserProfileRespVo();
			userProfileRespVo.setEmailID(user.getEmailID());
			userProfileRespVo.setFirstName(user.getFirstName());
			userProfileRespVo.setLastName(user.getLastName());
			userProfileRespVo.setUserName(user.getUserName());
		}
		return userProfileRespVo;
	}

	/**
	 * @author Vivekkumar Patel Update user details such as
	 *         firstname,lastname,username etc
	 * @param userProfileRespVo model containing user details to be updated.
	 * @return User model containing userdetails
	 */
	@Override
	public User updateUserProfile(UserProfileRequestVo userProfileRespVo) {

		User user = userRepository.findByUserName(userProfileRespVo.getUserName());

		if (user != null) {
			user.setEmailID(userProfileRespVo.getEmailID());
			user.setFirstName(userProfileRespVo.getFirstName());
			user.setLastName(userProfileRespVo.getLastName());
			user.setUserName(userProfileRespVo.getUserName());
			return userRepository.save(user);
		}

		return null;

	}

	/**
	 * Fetch the Articles posted by the user.
	 *
	 * @return Details of the articles posted by the user.
	 */
	@Override
	public List<Map<String, Object>> getUsersPostedArticle() {
		List<Map<String, Object>> userDetails = new ArrayList<>();
		Map<String, Object> details = new HashMap<>();
		List<Article> articleList = articleRepository.findAll();
		if (articleList.isEmpty()) {
			return new ArrayList<>();
		}
		for (Article article : articleList) {
			details = new HashMap<>();
			details.put("firstName", article.getUserId().getFirstName());
			details.put("lastName", article.getUserId().getLastName());
			details.put("userName", article.getUserId().getUserName());
			details.put("id", article.getUserId().getId());
			userDetails.add(details);
		}

		userDetails = userDetails.stream().distinct().collect(Collectors.toList());
		return userDetails;

	}

	/**
	 * Fetch the list of the users.
	 * @param userId user ID of the user
	 * @return List of user objects
	 */
	@Override
	public List<User> getUserList(List<Long> userId) {

		List<User> users = userRepository.findByIdIn(userId);

		return users;
	}
}

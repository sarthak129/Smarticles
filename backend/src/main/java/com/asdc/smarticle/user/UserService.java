package com.asdc.smarticle.user;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.core.metrics.StartupStep.Tags;

import com.asdc.smarticle.articletag.Tag;
import com.asdc.smarticle.user.exception.UserExistException;
import com.asdc.smarticle.user.userVo.UserProfileRequestVo;
import com.asdc.smarticle.user.userVo.UserProfileRespVo;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Services for user entity.
 * 
 * @author Vivekkumar Patel,Sarthak Patel
 * @version 1.0
 * @since 2022-02-19
 */
public interface UserService {

	boolean isEmailIdRegistered(String email);

	boolean isUsernameRegistered(String userName);

	User registerUser(User user) throws UserExistException;

	String encodePswd(String pswd);

	boolean verifyUser(String token);

	void addJwtToken(String username, String value);

	void removeJwtToken(String value);

	User getUserByEmailID(String emailID);

	User updateUserPassword(String userName, String password);

	User getUserByUserName(String username);

	UserProfileRespVo getUserDetails(String userName, ObjectMapper mapper);

	User updateUserProfile(UserProfileRequestVo userProfileRespVo);
	
	User saveUserPrefTags(String userName, Set<Tag> tagIdList);

	List<Map<String, Object>> getUsersPostedArticle();
	
	List<User> getUserList(List<Long> userId);

	String isUserVerified(String userName);
	
}

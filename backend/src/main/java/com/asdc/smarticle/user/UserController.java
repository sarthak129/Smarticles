package com.asdc.smarticle.user;

import java.util.*;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.asdc.smarticle.articletag.Tag;
import com.asdc.smarticle.comutil.ApplicationUrlPath;
import com.asdc.smarticle.httpresponse.BaseController;
import com.asdc.smarticle.httpresponse.ResponseVO;
import com.asdc.smarticle.mailing.EmailService;
import com.asdc.smarticle.pswdencrydecry.CipherConfig;
import com.asdc.smarticle.security.JwtUtils;
import com.asdc.smarticle.token.Token;
import com.asdc.smarticle.token.TokenService;
import com.asdc.smarticle.user.exception.UserExistException;
import com.asdc.smarticle.user.userVo.UserProfileRequestVo;
import com.asdc.smarticle.user.userVo.UserProfileRespVo;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Vivekkumar Patel, Sarthak Patel
 * @version 1.0
 * @since 2022-02-19
 */
@CrossOrigin
@RestController
@RequestMapping("/smarticleapi/user")
public class UserController extends BaseController {

	@Autowired
	EmailService emailServiceImpl;

	@Autowired
	UserService userService;

	@Autowired
	TokenService tokenService;

	@Autowired
	CipherConfig cipherCf;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtUtils jwtUtils;

	@Value("${enc.key}")
	private String key;

	/**
	 * Create user account with the given credentials.
	 *
	 * // * @param ##sser model containing user details.
	 *
	 * @return the response entity
	 * @throws UserExistException If the user is registered with the given email id.
	 */
	@PostMapping(ApplicationUrlPath.USER_REGISTER_REQ_PATH)
	public ResponseVO<String> registerUser(@RequestBody User user) {

		try {
			userService.registerUser(user);
			Token token = tokenService.createToken(user);
			emailServiceImpl.sendConfirmationEmail(user, token);
		} catch (UserExistException e) {

			return error(HttpStatus.CONFLICT.value(), e.getMessage(), false);
		} catch (MessagingException e) {
			return error(HttpStatus.CONFLICT.value(), e.getMessage(), false);
		}

		return success(HttpStatus.OK.value(), HttpStatus.OK.name(), true);
	}

	/**
	 * Activate the user account .
	 *
	 * @param token token string to be verified for user account activation.
	 * @return true if user account is activated else false.
	 */
	@PostMapping(ApplicationUrlPath.USER_ACCOUNT_ACTIVATION_REQ_PATH)
	public ResponseVO<String> activateUserAccount(@RequestParam String token) {

		userService.verifyUser(token);

		return success(HttpStatus.OK.value(), HttpStatus.OK.name(), true);
	}

	/**
	 * Authenticate the user .
	 *
	 * @param userRequest user object passed as request body
	 * @return true if user is authenticated.
	 */
	@PostMapping(ApplicationUrlPath.USER_LOGIN)
	public ResponseVO<Object> authenticateUser(@RequestBody User userRequest) {
		try {
			String isVerified = userService.isUserVerified(userRequest.getUserName());
			if (isVerified.equalsIgnoreCase("verified")) {
				Authentication authentication = authenticationManager.authenticate(
						new UsernamePasswordAuthenticationToken(userRequest.getUserName(), userRequest.getPswd()));
				SecurityContextHolder.getContext().setAuthentication(authentication);
				UserDetails userDetails = (UserDetails) authentication.getPrincipal();
				ResponseCookie jwtCookie = jwtUtils.generateJwtTokenCookie(userDetails.getUsername());
				userService.addJwtToken(userDetails.getUsername(), jwtCookie.getValue());
				User user = userService.getUserByUserName(userDetails.getUsername());
				HashMap<String, String> data = new HashMap<String, String>();
				data.put("userName", user.getUserName());
				data.put("firstName", user.getFirstName());
				data.put("lastName", user.getLastName());
				data.put("jwt-token", jwtCookie.getValue());
				data.put("msg", "User Logged in sucessfully");
				return prepareSuccessResponse(data);
			}else {
				return error(HttpStatus.UNPROCESSABLE_ENTITY.value(), isVerified, false);
			}

		} catch (Exception e) {
			return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), false);
		}
	}

	/**
	 * Logout the user .
	 *
	 * @return true if user is logged out.
	 */
	@PostMapping(ApplicationUrlPath.USER_LOGOUT)
	public ResponseVO<String> logoutUser() {
		try {
			ResponseCookie cookie = jwtUtils.getCleanJwtTokenCookie();
			userService.removeJwtToken(cookie.getValue());
			System.out
					.println("jwtUtils - " + cookie.getValue() + " - " + cookie.getMaxAge() + " - " + cookie.getName());
			return success(HttpStatus.OK.value(), HttpStatus.OK.name(), true);
		} catch (Exception e) {
			return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), false);
		}
	}

	/**
	 * Send email when user forgets the password .
	 *
	 * @param userRequest user object passed as request body
	 * @return true if user is logged out.
	 */
	@PostMapping(ApplicationUrlPath.USER_FORGOT_PASSWORD)
	public ResponseVO<String> forgotPassword(@RequestBody User userRequest) {
		try {
			User user = userService.getUserByEmailID(userRequest.getEmailID());
			if (user != null) {
				ResponseCookie jwtCookie = jwtUtils.generateJwtTokenCookie(user.getUserName());
				emailServiceImpl.sendForgotPasswordEmail(user, jwtCookie);
				return success(HttpStatus.OK.value(), HttpStatus.OK.name(), true);
			} else {
				return error(HttpStatus.UNPROCESSABLE_ENTITY.value(), "No registered emailID found.", false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), false);
		}
	}

	/**
	 * Updates users password .
	 *
	 * @param http request header
	 * @param uservo user object passed as request body
	 * @return true if password is updated.
	 */
	@PostMapping(ApplicationUrlPath.SET_PASSWORD_PATH)
	public ResponseVO<String> resetPassword(@RequestHeader HttpHeaders http, @RequestBody User uservo) {
		try {

			String jwtToken = http.getFirst("jwt-token");
			if (!jwtToken.isEmpty()) {
				String userName = jwtUtils.getUserNameFromJwt(jwtToken);
				System.out.println(("userName " + userName + "  " + uservo.getPswd()));
				User user = userService.updateUserPassword(userName, uservo.getPswd());
				if (user != null) {
					return success(HttpStatus.OK.value(), HttpStatus.OK.name(), true);
				}
			}
		} catch (Exception e) {
			return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), false);
		}
		return success(HttpStatus.OK.value(), HttpStatus.OK.name(), true);
	}

	/**
	 * Save the user tags .
	 *
	 * @param http request header
	 * @param tagList list of tags
	 * @return true if user tags are saved.
	 */
	@PostMapping(ApplicationUrlPath.SAVE_USER_TAG_PREFERENCE)
	public ResponseVO<String> saveUserTag(@RequestHeader HttpHeaders http, @RequestBody Set<Tag> tagList) {
		try {
			System.out.println(tagList);
			String jwtToken = http.getFirst("jwt-token");
			if (!jwtToken.isEmpty()) {
				String userName = jwtUtils.getUserNameFromJwt(jwtToken);
				User user = userService.saveUserPrefTags(userName, tagList);
				if (user != null) {
					return success(HttpStatus.OK.value(), HttpStatus.OK.name(), true);
				}
			}
		} catch (Exception e) {
			return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), false);
		}
		return success(HttpStatus.OK.value(), HttpStatus.OK.name(), true);
	}

	/**
	 * @author Vivekkumar Patel Get user details such as firstname,lastname,username
	 *         etc
	 * @param http header containing jet token to validate the user.
	 * @return UserProfileRespVo userdetails
	 */
	@GetMapping(ApplicationUrlPath.GET_USER_PROFILE)
	public ResponseVO<UserProfileRespVo> getUserProfile(@RequestHeader HttpHeaders http) {
		UserProfileRespVo userProfileRespVo = null;
		try {
			String jwtToken = http.getFirst("jwt-token");
			if (!jwtToken.isEmpty()) {
				String userName = jwtUtils.getUserNameFromJwt(jwtToken);

				userProfileRespVo = userService.getUserDetails(userName, new ObjectMapper());
				if (userProfileRespVo == null) {
					return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
							false);
				}
			}
		} catch (Exception e) {
			return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), false);
		}
		return prepareSuccessResponse(userProfileRespVo);
	}

	/**
	 * @author Vivekkumar Patel Get user details such as firstname,lastname,username
	 *         etc
	 * @param http header containing jet token to validate the user.
	 * @return UserProfileRespVo userdetails
	 */
	@PostMapping(ApplicationUrlPath.UPDATE_USER_PROFILE)
	public ResponseVO<User> updateUserProfile(@RequestHeader HttpHeaders http,
											  @RequestBody UserProfileRequestVo userProfileRequestVo) {
		User user = null;
		try {
			String jwtToken = http.getFirst("jwt-token");
			if (!jwtToken.isEmpty()) {

				user = userService.updateUserProfile(userProfileRequestVo);
				if (user == null) {
					return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.name(),
							false);
				}
			}
		} catch (Exception e) {
			return error(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage(), false);
		}
		return prepareSuccessResponse(user);
	}

	/**
	 * Fetch the user details who have posted articles .
	 *
	 * @param http request header
	 * @return true if user tags are saved.
	 */
	@GetMapping(ApplicationUrlPath.GET_USER_DETAILS_POSTED_ARTICLE)
	public List<Map<String, Object>> getUserDetailsPostedArticle(@RequestHeader HttpHeaders http) {
		List<Map<String, Object>> userDetailsOfPostedArticle = new ArrayList<>();
		try {
			userDetailsOfPostedArticle = userService.getUsersPostedArticle();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return userDetailsOfPostedArticle;
	}

}

package com.asdc.smarticle.security;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.asdc.smarticle.comutil.AppConstant;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;

/**
 * 
 * @author Sarthak Patel
 * @version 1.0
 * @since 2022-02-22
 */

@Component
public class JwtUtils {

	@Value("${app.jwtSecret}")
	private String jwtSecret;

	@Value("${app.jwtExpiry}")
	private int jwtExpiry;

	@Value("${app.jwtCookieName}")
	private String jwtCookie;

	/**
	 * This method will return the JWT-Token
	 * @param request is the instance of HttpServletRequest provides with servelet request information
	 * @return jwt-token if cookie value if not null else return null
	 * */
	public String getJwtTokenFromCookies(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, jwtCookie);
		if (cookie != null) {
			return cookie.getValue();
		} else {
			return null;
		}
	}
	/**
	 * This method will generate the JWT-Token based for the given user
	 * @param userName is the name of the user for whom the token is generated
	 * @return cookie containing jwt-token
	 * */
	public ResponseCookie generateJwtTokenCookie(String userName) {
		String jwt = generateJwtTokenFromUsername(userName);
		ResponseCookieBuilder responseCookieBuilder = ResponseCookie.from(jwtCookie, jwt);

		responseCookieBuilder = responseCookieBuilder.path("/smarticleapi").maxAge(AppConstant.MAX_AGE_CONSTANT);

		ResponseCookie cookie = responseCookieBuilder.httpOnly(true).build();
		return cookie;
	}

	/**
	 * This method will clean the cookie
	 * @return cookie containing jwt-token
	 * */
	public ResponseCookie getCleanJwtTokenCookie() {
		ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/smarticleapi").build();
		return cookie;
	}

	/**
	 * This username based on JWT-Token
	 * @param token contain the token number from where the userName is retrieved
	 * @return usrname
	 * */
	public String getUserNameFromJwt(String token) {
		return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
	}
	/**
	 * This method will validate th jwt-token based on its expiration date,  validity of token.
	 * @param contains the token number
	 * @return return true if validation is successful else false
	 * */
	public boolean validateJwt(String authToken) {
		try {
			Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
			return true;
		} catch (MalformedJwtException e) {
			System.out.println("Invalid JWT token: " + e.getMessage());
		} catch (ExpiredJwtException e) {
			System.out.println("JWT token is expired: " + e.getMessage());
		} catch (UnsupportedJwtException e) {
			System.out.println("JWT token is unsupported: " + e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println("JWT claims string is empty: " + e.getMessage());
		}
		return false;
	}

	/**
	 * This method will generate the token for the user
	 * @param userName contains the username of the user.
	 * @return returns the instance of jwt.
	 */
	public String generateJwtTokenFromUsername(String username) {
		JwtBuilder jwtBuilder = Jwts.builder();
		jwtBuilder.setSubject(username);
		jwtBuilder.setIssuedAt(new Date());
		jwtBuilder.setExpiration(new Date((new Date()).getTime() + jwtExpiry));
		jwtBuilder.signWith(SignatureAlgorithm.HS512, jwtSecret);

		return jwtBuilder.compact();
	}

	/**
	 * @author Vivekkumar Patel This method checks that jwtToken is empty or not.
	 * @param jwtToken string.
	 * @return true if jwtToken is empty else false.
	 */
	public boolean isTokenEmpty(String jwtToken) {
		return jwtToken != null && !jwtToken.isEmpty();
	}

	/**
	 * @author Vivekkumar Patel This method checks that jwtToken is undefined or
	 *         not.
	 * @param jwtToken string.
	 * @return true if jwtToken is undefined else false.
	 */
	public boolean isTokenUndefined(String jwtToken) {
		return !jwtToken.equals("null") && !jwtToken.equals("undefined");
	}
}

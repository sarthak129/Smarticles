package com.asdc.smarticle;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseCookie.ResponseCookieBuilder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.asdc.smarticle.comutil.AppConstant;
import com.asdc.smarticle.security.JwtUtils;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class JwtUtilsUnitTest {

	@Autowired
	JwtUtils jwtUtils;

	@MockBean
	Cookie cookie;

	@MockBean
	HttpServletRequest request;

	@MockBean
	ResponseCookie responseCookie;

	@MockBean
	JwtBuilder jwtBuilder;
	
	@MockBean
	ResponseCookieBuilder responseCookieBuilder;

	@Test
	void testGetJwtFromCookie() {
		// cookie = WebUtils.getCookie(request, "xyz");
		// cookie.setValue(null);
		// Assert.assertEquals(cookie.getValue(),
		// jwtUtils.getJwtTokenFromCookies(request));
		Assert.assertNull(jwtUtils.getJwtTokenFromCookies(request));
	}

	@Test
	void testGetCleanJwtTokenCookie() {
		responseCookie = ResponseCookie.from("smarticlejwt", null).path("/smarticleapi").build();
		Assert.assertEquals(responseCookie, jwtUtils.getCleanJwtTokenCookie());
	}

	@Test
	void testGetUserNameFromJwt() {
		String token = jwtUtils.generateJwtTokenFromUsername("sarthak");
		String userName = Jwts.parser().setSigningKey("smarticlejwtSecret").parseClaimsJws(token).getBody()
				.getSubject();
		Assert.assertEquals(userName, jwtUtils.getUserNameFromJwt(token));
	}

	@Test
	void testIsTokenEmpty() {
		Assert.assertTrue(jwtUtils.isTokenEmpty("abc"));
		Assert.assertFalse(jwtUtils.isTokenEmpty(""));
		Assert.assertFalse(jwtUtils.isTokenEmpty(null));
	}

	@Test
	void testIsTokenUndefined() {
		Assert.assertTrue(jwtUtils.isTokenUndefined("abc"));
		Assert.assertFalse(jwtUtils.isTokenUndefined("null"));
		Assert.assertFalse(jwtUtils.isTokenUndefined("undefined"));
	}

	@Test
	void testValidateJwt() {
		String token = jwtUtils.generateJwtTokenFromUsername("sarthak");
		Assert.assertTrue(jwtUtils.validateJwt(token));
		Assert.assertFalse(jwtUtils.validateJwt(""));
		Assert.assertFalse(jwtUtils.validateJwt("abc"));
		Assert.assertFalse(jwtUtils.validateJwt(
				"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJzYXJ0aGFrMDMiLCJpYXQiOjE2NDg1MjA0NjUsImV4cCI6MTY0ODUzMDQ2NX0.FmpTCMUsquLEKXtp7mr_gvBoAEP5xite6WXClBXvAZFNxll92tbfHFW24Tt5gPOcYKGue0CCpsGU_8w7QApU2g"));
	}

	@Test
	void testGenerateJwtTokenCookie() {
		String token = jwtUtils.generateJwtTokenFromUsername("sarthak");
		responseCookieBuilder = ResponseCookie.from("smarticlejwt", token);
		responseCookieBuilder = responseCookieBuilder.path("/smarticleapi").maxAge(AppConstant.MAX_AGE_CONSTANT);
		responseCookie = responseCookieBuilder.httpOnly(true).build();
		//responseCookieBuilder = responseCookieBuilder.path("/smarticleapi").maxAge(AppConstant.MAX_AGE_CONSTANT);
		Assert.assertEquals(responseCookie, jwtUtils.generateJwtTokenCookie("sarthak"));
	}
}

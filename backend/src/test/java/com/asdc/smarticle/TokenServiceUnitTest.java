package com.asdc.smarticle;


import com.asdc.smarticle.comutil.AppConstant;
import com.asdc.smarticle.token.Token;
import com.asdc.smarticle.token.TokenFactory;
import com.asdc.smarticle.token.TokenRepository;
import com.asdc.smarticle.token.TokenService;
import com.asdc.smarticle.user.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class TokenServiceUnitTest {
	

	 

	@MockBean
	private TokenRepository tokenRepository;
	
	@Autowired
	TokenService tokenService;
	
	@MockBean
	TokenFactory tokenFactory;
	
	@MockBean
	private Token token;
	
	@MockBean
	private StringKeyGenerator stringKeyGenerator;
	
	@MockBean
	private User user;

	
	@Test
	void testTokenExpired() {
		
		Mockito.when(token.getExpiryDate()).thenReturn(LocalDateTime.now().minusSeconds(AppConstant.TEST_TOKEN_VAL_SEC));
		Assert.assertTrue(tokenService.isTokenExpired(token));
		 
		
	}
	
	@Test
	void testDeleteToken() {
		
		tokenService.deleteToken(token);
		Mockito.verify(tokenRepository, Mockito.times(1)).delete(token);
		
		
	}
	
	 
 	 
	@Test 
	void testCreateToken() { 

		
		Mockito.when(tokenFactory.getTokenInstance()).thenReturn(token);
		token.setUser(user);
		token.setExpiryDate(LocalDateTime.now().plusSeconds(Long.parseLong("10000000")));
		token.setToken(stringKeyGenerator.generateKey());
		Mockito.when(tokenRepository.save(token)).thenReturn(token);
		Assert.assertEquals(token, tokenService.createToken(user));
	}
	 
	 

}

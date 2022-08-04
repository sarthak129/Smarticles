package com.asdc.smarticle.token;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.security.crypto.keygen.StringKeyGenerator;
import org.springframework.stereotype.Service;

import com.asdc.smarticle.user.User;

/**
 * Service implementation for token entity.
 * 
 * @author Vivekkumar Patel
 * @version 1.0
 * @since 2022-02-19
 */
@Service
public class TokenServiceImpl implements TokenService {
 
	@Autowired
	TokenRepository tokenRepository;
	
	@Autowired
	TokenFactory tokenFactory;

	@Value("${token.expirtytime}")
	private String tokenValidity;

	/**
	 * This method will create the tooken for the user
	 * @param user is the instnce of User; token generated for the user
	 * @return the token generated for the user
	 */

	@Override
	public Token createToken(User user) {

		StringKeyGenerator tokenString = KeyGenerators.string();
		Token token = tokenFactory.getTokenInstance();
		token.setUser(user);
		token.setExpiryDate(LocalDateTime.now().plusSeconds(Long.parseLong(tokenValidity)));
		token.setToken(tokenString.generateKey());
		tokenRepository.save(token);

		return token;
	}

	/**
	 * This method will check weather the token is expired or not
	 * @param token is the instance of Token whose expiry date is to be checked
	 * @return true is token is expired, false otherwise
	 */
	@Override
	public boolean isTokenExpired(Token token) {

		 return token.getExpiryDate().isBefore(LocalDateTime.now());
	}

	/**
	 * This method will delete the token
	 * @param token is the instance of Token which is to be deleted
	 */
	@Override
  	public void deleteToken(Token token) { 

		tokenRepository.delete(token); 
	}
  
}

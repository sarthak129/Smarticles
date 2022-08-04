package com.asdc.smarticle.token;

import com.asdc.smarticle.user.User;

/**
 * Services for token entity.
 * 
 * @author Vivekkumar Patel
 * @version 1.0
 * @since 2022-02-19
 */
public interface TokenService {

	Token createToken(User user);
	
	boolean isTokenExpired(Token token);
	
	void deleteToken(Token token);
	
}

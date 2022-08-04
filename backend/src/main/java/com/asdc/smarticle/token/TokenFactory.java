package com.asdc.smarticle.token;

import org.springframework.stereotype.Component;

/**Component class to retrieve the token instance*/
@Component
public class TokenFactory {

	public Token getTokenInstance() {

		return new Token();
	}

}

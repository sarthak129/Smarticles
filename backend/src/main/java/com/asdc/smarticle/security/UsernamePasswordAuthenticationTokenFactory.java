package com.asdc.smarticle.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * This component class to provide authneticate the user
 *
 */
@Component
public class UsernamePasswordAuthenticationTokenFactory {

	/**
	 * This method will authenticate the user
	 * @param userDetails is the instance of UserDetails contains the details for user.
	 * @return returns the instance of UsernamePasswordAuthenticationToken.
	 */
	public UsernamePasswordAuthenticationToken getUsernamePasswordAuthenticationTokenFactory(UserDetails userDetails) {

		return new UsernamePasswordAuthenticationToken(
				userDetails, null, userDetails.getAuthorities());
	}
}

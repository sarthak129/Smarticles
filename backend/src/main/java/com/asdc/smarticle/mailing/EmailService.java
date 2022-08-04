package com.asdc.smarticle.mailing;

import javax.mail.MessagingException;

import org.springframework.http.ResponseCookie;

import com.asdc.smarticle.token.Token;
import com.asdc.smarticle.user.User;

/**
 * Service for email entity
 * 
 * @author Vivekkumar Patel, Sarthak Patel
 * @version 1.0
 * @since 2022-02-19
 */
public interface EmailService {

	void sendConfirmationEmail(User user, Token token) throws MessagingException;

	void sendForgotPasswordEmail(User user, ResponseCookie jwtCookie) throws MessagingException;
	
	String httpVeificationURL(Token token);
	
	String httpResetPasswordnURL(ResponseCookie token); 
}

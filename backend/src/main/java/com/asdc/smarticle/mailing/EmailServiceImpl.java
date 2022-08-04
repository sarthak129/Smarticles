package com.asdc.smarticle.mailing;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.asdc.smarticle.token.Token;
import com.asdc.smarticle.user.User;

/**
 * Implementation to for email entity.
 * 
 * @author Vivekkumar Patel, Sarthak Patel
 * @version 1.0
 * @since 2022-02-19
 */
@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	JavaMailSender javaMailSender;

	@Autowired
	TemplateEngine templateEngine;
	
	@Autowired
	MimeMessageFactory mimeMessageFactory;
	
	@Autowired
	ContextFactory contextFactory;

	@Value("${client.url}")
	private String clientURl;

	/**
	 * Send confirmation email to user .
	 *
	 * @param user User object
	 * @param token
	 */
	@Override
	public void sendConfirmationEmail(User user, Token token) throws MessagingException {
		Context context = contextFactory.getContextInstance();

		context.setVariable("verificationLink", httpVeificationURL(token));

		String template = templateEngine.process("welcome", context);
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = mimeMessageFactory.getMimeMessageInstance(mimeMessage);
		mimeMessageHelper.setFrom("patelvivek221996@gmail.com");
		mimeMessageHelper.setTo(user.getEmailID());
		mimeMessageHelper.setText(template, true);
		mimeMessageHelper.setSubject("Smarticle user account verification");
		javaMailSender.send(mimeMessage);
	}
	
	/**
	 * @author Vivekkumar Patel
	 * Build http url for account verification containing given token 
	 * @param token jwt token string.
	 * @return http url string.
	 */
	@Override
	public String httpVeificationURL(Token token) {

		UriComponentsBuilder verificationURl = UriComponentsBuilder.fromHttpUrl(clientURl).path("/verify");

		verificationURl = verificationURl.queryParam("token", token.getToken());

		return verificationURl.toUriString();
	}
	
	/** 
	 * @author Vivekkumar Patel
	 * Build http url for reset password containing given token 
	 * @param token jwt token string.
	 * @return http url string. 
	 */
	@Override
	public String httpResetPasswordnURL(ResponseCookie token) {
		UriComponentsBuilder verificationURl = UriComponentsBuilder.fromHttpUrl(clientURl).path("/reset");

		verificationURl = verificationURl.queryParam("token", token.getValue());
		return verificationURl.toUriString();
	}

	/**
	 * Send email to user when the user forgets password .
	 *
	 * @param user User object
	 * @param token
	 */
	@Override
	public void sendForgotPasswordEmail(User user, ResponseCookie token) throws MessagingException {
		Context context = new Context();
		context.setVariable("resetPasswordLink", httpResetPasswordnURL(token));
		String template = templateEngine.process("resetPassword", context);
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
		mimeMessageHelper.setTo(user.getEmailID());
		mimeMessageHelper.setText(template, true);
		mimeMessageHelper.setSubject("Reset Password");
		javaMailSender.send(mimeMessage);
	} 
 
}

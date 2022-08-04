package com.asdc.smarticle;

import com.asdc.smarticle.mailing.ContextFactory;
import com.asdc.smarticle.mailing.EmailService;
import com.asdc.smarticle.mailing.MimeMessageFactory;
import com.asdc.smarticle.token.Token;
import com.asdc.smarticle.user.User;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseCookie;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.util.UriComponentsBuilder;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EmailServiceUnitTest {

	@Autowired
	private EmailService emailService;

	@MockBean
	JavaMailSender javaMailSender;

	@MockBean
	MimeMessage mimeMessage;

	@MockBean
	MimeMessageHelper mimeMessageHelper;

	@MockBean
	MimeMessageFactory mimeMessageFactory;

	@MockBean
	ContextFactory contextFactory;

	@MockBean
	UriComponentsBuilder uriComponentsBuilder;

	@MockBean
	Context context;

	@MockBean
	ResponseCookie responseCookie;

	@MockBean
	Token token;

	@MockBean
	User user;

	/*
	 * @Test void testSendConfirmationEmail() throws MessagingException {
	 * 
	 * Context context = new Context();
	 * 
	 * String templateString = "template";
	 * Mockito.when(emailService.httpVeificationURL(token)).thenReturn("http://test"
	 * );
	 * Mockito.when(UriComponentsBuilder.fromHttpUrl("http://test.com").path("test")
	 * ).thenReturn(uriComponentsBuilder);
	 * Mockito.when(token.getToken()).thenReturn("token");
	 * Mockito.when(uriComponentsBuilder.queryParam("token", token.getToken()));
	 * Assert.assertEquals("http://test/token",
	 * emailService.httpVeificationURL(token));
	 * 
	 * context.setVariable("verificationLink",
	 * emailService.httpVeificationURL(token));
	 * Mockito.when(templateEngine.process("welcome",
	 * context)).thenReturn(templateString);
	 * Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
	 * mimeMessageHelper = new MimeMessageHelper(mimeMessage);
	 * mimeMessageHelper.setFrom("testFrom"); mimeMessageHelper.setTo("testTo");
	 * mimeMessageHelper.setText(templateString,true);
	 * mimeMessageHelper.setSubject("subject");
	 * Mockito.verify(javaMailSender,Mockito.times(1)).send(mimeMessage);
	 * emailService.sendConfirmationEmail(user, token);
	 * 
	 * }
	 */

	/*
	 * @Test void testSendConfirmationEmail() throws MessagingException {
	 * Mockito.when(contextFactory.getContextInstance()).thenReturn(context);
	 * Mockito.when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);
	 * Mockito.when(mimeMessageFactory.getMimeMessageInstance(mimeMessage)).
	 * thenReturn(mimeMessageHelper); TemplateEngine templateEngine = new
	 * TemplateEngine(); String template = templateEngine.process("welcome",
	 * context); mimeMessageHelper.setFrom("patelvivek221996@gmail.com");
	 * mimeMessageHelper.setTo(user.getEmailID());
	 * mimeMessageHelper.setText(template, true);
	 * mimeMessageHelper.setSubject("Smarticle user account verification");
	 * emailService.sendConfirmationEmail(user, token);
	 * Mockito.verify(javaMailSender, Mockito.times(1)).send(mimeMessage); }
	 */

	@Test
	void TestVerificationUrl() {

		String url = "https://smarticledigital.herokuapp.com";

		uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url).path("/verify");
		Mockito.when(token.getToken()).thenReturn("123ABC");

		uriComponentsBuilder = uriComponentsBuilder.queryParam("token", "123ABC");
		Assert.assertEquals(uriComponentsBuilder.toUriString(), emailService.httpVeificationURL(token));
	}

	@Test
	void TestResetUrl() {

		String url = "https://smarticledigital.herokuapp.com";

		uriComponentsBuilder = UriComponentsBuilder.fromHttpUrl(url).path("/reset");
		Mockito.when(responseCookie.getValue()).thenReturn("123ABC");

		uriComponentsBuilder = uriComponentsBuilder.queryParam("token", "123ABC");
		Assert.assertEquals(uriComponentsBuilder.toUriString(), emailService.httpResetPasswordnURL(responseCookie));
	}

} 

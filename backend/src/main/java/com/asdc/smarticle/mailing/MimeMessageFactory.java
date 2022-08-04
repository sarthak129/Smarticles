package com.asdc.smarticle.mailing;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class MimeMessageFactory {

	public MimeMessageHelper getMimeMessageInstance(MimeMessage mimeMessage) {

		return new MimeMessageHelper(mimeMessage);
	}

}

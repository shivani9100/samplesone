package com.Inventory.Project.AssectService.MaillingService;

import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

	private static final Logger logger = LogManager.getLogger(MailService.class);

	@Autowired
	private JavaMailSender javaMailSender;

	public Boolean sendMail(Mail mail) {
		try {
			logger.info("sending mail");
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setFrom(mail.getMailFrom());

			if (mail.getMailTo() != null) {

				helper.setTo(mail.getMailTo());
			}

			if (mail.getMailToArray() != null) {

				helper.setTo(mail.getMailToArray());
			}

			if (mail.getMailCc() != null) {

				helper.setCc(mail.getMailCc());
			}

			if (mail.getMailCcArray() != null) {

				helper.setCc(mail.getMailCcArray());
			}

			helper.setSubject(mail.getMailSubject());

			helper.setText(mail.getMailContent());
		

			javaMailSender.send(message);

			return true;

		} catch (Exception e) {

			logger.info("Exception in sending mail" + e);

			return false;
		}

	}

}

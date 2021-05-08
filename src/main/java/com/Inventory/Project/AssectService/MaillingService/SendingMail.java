package com.Inventory.Project.AssectService.MaillingService;

import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.Inventory.Project.AssectService.Employee.Employee;

/*@RestController
@RequestMapping("/send")*/

@Service
public class SendingMail {

	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private Environment environment;

	private static final Logger logger = LogManager.getLogger(SendingMail.class);

	/*
	 * @PostMapping("/allocate") public String mailForAllocatingAsset(@RequestBody
	 * Employee employee) {
	 */

	public Boolean sendingMailToAdmin(List<Employee> employeeByRole, Integer id) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setFrom(environment.getProperty("spring.mail.username"));

			List<String> arrayList = new ArrayList<>();

			if (!employeeByRole.isEmpty()) {
				employeeByRole.forEach(emp -> arrayList.add(emp.getEmail()));

				String[] stringArray = arrayList.toArray(new String[0]);

				helper.setTo(stringArray);
			}

			helper.setSubject(environment.getProperty("trouble.ticket.mail.subject"));

			helper.setText("Hi," + "A Trouble ticket with No. " + id + " has been created and please review that.");
			javaMailSender.send(message);
			logger.info("Request for asset:mail sent success to admin with ticket id" + id);
		} catch (MessagingException e) {

			logger.error("occured during sending a mail" + e.getMessage());
			return false;
		}

		return true;
	}

	public Boolean sendingMail(String employeeemail, Integer ticketid, String ccmail) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);

			helper.setFrom(environment.getProperty("spring.mail.username"));
			helper.setTo(employeeemail);

			if (ccmail != null) {
				helper.setCc(ccmail);
			}

			helper.setSubject(environment.getProperty("trouble.ticket.mail.subject"));

			helper.setText("Hi," + "A Trouble ticket with No. " + ticketid
					+ " has been created and sent to the concerned person. Please await a response from them.");
			javaMailSender.send(message);
			logger.info(

					"Request for asset:mail sent success for employee " + employeeemail + "with ticket id" + ticketid);
		} catch (MessagingException e) {

			logger.error("occured during sending a mail" + e.getMessage());
			return false;
		}

		return true;

	}

}

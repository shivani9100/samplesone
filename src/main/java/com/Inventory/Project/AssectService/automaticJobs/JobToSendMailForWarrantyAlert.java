package com.Inventory.Project.AssectService.automaticJobs;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.codec.CharEncoding;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.Inventory.Project.AssectService.Assect.AssectModel;
import com.Inventory.Project.AssectService.Assect.AssectService;
import com.Inventory.Project.AssectService.Assect.ExcelFileExport;
import com.Inventory.Project.AssectService.Employee.Employee;
import com.Inventory.Project.AssectService.Employee.EmployeeService;

@Component
public class JobToSendMailForWarrantyAlert {

	@Autowired
	private AssectService assectservice;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private JavaMailSender javaMailSender;
	@Autowired
	private ExcelFileExport exportListToExcel;

	@Autowired
	private Environment environment;

	@Scheduled(cron = "0 30 9 * * FRI")
	//@Scheduled(cron = "0 0/2 * * * ?")
	public void sendWarrantyExpiryMail() throws IOException, MessagingException {
		CompletableFuture.runAsync(() -> {
			Calendar now = Calendar.getInstance();
			Date now1 = now.getTime();
			Calendar end = Calendar.getInstance();
			end.add(Calendar.MONTH, 3);
			Date end1 = end.getTime();
			List<AssectModel> assetsList = assectservice.getAssetsBasedOnWarrantyBetween(now1, end1);

			try {
				ByteArrayInputStream byteArray = exportListToExcel.exportAssectListToExcelFile(assetsList);

				List<Employee> employeeByRole = employeeService
						.getEmployeeByRole(environment.getProperty("employee.role"));

				MimeMessage message = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true, CharEncoding.UTF_8);

				List<String> arrayList = new ArrayList<>();

				if (!employeeByRole.isEmpty()) {
					employeeByRole.forEach(emp -> arrayList.add(emp.getEmail()));
				}
				String[] str = arrayList.toArray(new String[0]);

				helper.setFrom(environment.getProperty("spring.mail.username"));

				helper.setTo(str);
				helper.setSubject("Asset Warranty Expiry Alert");

				helper.setText(
						"Hi , This is to inform you that the following assets expiry end date  is less than Three months  ");

				ByteArrayDataSource attachment = new ByteArrayDataSource(byteArray, "application/octet-stream");
				helper.addAttachment("AssetsWithWarrantyEndIn3Months.xlsx", attachment);

				javaMailSender.send(message);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("something went wrong");
			}
		});

	}

}

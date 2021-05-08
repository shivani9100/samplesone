package com.Inventory.Project.AssectService.Employee;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Inventory.Project.AssectService.Exception.RecordNotFoundException;
import com.Inventory.Project.AssectService.Response.Response;
import com.Inventory.Project.AssectService.Response.ResponseList;
import com.Inventory.Project.AssectService.Security.AuthenticationResponse;
import com.Inventory.Project.AssectService.Security.JwtUtil;
import com.Inventory.Project.AssectService.Security.LoginRequest;
import com.Inventory.Project.AssectService.Security.UserDetailsImple;
import com.Inventory.Project.AssectService.Validations.Validations;
import com.Inventory.Project.AssectService.forgotpassword.ConfirmationToken;
import com.Inventory.Project.AssectService.forgotpassword.ConfirmationTokenService;

@CrossOrigin
@RestController
@RequestMapping("/employee")
public class EmployeeController {

	private static final long EXPIRE_TOKEN_AFTER_MINUTES = 5;

	private static final Logger logger = LogManager.getLogger(EmployeeController.class);

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private UserDetailsImple userDetailsImple;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtTokenUtil;

	@Autowired
	private ConfirmationTokenService confirmationTokenService;

	@Autowired
	private JavaMailSender javaMailSender;
	
	@GetMapping("/test")
	public String test() {
		return "test";
	}
	// Store the UserProfile Data into DataBase //
	@PostMapping("/saveUser")
	public ResponseEntity<?> saveData(@RequestBody Employee employee) {
		try {
			Response response = new Response();
			if (employee == null) {
				logger.error("Employee object is null  in post");
				response.setMessage("Request object shouldn't null");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}
			if (employee.getEmployeeid().isEmpty() || employee.getFirstName().isEmpty()
					|| employee.getLastName().isEmpty() || employee.getEmail().isEmpty()
					|| employee.getPhoneNumber().isEmpty()) {
				response.setMessage(" Null values are not allowed");
				response.setStatus("422");

				logger.info("Null values are not allowed");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			if (!Validations.isNameValid(employee.getFirstName())) {

				response.setMessage(" Give Valid FirstName");
				response.setStatus("422");
				logger.info("Give Valid FirstName");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			if (!Validations.isNameValid(employee.getLastName())) {

				response.setMessage(" Give Valid LastName");
				response.setStatus("422");
				logger.info("Give Valid LastName");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			if (!Validations.isEmailValid(employee.getEmail())) {

				response.setMessage(" Give Valid Email");
				response.setStatus("422");
				logger.info("Give Valid Email");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			//            if (!Validations.isPassWordValid(employee.getPassword())) {
			//
			//                response.setMessage(" Give Valid Password");
			//                response.setStatus("422");
			//                logger.info("Give Valid Password");
			//                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			//            }
			if (!Validations.isMobileValid(employee.getPhoneNumber())) {

				response.setMessage(" Give Valid PhoneNumber");
				response.setStatus("422");
				logger.info("Give Valid PhoneNumber");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			if (!Validations.isEmailValid(employee.getReportingManager())) {

				response.setMessage("Give Valid Manager Data");
				response.setStatus("422");
				logger.info("Give Valid Manager Data");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			//            employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
			String passwordDefault = "Ojas@1525";
			employee.setPassword(bCryptPasswordEncoder.encode(passwordDefault));

			boolean b = employeeService.saveEmployeeData(employee);
			Employee emp = employeeService.findByEmail(employee.getEmail());
			System.out.println(emp);
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			helper.setTo(emp.getEmail());
			helper.setSubject("Welcome To Ojas Family");
			helper.setFrom("inventory20k@gmail.com");
			message.setContent(
					"<html>Welcome To OJAS INNOVATIVE TECHNOLOGIES: Please Find the below credetials for Login" + "<br>"
							+ "<span><b>Employee ID</b></span> :" + employee.getEmployeeid() + "<br>"
							+ "<span><b>Login Mail-ID</b></span>:" + employee.getEmail() + "<br>"
							+ "<span><b>Login Passowrd</b></span>:" + employee.getPasswordConfirm() + "<br>"
							+ "<span><b>Reporting Manager</b></span>:" + employee.getReportingManager() + "<br>"

							+ "<a href='http://localhost:8900/employee/authenticate'><b>click here to login page</b></a>"
							+ "</html>",
					"text/html");
			javaMailSender.send(message);
			logger.info("Employee Details are added or created");
			if (b) {
				response.setMessage("Employee registered successfully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				response.setMessage(" Employee not registered successfully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);
			}

		} catch (DataIntegrityViolationException exception) {
			Response response = new Response();
			response.setMessage("Already Exists Email or Phone num");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			Response response = new Response();
			logger.debug("inside catch block " + e.getMessage());

			response.setMessage("Dublicates are not allowed");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}
	}

	@GetMapping("/getall")
	public ResponseEntity<?> getAll() {
		try {
			logger.info("getting list of records");
			List<Employee> list = employeeService.getAllEmployee();

			return new ResponseEntity<>(list, HttpStatus.OK);
		}

		catch (Exception e) {
			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}
	}

	// Get All UserProfile Details //
	@GetMapping(path = { "/{pageNo}/{sizePerPage}/{searchByFirstName}" })
	public ResponseEntity<?> getAllEmployeeInfo(
			@PathVariable(required = false, name = "searchByFirstName") String searchByFirstName,
			@PathVariable("pageNo") Integer pageNo, @PathVariable("sizePerPage") Integer sizePerPage) {
		if (searchByFirstName.equalsIgnoreCase("null") || searchByFirstName.equalsIgnoreCase("undefined")) {
			searchByFirstName = null;
		}
		if (pageNo == null || sizePerPage == null) {
			logger.error("Object is null  in post");
			Response response = new Response();

			response.setMessage("page number and size per page shouldnot null");
			response.setStatus("400");

			return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		}

		try {
			ResponseList listOfUsers = null;

			if (searchByFirstName == null) {
				listOfUsers = employeeService.getAllEmployeeDetails(pageNo, sizePerPage);
			}

			else {
				listOfUsers = employeeService.getAllEmployeeDetails(searchByFirstName, pageNo, sizePerPage);
			}

			logger.info("getting list of records" + listOfUsers);

			return new ResponseEntity<>(listOfUsers, HttpStatus.OK);
		} catch (Exception e) {

			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}

	// Get UserProfile details By Id //
	@GetMapping("/get/{id}")
	public ResponseEntity<?> getEmployeeDetailsById(@PathVariable("id") String id) throws RecordNotFoundException {
		try {

			if (id == null) {

				logger.error("Employee id is null  in getMapping");

				Response response = new Response();

				response.setMessage("Employee id is null");
				response.setStatus("422");

				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}

			Employee user = employeeService.getEmployeeById(id);

			if (user == null) {
				throw new RecordNotFoundException("Record is not present");
			}

			return new ResponseEntity<>(user, HttpStatus.OK);
		} catch (RecordNotFoundException e) {
			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("No records found");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception e) {
			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}
	}

	// Get UserProfile details By Mail //
	@GetMapping("/{mail}")
	public ResponseEntity<?> getEmployeeDetailsByEmail(@PathVariable("mail") String mail)
			throws RecordNotFoundException {
		try {

			if (mail == null) {

				logger.error("Employee status is null  in getMail by mail");

				Response response = new Response();

				response.setMessage("Mail is null");
				response.setStatus("422");

				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			Employee profile = employeeService.findByEmail(mail);

			if (profile == null) {
				throw new RecordNotFoundException("Record is not present");
			}

			return new ResponseEntity<>(profile, HttpStatus.OK);
		} catch (RecordNotFoundException e) {
			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("No Record Exit By This Mail");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

		catch (Exception e) {
			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}

	}

	// Updating the UserProfile ById //
	@PutMapping("/update/{id}")
	public ResponseEntity<?> updateEmployeeById(@PathVariable("id") String id, @RequestBody Employee employee)
			throws RecordNotFoundException {
		try {
			Response response = new Response();

			if (employee == null) {
				logger.error("Object is null  in post");

				response.setMessage("Request object shouldn't null ");
				response.setStatus("422");

				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
			}

			if (employee.getEmployeeid().isEmpty() || employee.getFirstName().isEmpty()
					|| employee.getLastName().isEmpty() || employee.getEmail().isEmpty()
					|| employee.getPhoneNumber().isEmpty()) {
				response.setMessage(" Null values are not allowed");
				response.setStatus("422");

				logger.info("Null values are not allowed");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			if (!Validations.isEmailValid(employee.getReportingManager())) {

				response.setMessage("Give Valid Manager Data");
				response.setStatus("422");
				logger.info("Give Valid Manager Data");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			if (!Validations.isNameValid(employee.getFirstName())) {

				response.setMessage(" Give Valid FirstName");
				response.setStatus("422");
				logger.info("Give Valid FirstName");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			if (!Validations.isNameValid(employee.getLastName())) {

				response.setMessage(" Give Valid LastName");
				response.setStatus("422");
				logger.info("Give Valid LastName");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			if (!Validations.isEmailValid(employee.getEmail())) {

				response.setMessage(" Give Valid Email");
				response.setStatus("422");
				logger.info("Give Valid Email");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			//                         if (!Validations.isPassWordValid(employee.getPassword())) {
			//
			//                             response.setMessage(" Give Valid Password");
			//                             response.setStatus("422");
			//                             logger.info("Give Valid Password");
			//                             return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			//                         }
			if (!Validations.isMobileValid(employee.getPhoneNumber())) {

				response.setMessage(" Give Valid PhoneNumber");
				response.setStatus("422");
				logger.info("Give Valid PhoneNumber");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			//                         employee.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
			Employee profile = employeeService.getEmployeeById(employee.getEmployeeid());

			if (profile == null) {
				throw new RecordNotFoundException("Record is not present");
			}

			Boolean boolean1 = employeeService.updateOrEditEmployeeProfile(employee);
			if (boolean1) {

				response.setMessage("Employee updated successfully");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {

				response.setMessage("Profile not updated");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);

			}

		} catch (RecordNotFoundException e) {
			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("No records found");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (DataIntegrityViolationException exception) {
			Response response = new Response();
			response.setMessage("Already Exists Email or Phone num");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (Exception e) {
			Response response = new Response();
			logger.debug("inside catch block " + e.getMessage());

			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}
	}

	// Deleting the UserProfile By Id //
	@DeleteMapping("/delete/{id}")
	public ResponseEntity<?> deleteEmplyeeById(@PathVariable("id") String id) throws RecordNotFoundException {
		try {
			if (id == null) {
				logger.error("Id is null in DeleteMapping");

				Response response = new Response();

				response.setMessage("Id shouldn't null for while Deleting ");
				response.setStatus("422");

				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

			}

			Employee profile = employeeService.getEmployeeById(id);

			if (profile == null) {
				throw new RecordNotFoundException("record is not present");
			}

			employeeService.deleteEmployeeProfileById(id);

			logger.info("EmployeeProfile is deleted");

			Response response = new Response();
			response.setMessage("Employee Profile deleted successfully");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);

		} catch (RecordNotFoundException e) {
			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("No records found");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		} catch (Exception e) {
			Response response = new Response();
			logger.debug("inside catch block " + e.getMessage());

			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}

	}

	// Generate JWT Token //
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthenticationToken(@RequestBody LoginRequest loginRequest) {
		try {
			Employee employee = employeeService.findByEmail(loginRequest.getUserMail());
			if (employee == null) {
				throw new RecordNotFoundException("Email Not Found");
			}
			authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUserMail(), loginRequest.getPassword()));
		} catch (RecordNotFoundException r) {

			Response response = new Response();
			response.setMessage("Please Enter Registered EMail");
			response.setStatus("200");
			r.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.OK);
			// throw new RecordNotFoundException("Incorrect Username and Password");
		} catch (BadCredentialsException e) {

			Response response = new Response();
			response.setMessage("Incorrect Password");
			response.setStatus("200");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.OK);
			// throw new RecordNotFoundException("Incorrect Username and Password");
		}
		final UserDetails userDetails = userDetailsImple.loadUserByUsername(loginRequest.getUserMail());
		String username = userDetails.getUsername();
		Employee employee = employeeService.findByEmail(username);
		String userEditId = employee.getEmployeeid();
		String userEditEmail = employee.getEmail();
		String role = null;
		for (Role roles : employee.getRoles()) {
			role = roles.getName();
		}

		final String jwt = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new AuthenticationResponse(jwt, userEditId, userEditEmail, role));
	}

	/**
	 * Receive email of the user, create token and send it via email to the user
	 * 
	 * @throws MessagingException
	 */

	@PostMapping("/forgot-password/{mail}")
	public ResponseEntity<?> forgotUserPassword(@PathVariable("mail") String mail) throws MessagingException {
		try {
			Employee existingUser = employeeService.findByEmail(mail);

			if (existingUser != null) { // create token
				ConfirmationToken confirmationToken = confirmationTokenService.generateOTP(existingUser);

				// create the email
				MimeMessage message = javaMailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message);
				helper.setTo(existingUser.getEmail());
				helper.setSubject("Complete Password Reset!");
				helper.setFrom("pranaypodduturi@gmail.com");
				message.setContent(
						"<html>To complete the password reset process, OTP is Generated and It will Expiry in 5 Mintues: "
								+ "<br>" + confirmationToken.getConfirmationToken() + "</html>",
						"text/html");
				javaMailSender.send(message);

				Response response = new Response();
				response.setMessage("Request to reset password received. Check your inbox for the reset OTP.");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);

			} else {

				Response response = new Response();
				response.setMessage("This email address does not exist!");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);

			}

		} catch (Exception e) { // TODO: handle exception
			Response response = new Response();

			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}

	// Endpoint to confirm the token

	@GetMapping("/confirm-reset/{token}")
	public ResponseEntity<?> validateResetToken(@PathVariable("token") String confirmationToken) {
		try {
			ConfirmationToken token = confirmationTokenService.findByConfirmationToken(confirmationToken);

			if (token != null) {
				Employee employee = employeeService.findByEmail(token.getEmployee().getEmail());
				LocalDateTime now = LocalDateTime.now();
				Date date = token.getCreatedDate();
				LocalDateTime tokrnGeneratedTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
				Duration diff = Duration.between(tokrnGeneratedTime, now);

				Response response = new Response();

				if (diff.toMinutes() >= EXPIRE_TOKEN_AFTER_MINUTES) {

					response.setMessage("OTP is Expired");
					response.setStatus("200");
					return new ResponseEntity<>(response, HttpStatus.OK);
				}
				employeeService.saveEmployeeData(employee);
				logger.info("Account Verified");

				response.setMessage("Account Verified");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);
			} else {
				Response response = new Response();
				response.setMessage(" OTP is invalid or broken!");
				response.setStatus("409");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);

			}
		} catch (Exception e) {

			Response response = new Response();
			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}
	}

	/**
	 * Receive the token from the link sent via email and display form to reset
	 * password
	 */

	@PostMapping(value = "/reset-password")
	public ResponseEntity<?> resetUserPassword(@RequestBody Employee employee) {
		try {
			if (!Validations.isPassWordValid(employee.getPassword())) {
				Response response = new Response();
				response.setMessage(" Give Valid Password");
				response.setStatus("422");
				logger.info("Give Valid Password");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			Employee tokenUser = null;
			if (employee != null) {
				// use email to find user
				tokenUser = employeeService.findByEmail(employee.getEmail());
				if (tokenUser != null) {
					tokenUser.setPassword(bCryptPasswordEncoder.encode(employee.getPassword()));
					tokenUser.setPasswordConfirm(employee.getPasswordConfirm());

					employeeService.saveEmployeeData(tokenUser);
					Response response = new Response();
					response.setMessage("Password successfully reset. You can now log in with the new credentials.");
					response.setStatus("200");
					return new ResponseEntity<>(response, HttpStatus.OK);
				} else {
					Response response = new Response();
					response.setMessage(" Please provide valid email");
					response.setStatus("409");
					return new ResponseEntity<>(response, HttpStatus.CONFLICT);
				}
			} else {

				Response response = new Response();
				response.setMessage(" The link is invalid or broken!");
				response.setStatus("409");
				return new ResponseEntity<>(response, HttpStatus.CONFLICT);

			}
		} catch (Exception e) {
			Response response = new Response();
			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}

	@PostMapping(value = "/change-password")
	public ResponseEntity<?> changePassword(@RequestBody ChangePasswordRequest changePassword) {
		try {

			if (changePassword.getEmail().isEmpty() || changePassword.getEmail() == null
					|| changePassword.getOldPassword().isEmpty() || changePassword.getOldPassword() == null
					|| changePassword.getNewPassword().isEmpty() || changePassword.getNewPassword() == null

					|| changePassword.getNewConfirmPassword().isEmpty()
					|| changePassword.getNewConfirmPassword() == null)

			{

				Response response = new Response();
				response.setMessage(" Please provide Valid Information");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);

			}
			if (!Validations.isPassWordValid(changePassword.getNewPassword())) {
				Response response = new Response();
				response.setMessage(" Give Valid Password");
				response.setStatus("422");
				logger.info("Give Valid Password");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			Employee findByEmail = employeeService.findByEmail(changePassword.getEmail());
			if (findByEmail == null) {
				throw new RecordNotFoundException("Record is not present");
			}
			String password = findByEmail.getPassword();
			boolean matches = bCryptPasswordEncoder.matches(changePassword.getOldPassword(), password);
			if (!matches) {
				Response response = new Response();
				response.setMessage("Please Enter Correct Password");
				response.setStatus("200");
				return new ResponseEntity<>(response, HttpStatus.OK);
			}
			findByEmail.setPassword(bCryptPasswordEncoder.encode(changePassword.getNewPassword()));
			findByEmail.setPasswordConfirm(changePassword.getNewConfirmPassword());
			employeeService.updateOrEditEmployeeProfile(findByEmail);
			Response response = new Response();
			response.setMessage("Password Changed Successfully ");
			response.setStatus("200");
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (RecordNotFoundException e) {
			Response response = new Response();
			response.setMessage("Employee Email Not Found");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		} catch (Exception e) {
			Response response = new Response();
			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

	}

	@GetMapping("/search/{pageNumber}/{pageSize}/{text}")
	public ResponseEntity getSearchAll(@PathVariable(required = false, name = "text") String text,

			@PathVariable("pageNumber") Integer pageno, @PathVariable("pageSize") Integer pagesize) {

		try {

			if (text.equalsIgnoreCase("null") || text.equalsIgnoreCase("undefined")) {
				text = null;
			}

			if (pageno == null || pagesize == null) {
				Response response = new Response();
				response.setMessage("bad request");
				response.setStatus("422");
				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}
			ResponseList data = null;
			if (text == null) {
				data = employeeService.getAllEmployeeDetails(pageno, pagesize);
			} else {
				data = employeeService.searchAll(text, pageno, pagesize);
			}
			return new ResponseEntity<>(data, HttpStatus.OK);
		} catch (Exception exception) {
			logger.debug("Inside Catch Block : " + exception.getMessage());
			Response response = new Response();
			response.setMessage("Exception Caught");
			response.setStatus("409");
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}
	}

	// Get details of Reporting Manager //
	@GetMapping("/reportingManager/{reportingManager}")
	public ResponseEntity<?> getDetailsOfReportingManager(@PathVariable("reportingManager") String reportingManager) {
		try {

			if (reportingManager == null) {

				logger.error("There is no Reposrting Manager Data");

				Response response = new Response();

				response.setMessage("Reposrting Manager Data is null");
				response.setStatus("422");

				return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
			}

			List<Employee> profile = employeeService.getDetailsByReportingManager(reportingManager);

			if (profile == null) {
				throw new RecordNotFoundException("Record is not present");
			}

			return new ResponseEntity<>(profile, HttpStatus.OK);
		} catch (RecordNotFoundException e) {
			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("No Record Exit By This Reporting Manager");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);
		}

		catch (Exception e) {
			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}

	}

	// Getting data by roles //
	@GetMapping("/getByRole")
	public ResponseEntity<?> getEmployeeByRoles() {
		try {
			logger.info("getting list of records");
			List<Employee> employeeByRole = employeeService.getEmployeeByRole("Admin");
			return new ResponseEntity<>(employeeByRole, HttpStatus.OK);

		} catch (Exception e) {
			logger.debug("inside catch block " + e.getMessage());
			Response response = new Response();
			response.setMessage("Exception caught");
			response.setStatus("409");
			e.printStackTrace();
			return new ResponseEntity<>(response, HttpStatus.CONFLICT);

		}
	}
}

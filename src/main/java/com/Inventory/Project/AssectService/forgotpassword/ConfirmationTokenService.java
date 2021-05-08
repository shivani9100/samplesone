package com.Inventory.Project.AssectService.forgotpassword;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Inventory.Project.AssectService.Employee.Employee;

@Service
public class ConfirmationTokenService {

	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;

	public ConfirmationToken generateOTP(Employee existingUser) {
		int randomPin = (int) (Math.random() * 9000) + 1000;
		String otp = String.valueOf(randomPin);
		ConfirmationToken token = new ConfirmationToken();
		token.setConfirmationToken(otp);
		token.setCreatedDate(new Date());

		token.setEmployee(existingUser);
		ConfirmationToken token1 = confirmationTokenRepository.save(token);
		if (token1 == null) {
			return null;
		} else {
			return token1;
		}
	}

	public ConfirmationToken findByConfirmationToken(String confirmationToken) {
		
		ConfirmationToken findByConfirmationToken = confirmationTokenRepository
				.findByConfirmationToken(confirmationToken);
		return findByConfirmationToken;
	}

}

package com.Inventory.Project.AssectService.Security;

public class LoginRequest {

	private String userMail;
	private String password;

	public String getUserMail() {
		return userMail;
	}

	public void setUserMail(String userMail) {
		this.userMail = userMail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LoginRequest() {

	}

	public LoginRequest(String userMail, String password) {
		super();
		this.userMail = userMail;
		this.password = password;
	}

}

package com.Inventory.Project.AssectService.Security;

public class AuthenticationResponse {

	private String jwt;
	private String userId;
	private String userEmail;
	private String role;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public AuthenticationResponse(String jwt, String userId, String userEmail, String role) {
		super();
		this.jwt = jwt;
		this.userId = userId;
		this.userEmail = userEmail;
		this.role = role;
	}

	public AuthenticationResponse() {
		super();
	}

}

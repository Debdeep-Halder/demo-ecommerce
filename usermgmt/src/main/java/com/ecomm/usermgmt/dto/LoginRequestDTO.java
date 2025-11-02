package com.ecomm.usermgmt.dto;

public class LoginRequestDTO {
	private String username;
	private String requestedRole;
	private String password;
	private String email;
	
	public String getEmail() {
		return email;
	}

	public String getUsername() {
		return username;
	}
	
	public String getRequestedRole() {
		return requestedRole;
	}
	
	public String getPassword() {
		return password;
	}
	
}

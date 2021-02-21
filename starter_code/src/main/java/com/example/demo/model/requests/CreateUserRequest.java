package com.example.demo.model.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.context.annotation.Bean;

import javax.persistence.Column;

public class CreateUserRequest {
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@JsonProperty
	private String username;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;

	private String confirmPassword;



	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}

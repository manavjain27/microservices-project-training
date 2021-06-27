package com.wp.bookapi.model;

import org.springframework.stereotype.Component;

@Component
public class AuthenticationResponse {

	String jwt;

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		this.jwt = jwt;
	}

	public AuthenticationResponse(String jwt) {
		super();
		this.jwt = jwt;
	}

	public AuthenticationResponse() {
		super();
	}

}

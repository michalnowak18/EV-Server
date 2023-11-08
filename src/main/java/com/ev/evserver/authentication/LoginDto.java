package com.ev.evserver.authentication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto extends AuthData {

	@Builder
	public LoginDto(String email, String password) {
		super(email, password);
	}
}

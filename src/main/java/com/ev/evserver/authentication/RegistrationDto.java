package com.ev.evserver.authentication;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// TODO: this class will probably be expanded in future

@Getter
@Setter
public class RegistrationDto extends AuthData {

	@Builder
	public RegistrationDto(String email, String password) {
		super(email, password);
	}
}

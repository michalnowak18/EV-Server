package com.ev.evserver.authentication;

import com.ev.evserver.user.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegistrationDto {

	private String email;

	private String name;

	private Role role;

}

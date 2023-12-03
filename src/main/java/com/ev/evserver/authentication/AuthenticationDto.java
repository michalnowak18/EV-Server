package com.ev.evserver.authentication;

import com.ev.evserver.user.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDto {

	private String token;

	private String password;

	private Role role;

	private Long userId;

	private String name;
}

package com.ev.evserver.user;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class UserDto {

	private Long id;

	private String email;

	private String name;

	@Enumerated(EnumType.STRING)
	private Role role;

	private boolean isBlocked = false;

	public UserDto(User user) {
		this.id = user.getId();
		this.email = user.getEmail();
		this.name = user.getName();
		this.role = user.getRole();
		this.isBlocked = !user.isAccountNonLocked();
	}
}

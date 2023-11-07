package com.ev.evserver.authentication;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public abstract class AuthData {

	protected String email;

	protected String password;
}

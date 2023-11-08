package com.ev.evserver.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/auth",
	consumes = MediaType.APPLICATION_JSON_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping("/register")
	public ResponseEntity<AuthenticationDto> register(@RequestBody RegistrationDto registrationDto) {

		return ResponseEntity.ok(authService.register(registrationDto));
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationDto> login(@RequestBody LoginDto loginDto) {

		return ResponseEntity.ok(authService.login(loginDto));
	}


}

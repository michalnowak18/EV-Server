package com.ev.evserver.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class AuthController {

	private final AuthService authService;

	@Autowired
	public AuthController(AuthService authService) {
		this.authService = authService;
	}

	@PostMapping("/register")
	public ResponseEntity<AuthenticationDto> register(@RequestBody RegistrationDto registrationDto) {

		AuthenticationDto authenticationDto = authService.register(registrationDto);
		if (authenticationDto != null) {
			return ResponseEntity.ok(authenticationDto);
		}

		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	@PostMapping("/login")
	public ResponseEntity<AuthenticationDto> login(@RequestBody LoginDto loginDto) {

		return ResponseEntity.ok(authService.login(loginDto));
	}


}

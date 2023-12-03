package com.ev.evserver.authentication;

import com.ev.evserver.recruiter.surveys.SurveysUtils;
import com.ev.evserver.security.JwtService;
import com.ev.evserver.user.User;
import com.ev.evserver.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final JwtService jwtService;

	private final AuthenticationManager authenticationManager;

	@Autowired
	public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
		this.jwtService = jwtService;
		this.authenticationManager = authenticationManager;
	}

	public AuthenticationDto register(RegistrationDto registrationDto) {

		String password = SurveysUtils.generateCode();

		User user = User.builder()
			.email(registrationDto.getEmail())
			.name(registrationDto.getName())
			.password(passwordEncoder.encode(password))
			.role(registrationDto.getRole())
			.build();

		User savedUser = userRepository.save(user);

		return AuthenticationDto.builder()
			.token(jwtService.generateToken(user))
			.password(password)
			.role(registrationDto.getRole())
			.userId(savedUser.getId())
			.name(savedUser.getName())
			.build();
	}

	public AuthenticationDto login(LoginDto loginDto) {

		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
			loginDto.getPassword()));

		User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow();

		return AuthenticationDto.builder()
			.token(jwtService.generateToken(user))
			.role(user.getRole())
			.userId(user.getId())
			.name(user.getName())
			.build();
	}
}

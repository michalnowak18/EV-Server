package com.ev.evserver.authentication;

import com.ev.evserver.recruiter.surveys.SurveysUtils;
import com.ev.evserver.security.JwtService;
import com.ev.evserver.user.Role;
import com.ev.evserver.user.User;
import com.ev.evserver.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

	private final UserRepository userRepository;

	private final PasswordEncoder passwordEncoder;

	private final JwtService jwtService;

	private final AuthenticationManager authenticationManager;

	private final SurveysUtils surveysUtils;

	public AuthenticationDto register(RegistrationDto registrationDto) {

		User user = User.builder()
			.email(registrationDto.getEmail())
			.name(registrationDto.getName())
			.password(passwordEncoder.encode(surveysUtils.generateCode()))
			.role(Role.RECRUITER)
			.build();

		userRepository.save(user);

		return AuthenticationDto.builder()
			.token(jwtService.generateToken(user))
			.build();
	}

	public AuthenticationDto login(LoginDto loginDto) {

		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(),
			loginDto.getPassword()));

		User user = userRepository.findByEmail(loginDto.getEmail()).orElseThrow();

		return AuthenticationDto.builder()
			.token(jwtService.generateToken(user))
			.build();
	}
}

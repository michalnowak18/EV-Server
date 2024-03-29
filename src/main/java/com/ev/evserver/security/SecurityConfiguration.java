package com.ev.evserver.security;

import com.ev.evserver.user.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration  {

	private final JwtAuthenticationFilter jwtAuthFilter;

	private final AuthenticationProvider authenticationProvider;

	@Autowired
	public SecurityConfiguration(JwtAuthenticationFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.authenticationProvider = authenticationProvider;
	}

	@Bean
	@Profile("prod")
	public SecurityFilterChain securityFilterChainProd(HttpSecurity httpSecurity) throws Exception {

		httpSecurity
			.csrf()
			.disable()
			.cors()
			.and()
			.authorizeHttpRequests()
				.requestMatchers(ALL_METHODS_WHITE_LIST).permitAll()
				.requestMatchers(GET, GET_WHITE_LIST).permitAll()
				.requestMatchers(POST, POST_PERMITTED_FOR_RECRUITER_AND_ADMIN).hasAnyAuthority(Role.RECRUITER.name(), Role.ADMIN.name())
				.requestMatchers(PATCH, PATCH_PERMITTED_FOR_RECRUITER_AND_ADMIN).hasAnyAuthority(Role.RECRUITER.name(), Role.ADMIN.name())
				.requestMatchers(ADMIN_EXCLUSIONS).hasAnyAuthority(Role.RECRUITER.name(), Role.ADMIN.name(), Role.READONLY.name())
				.requestMatchers(PERMITTED_FOR_ADMIN).hasAuthority(Role.ADMIN.name())
				.anyRequest().authenticated()
			.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authenticationProvider(authenticationProvider)
			.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return httpSecurity.build();
	}

	@Bean
	@Profile("test")
	public SecurityFilterChain securityFilterChainTest(HttpSecurity httpSecurity) throws Exception {

		httpSecurity
			.csrf()
			.disable()
			.cors()
			.disable();

		return httpSecurity.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "https://easyvisit.netlify.app"));
		configuration.setAllowedMethods(Arrays.asList("GET","POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
		configuration.setExposedHeaders(Arrays.asList("Authorization", "content-type"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "content-type"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	private final static String[] ALL_METHODS_WHITE_LIST = new String[] {
		"/auth/**", "/surveys/**", "consents/**"};

	private final static String[] GET_WHITE_LIST = new String[] {
		"users/{userId}/events/{id}",
		"events/{eventId}/availabilities"};

	private final static String[] POST_PERMITTED_FOR_RECRUITER_AND_ADMIN = new String[] {
		"users/{userId}/events",
		"users/{userId}/events/**,",
		"consents/events/{eventId}"};

	private final static String[] PATCH_PERMITTED_FOR_RECRUITER_AND_ADMIN = new String[] {
		"users/{userId}/events/**"};

	private final static String[] PERMITTED_FOR_ADMIN = new String[] {
		"/admin/**"};

	private final static String[] ADMIN_EXCLUSIONS = new String[] {
		"/admin/users/{id}/changePassword"};
}

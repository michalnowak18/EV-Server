package com.ev.evserver.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserUtils {

	UserRepository userRepository;

	@Autowired
	public UserUtils(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User fetchValidUser(long id) {

		Optional<User> userOpt = userRepository.findById(id);
		if (userOpt.isEmpty()) {
			throw new RuntimeException("Invalid ID");
		}

		return userOpt.get();
	}
}

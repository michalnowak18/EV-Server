package com.ev.evserver.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

	private final UserRepository userRepository;

	private final UserUtils userUtils;

	@Autowired
	public UsersService(UserRepository userRepository, UserUtils userUtils) {
		this.userRepository = userRepository;
		this.userUtils = userUtils;
	}

	public List<UserDto> getAllUsers() {

		List<User> userList = userRepository.findAll();

		return userList.stream().map(UserDto::new).toList();
	}

	public UserDto getUser(long id) {

		User user = userUtils.fetchValidUser(id);

		return new UserDto(user);
	}

	public UserDto changeUserStatus(long id, boolean isToBeBlocked) {

		User user = userUtils.fetchValidUser(id);
		user.setBlocked(isToBeBlocked);
		userRepository.save(user);

		return new UserDto(user);
	}
}

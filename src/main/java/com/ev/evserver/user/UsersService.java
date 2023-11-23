package com.ev.evserver.user;

import com.ev.evserver.recruiter.events.Event;
import com.ev.evserver.recruiter.events.EventDto;
import com.ev.evserver.recruiter.events.EventRepository;
import com.ev.evserver.recruiter.events.EventsUtils;
import com.ev.evserver.recruiter.surveys.SurveysUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsersService {

	private final UserRepository userRepository;

	private final UserUtils userUtils;

	private final PasswordEncoder passwordEncoder;

	private final EventsUtils eventsUtils;
	private final EventRepository eventRepository;

	@Autowired
	public UsersService(UserRepository userRepository, UserUtils userUtils, PasswordEncoder passwordEncoder,
	                    EventsUtils eventsUtils, EventRepository eventRepository) {
		this.userRepository = userRepository;
		this.userUtils = userUtils;
		this.passwordEncoder = passwordEncoder;
		this.eventsUtils = eventsUtils;
		this.eventRepository = eventRepository;
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

	public PasswordDto changeUserPassword(Long id, PasswordDto password) {

		User newUser = userUtils.fetchValidUser(id);
		newUser.setPassword(passwordEncoder.encode(password.getPassword()));
		userRepository.save(newUser);

		return password;
	}

	public PasswordDto resetUserPassword(Long id) {

		PasswordDto password = new PasswordDto();
		password.setPassword(SurveysUtils.generateCode());

		User newUser = userUtils.fetchValidUser(id);
		newUser.setPassword(passwordEncoder.encode(password.getPassword()));
		userRepository.save(newUser);

		return password;
	}

	public EventDto reassignUser(Long id, Long eventId) {

		Event event = eventsUtils.fetchValidEvent(eventId);
		User user = userUtils.fetchValidUser(id);

		event.setUser(user);
		eventRepository.save(event);

		return new EventDto(event);
	}
}

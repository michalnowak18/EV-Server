package com.ev.evserver.user;

import com.ev.evserver.recruiter.events.EventDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/users",
	consumes = MediaType.APPLICATION_JSON_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE)
public class UsersController {

	private final UsersService usersService;

	@Autowired
	public UsersController(UsersService usersService) {
		this.usersService = usersService;
	}

	@GetMapping
	public ResponseEntity<List<UserDto>> getAll() {
		return new ResponseEntity<>(usersService.getAllUsers(), HttpStatus.OK);
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<UserDto> get(@PathVariable Long id) {
		return new ResponseEntity<>(usersService.getUser(id), HttpStatus.OK);
	}

	@PostMapping(path = "/{id}/block")
	public ResponseEntity<UserDto> block(@PathVariable Long id) {
		return new ResponseEntity<>(usersService.changeUserStatus(id, true), HttpStatus.OK);
	}

	@PostMapping(path = "/{id}/unblock")
	public ResponseEntity<UserDto> unblock(@PathVariable Long id) {
		return new ResponseEntity<>(usersService.changeUserStatus(id, false), HttpStatus.OK);
	}

	@PatchMapping(path = "/{id}/changePassword")
	public ResponseEntity<PasswordDto> changePassword(@RequestBody PasswordDto passwordDto,
											   @PathVariable Long id) {

		PasswordDto passwordDtoResponse = usersService.changeUserPassword(id, passwordDto);
		if (passwordDtoResponse != null) {
			return new ResponseEntity<>(passwordDtoResponse, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
	}

	@PatchMapping(path = "/{id}/resetPassword")
	public ResponseEntity<PasswordDto> resetPassword(@PathVariable Long id) {

		return new ResponseEntity<>(usersService.resetUserPassword(id), HttpStatus.OK);
	}

	@PatchMapping(path = "/{id}/reassignUser/{eventId}")
	public ResponseEntity<EventDto> reassignUser(@PathVariable Long id, @PathVariable Long eventId) {

		return new ResponseEntity<>(usersService.reassignUser(id, eventId), HttpStatus.OK);
	}
}

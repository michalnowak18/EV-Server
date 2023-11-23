package com.ev.evserver.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/admin/users",
	consumes = MediaType.APPLICATION_JSON_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UsersController {

	private final UsersService usersService;

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


		return new ResponseEntity<>(usersService.changeUserPassword(id, passwordDto), HttpStatus.OK);

	}

	@PatchMapping(path = "/{id}/resetPassword")
	public ResponseEntity<PasswordDto> resetPassword(@PathVariable Long id) {

		return new ResponseEntity<>(usersService.resetUserPassword(id), HttpStatus.OK);
	}
}

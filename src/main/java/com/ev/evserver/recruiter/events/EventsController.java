package com.ev.evserver.recruiter.events;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(
consumes = MediaType.APPLICATION_JSON_VALUE,
produces = MediaType.APPLICATION_JSON_VALUE)
public class EventsController {

	private final EventsService eventsService;

	@Autowired
	public EventsController(EventsService eventsService) {
		this.eventsService = eventsService;
	}

	@GetMapping("/events")
	public ResponseEntity<List<EventDto>> getAll() {

		return new ResponseEntity<>(eventsService.getAllEvents(), HttpStatus.OK);
	}

	@GetMapping("/users/{userId}/events")
	public ResponseEntity<List<EventDto>> getAllByUser(@PathVariable Long userId) {

		return new ResponseEntity<>(eventsService.getAllEventsByUser(userId), HttpStatus.OK);
	}

	@GetMapping(path = "/users/{userId}/events/{id}")
	public ResponseEntity<EventDto> get(@PathVariable Long id) {

		return new ResponseEntity<>(eventsService.getEvent(id), HttpStatus.OK);
	}

	@PostMapping("/users/{userId}/events")
	public ResponseEntity<EventDto> save(@Valid @RequestBody EventDto eventDto, @PathVariable Long userId) {

		EventDto newEventDto = eventsService.saveEvent(eventDto, userId);

		return new ResponseEntity<>(newEventDto, HttpStatus.OK);

	}

	@PatchMapping(path = "/users/{userId}/events/{id}")
	public ResponseEntity<Object> modifyEvent(@RequestBody EventDto eventDto,
												@PathVariable Long id) {

		EventDto newEventDto;

		try {
			newEventDto = eventsService.modifyEvent(eventDto, id);
		} catch (RuntimeException exception) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
		}

		if (newEventDto != null) {
			return new ResponseEntity<>(newEventDto, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}
}

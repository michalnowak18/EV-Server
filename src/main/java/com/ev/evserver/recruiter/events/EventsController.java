package com.ev.evserver.recruiter.events;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/events",
consumes = MediaType.APPLICATION_JSON_VALUE,
produces = MediaType.APPLICATION_JSON_VALUE)
public class EventsController {

	private final EventsService eventsService;

	@Autowired
	public EventsController(EventsService eventsService) {
		this.eventsService = eventsService;
	}

	@GetMapping
	public List<Event> getAll() {
		return eventsService.getAllEvents();
	}

	@PostMapping
	public ResponseEntity<Event> save(@Valid @RequestBody Event event) {
		return new ResponseEntity<>(eventsService.saveEvent(event), HttpStatus.OK);
	}
}

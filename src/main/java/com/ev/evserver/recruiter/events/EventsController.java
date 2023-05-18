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
	public List<EventDto> getAll() {
		return eventsService.getAllEvents();
	}

	@GetMapping(path = "/{id}")
	public EventDto get(@PathVariable int id) {
		return eventsService.getEvent(id);
	}

	@PostMapping
	public ResponseEntity<EventDto> save(@Valid @RequestBody EventDto eventDto) {
		return new ResponseEntity<>(eventsService.saveEvent(eventDto), HttpStatus.OK);
	}
}

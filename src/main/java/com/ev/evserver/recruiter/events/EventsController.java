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
	public ResponseEntity<List<EventDto>> getAll() {
		return new ResponseEntity<>(eventsService.getAllEvents(), HttpStatus.OK);
	}

	@GetMapping(path = "/{id}")
	public ResponseEntity<EventDto> get(@PathVariable Long id) {
		return new ResponseEntity<>(eventsService.getEvent(id), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<EventDto> save(@Valid @RequestBody EventDto eventDto) {

		EventDto newEventDto = eventsService.saveEvent(eventDto);

		if (newEventDto != null) {
			return new ResponseEntity<>(newEventDto, HttpStatus.OK);
		}

		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PatchMapping(path = "/{id}")
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

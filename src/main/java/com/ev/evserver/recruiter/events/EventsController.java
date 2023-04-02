package com.ev.evserver.recruiter.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventsController {

	@Autowired
	EventRepository eventRepository;

	@GetMapping
	public List<Event> getAll() {
		return eventRepository.findAll();
	}

	@PostMapping
	public Event save(@RequestBody Event event) {
		return eventRepository.save(event);
	}
}

package com.ev.evserver.recruiter.events;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

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

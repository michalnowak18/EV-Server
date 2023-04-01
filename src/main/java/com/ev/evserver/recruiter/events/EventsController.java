package com.ev.evserver.recruiter.events;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/events")
public class EventsController {

	// TODO: Uncomment after EventRepository and Event is added
//	@Autowired
//	EventRepository eventRepository;
//
//	@GetMapping
//	public List<Event> getAll() {
//		return eventRepository.findAll();
//	}
//
//	@PostMapping
//	public List<Event> save(@RequestBody Event event) {
//		return eventRepository.save(event);
//	}
}

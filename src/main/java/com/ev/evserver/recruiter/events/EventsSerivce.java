package com.ev.evserver.recruiter.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventsSerivce {

	@Autowired
	EventRepository eventRepository;

	public List<Event> getAllEvents() {
		return eventRepository.findAll();
	}

	public Event saveEvent(Event event) {
		return eventRepository.save(event);
	}
}

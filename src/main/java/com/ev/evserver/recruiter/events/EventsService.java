package com.ev.evserver.recruiter.events;

import com.ev.evserver.recruiter.surveys.SurveysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventsService {

	@Autowired
	EventRepository eventRepository;

	@Autowired
	SurveysService surveysService;

	public List<Event> getAllEvents() {
		return eventRepository.findAll();
	}

	public Event saveEvent(Event event) {
		surveysService.saveSurveyWithGeneratedSlots(event.getMaxUsers());
		return eventRepository.save(event);
	}
}

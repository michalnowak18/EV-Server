package com.ev.evserver.recruiter.events;

import com.ev.evserver.recruiter.surveys.SurveysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EventsService {

	private final EventRepository eventRepository;

	private final SurveysService surveysService;

	@Autowired
	public EventsService(EventRepository eventRepository, SurveysService surveysService) {
		this.eventRepository = eventRepository;
		this.surveysService = surveysService;
	}

	public List<EventDto> getAllEvents() {

		List<Event> eventList = eventRepository.findAll();
		List<EventDto> eventDtoList = eventList.stream().map(event -> new EventDto(event)).collect(Collectors.toList());

		return eventDtoList;
	}

	public EventDto saveEvent(EventDto eventDto) {

		Event event = new Event(eventDto);
		surveysService.saveSurveyWithGeneratedSlots(event.getMaxUsers());

		return new EventDto(eventRepository.save(event));
	}
}

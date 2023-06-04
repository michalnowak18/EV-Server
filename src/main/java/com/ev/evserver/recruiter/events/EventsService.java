package com.ev.evserver.recruiter.events;

import com.ev.evserver.recruiter.surveys.SurveysService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
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
		List<EventDto> eventDtoList = eventList.stream().map(EventDto::new).collect(Collectors.toList());

		return eventDtoList;
	}

	public EventDto getEvent(long id) {

		Event event = fetchValidEvent(id);
		return new EventDto(event);
	}

	public Event fetchValidEvent(long id) {
		Optional<Event> eventOpt = eventRepository.findById(id);
		if (eventOpt.isEmpty()) {
			throw new RuntimeException("Invalid ID");
		}

		return eventOpt.get();
	}

	public EventDto saveEvent(EventDto eventDto) {

		Event event = new Event(eventDto);

		Event savedEvent = eventRepository.save(event);

		if (savedEvent != null) {
			surveysService.saveSurveyWithGeneratedSlots(event.getMaxUsers(), savedEvent);
			return new EventDto(savedEvent);
		}

		return null;
	}
}

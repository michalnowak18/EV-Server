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

	private final EventsUtils eventsUtils;

	@Autowired
	public EventsService(EventRepository eventRepository, SurveysService surveysService,
						 EventsUtils eventsUtils) {
		this.eventRepository = eventRepository;
		this.surveysService = surveysService;
		this.eventsUtils = eventsUtils;
	}

	public List<EventDto> getAllEvents() {

		List<Event> eventList = eventRepository.findAll();
		List<EventDto> eventDtoList = eventList.stream().map(EventDto::new).collect(Collectors.toList());

		return eventDtoList;
	}

	public EventDto getEvent(long id) {

		Event event = eventsUtils.fetchValidEvent(id);
		return new EventDto(event);
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

	public EventDto modifyEvent(EventDto eventDto, Long id) {

		Event event = eventsUtils.fetchValidEvent(id);
		Event newEvent = new Event(eventDto);

		if(event.isActive() && !newEvent.isActive()) {
			event.setActive(false);
			event.setSlotsTaken(0);
			surveysService.deactivateAllCodes(event);
		}

		Event savedEvent = eventRepository.save(event);

		return new EventDto(savedEvent);
	}
}

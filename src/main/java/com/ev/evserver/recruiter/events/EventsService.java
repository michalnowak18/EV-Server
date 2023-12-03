package com.ev.evserver.recruiter.events;

import com.ev.evserver.recruiter.surveys.SurveysService;
import com.ev.evserver.user.User;
import com.ev.evserver.user.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class EventsService {

	private final EventRepository eventRepository;

	private final SurveysService surveysService;

	private final EventsUtils eventsUtils;

	private final UserUtils userUtils;

	@Autowired
	public EventsService(EventRepository eventRepository, SurveysService surveysService, EventsUtils eventsUtils, UserUtils userUtils) {
		this.eventRepository = eventRepository;
		this.surveysService = surveysService;
		this.eventsUtils = eventsUtils;
		this.userUtils = userUtils;
	}

	public List<EventDto> getAllEvents() {

		List<Event> eventList = eventRepository.findAll();

		return eventList.stream().map(EventDto::new).collect(Collectors.toList());
	}

	public List<EventDto> getAllEventsByUser(Long userId) {

		User user = userUtils.fetchValidUser(userId);
		Set<Event> eventList = eventRepository.findByUser(user);

		return eventList.stream().map(EventDto::new).collect(Collectors.toList());
	}

	public EventDto getEvent(long id) {

		Event event = eventsUtils.fetchValidEvent(id);
		return new EventDto(event);
	}

	public EventDto saveEvent(EventDto eventDto, Long userId) {

		Event event = new Event(eventDto);
		event.setUser(userUtils.fetchValidUser(userId));

		Event savedEvent = eventRepository.save(event);

		surveysService.saveSurveyWithGeneratedSlots(event.getMaxUsers(), savedEvent);
		return new EventDto(savedEvent);

	}

	public EventDto modifyEvent(EventDto eventDto, Long id) {

		Event event = eventsUtils.fetchValidEvent(id);
		Event newEvent = new Event(eventDto);

		if (event.isActive() && !newEvent.isActive()) {

			event.setActive(false);
			event.setSlotsTaken(0);
			surveysService.deactivateAllCodes(event);
		}

		if (newEvent.getName() != null) {

			event.setName(newEvent.getName());
		}

		if (newEvent.getDescription() != null) {

			event.setDescription(newEvent.getDescription());
		}

		if (newEvent.getSurveyDuration() != null) {

			event.setSurveyDuration(newEvent.getSurveyDuration());
		}

		if (newEvent.getSurveyBreakTime() != null) {

			event.setSurveyBreakTime(newEvent.getSurveyBreakTime());
		}

		if (newEvent.getEndDate() != null) {

			event.setEndDate(newEvent.getEndDate());
		}

		if (newEvent.getMaxUsers() != null && newEvent.getMaxUsers() >= event.getSlotsTaken()) {

			event.setMaxUsers(newEvent.getMaxUsers());
		}

		modifyOnlyStartDate(event, newEvent);

		modifyOnlyEndDate(event, newEvent);

		modifyStartDateAndEndDate(event, newEvent);

		Event savedEvent = eventRepository.save(event);

		return new EventDto(savedEvent);
	}

	public void modifyOnlyStartDate(Event event, Event newEvent) {

		if (newEvent.getResearchStartDate() != null && newEvent.getResearchEndDate() == null) {

			if (newEvent.getResearchStartDate().after(event.getResearchEndDate())) {
				throw new RuntimeException("Podana data rozpoczęcia badań nie może być większa niż data zakończenia.");
			}
			event.setResearchStartDate(newEvent.getResearchStartDate());
		}
	}

	public void modifyOnlyEndDate(Event event, Event newEvent) {

		if (newEvent.getResearchEndDate() != null && newEvent.getResearchStartDate() == null) {

			if (newEvent.getResearchEndDate().before(event.getResearchStartDate())) {
				throw new RuntimeException("Podana data zakończenia badań nie może być mniejsza niż data rozpoczęcia.");
			}
			event.setResearchEndDate(newEvent.getResearchEndDate());
		}
	}

	public void modifyStartDateAndEndDate(Event event, Event newEvent) {

		if (newEvent.getResearchEndDate() != null && newEvent.getResearchStartDate() != null) {

			if (newEvent.getResearchEndDate().before(newEvent.getResearchStartDate())) {
				throw new RuntimeException("Podana data zakończenia badań nie może być mniejsza niż data rozpoczęcia.");
			}
			event.setResearchStartDate(newEvent.getResearchStartDate());
			event.setResearchEndDate(newEvent.getResearchEndDate());
		}
	}
}

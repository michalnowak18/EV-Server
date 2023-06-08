package com.ev.evserver.recruiter.surveys;

import com.ev.evserver.recruiter.availability.Availability;
import com.ev.evserver.recruiter.availability.AvailabilityDto;
import com.ev.evserver.recruiter.events.Event;
import com.ev.evserver.recruiter.events.EventRepository;
import com.ev.evserver.recruiter.events.EventsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SurveysService {

	private final SurveyRepository surveyRepository;

	private final EventRepository eventRepository;

	private final EventsService eventsService;

	@Autowired
	public SurveysService(SurveyRepository surveyRepository, EventRepository eventRepository,
						  EventsService eventsService) {
		this.surveyRepository = surveyRepository;
		this.eventRepository = eventRepository;
		this.eventsService = eventsService;
	}

	public List<Survey> saveSurveyWithGeneratedSlots(int numberOfSlots, Event event) {

		List<Survey> newSurveys = generateSlots(numberOfSlots, event);
		return surveyRepository.saveAll(newSurveys);
	}

	List<Survey> generateSlots(int numberOfSlots, Event event) {

		List<String> generatedCodes;
		Set<String> uniqueCodes;

		do {
			generatedCodes = SurveysUtils.generateCodes(numberOfSlots);
			uniqueCodes = new HashSet<>(generatedCodes);
		} while (uniqueCodes.size() != numberOfSlots);

		List<Survey> newSurveys = new ArrayList<>();
		for (String code: generatedCodes) {
			newSurveys.add(new Survey(code, event));
		}
		return newSurveys;
	}

	public SurveyDto findByCode(String code) {
		Survey survey = surveyRepository.findByCode(code);
		SurveyDto surveyDto = new SurveyDto(survey);

		return surveyDto;
	}

	public SurveyDto modifySurvey(Long id, SurveyDto surveyDto) {

		Survey newSurvey = new Survey(surveyDto);

		Survey survey = surveyRepository.findById(id).orElseThrow();
		Event event = survey.getEvent();

		if (event.getSlotsTaken() == event.getMaxUsers()) {
			return null;
		}

		survey.setDate(newSurvey.getDate());
		SurveyDto newSurveyDto = new SurveyDto(surveyRepository.save(survey));
		event.setSlotsTaken(event.getSlotsTaken() + 1);
		eventRepository.save(event);

		return newSurveyDto;
	}

	public List<SurveyDto> findByEvent(long eventId) {

		Event event = eventsService.fetchValidEvent(eventId);
		Set<Survey> surveys = surveyRepository.findByEvent(event);
		List<SurveyDto> surveyDtoList = surveys
				.stream()
				.map(SurveyDto::new)
				.collect(Collectors.toList());

		return surveyDtoList;
	}

}

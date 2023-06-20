package com.ev.evserver.recruiter.surveys;

import com.ev.evserver.recruiter.events.Event;
import com.ev.evserver.recruiter.events.EventRepository;
import com.ev.evserver.recruiter.events.EventsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SurveysService {

	private final SurveyRepository surveyRepository;

	private final EventRepository eventRepository;

	private final EventsUtils eventsUtils;

	@Autowired
	public SurveysService(SurveyRepository surveyRepository, EventRepository eventRepository,
						  EventsUtils eventsUtils) {
		this.surveyRepository = surveyRepository;
		this.eventRepository = eventRepository;
		this.eventsUtils = eventsUtils;
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

		//case survey is new
		if (newSurvey.getDate() != null
			&& survey.getDate() == null
			&& !event.isFull()) {

			survey.setDate(newSurvey.getDate());
			survey.setSurveyState(SurveyState.USED);

			event.setSlotsTaken(event.getSlotsTaken() + 1);
			eventRepository.save(event);

			//case survey is new and event is full
		} else if (newSurvey.getDate() != null
		            && event.isFull()
					&& newSurvey.getSurveyState() != SurveyState.INACTIVE) {

			return null;
		}

		//case survey is going to be deactivated
		if (newSurvey.getSurveyState() == SurveyState.INACTIVE) {
			if (survey.getSurveyState() == SurveyState.USED) {
				event.setSlotsTaken(event.getSlotsTaken() - 1);
				eventRepository.save(event);
			}

			survey.setSurveyState(newSurvey.getSurveyState());
			survey.setDate(null);
		}

		SurveyDto newSurveyDto = new SurveyDto(surveyRepository.save(survey));

		return newSurveyDto;
	}

	public void deactivateAllCodes(Event event) {

		Set<Survey> surveys = surveyRepository.findByEvent(event);
		surveys.forEach(survey -> {
			survey.setSurveyState(SurveyState.INACTIVE);
			survey.setDate(null);
		});
		surveyRepository.saveAll(surveys);
	}

	public List<SurveyDto> findByEvent(long eventId) {

		Event event = eventsUtils.fetchValidEvent(eventId);
		Set<Survey> surveys = surveyRepository.findByEvent(event);
		List<SurveyDto> surveyDtoList = surveys
				.stream()
				.map(SurveyDto::new)
				.collect(Collectors.toList());

		return surveyDtoList;
	}

}

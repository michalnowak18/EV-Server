package com.ev.evserver.recruiter.surveys;

import com.ev.evserver.recruiter.events.Event;
import com.ev.evserver.recruiter.events.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SurveysService {

	private final SurveyRepository surveyRepository;

	private final EventRepository eventRepository;

	@Autowired
	public SurveysService(SurveyRepository surveyRepository, EventRepository eventRepository) {
		this.surveyRepository = surveyRepository;
		this.eventRepository = eventRepository;
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
		SurveyDto surveyDtoNew = new SurveyDto(surveyRepository.save(survey));
		event.setSlotsTaken(event.getSlotsTaken() + 1);
		eventRepository.save(event);

		return surveyDtoNew;
	}
}

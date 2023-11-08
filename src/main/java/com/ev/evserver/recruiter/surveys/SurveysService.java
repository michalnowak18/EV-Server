package com.ev.evserver.recruiter.surveys;

import com.ev.evserver.common.exceptions.EventIsFullException;
import com.ev.evserver.common.exceptions.SlotConflictException;
import com.ev.evserver.recruiter.availability.AvailabilitiesService;
import com.ev.evserver.recruiter.availability.Availability;
import com.ev.evserver.recruiter.availability.AvailabilityDto;
import com.ev.evserver.recruiter.events.Event;
import com.ev.evserver.recruiter.events.EventRepository;
import com.ev.evserver.recruiter.events.EventsUtils;
import com.ev.evserver.recruiter.initialAvailability.InitialAvailabilityDto;
import com.ev.evserver.recruiter.initialAvailability.InitialAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
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

	private final AvailabilitiesService availabilitiesService;

	private final InitialAvailabilityRepository initialAvailabilityRepository;

	@Autowired
	public SurveysService(SurveyRepository surveyRepository, EventRepository eventRepository,
						  EventsUtils eventsUtils, AvailabilitiesService availabilitiesService,
						  InitialAvailabilityRepository initialAvailabilityRepository) {
		this.surveyRepository = surveyRepository;
		this.eventRepository = eventRepository;
		this.eventsUtils = eventsUtils;
		this.availabilitiesService = availabilitiesService;
		this.initialAvailabilityRepository = initialAvailabilityRepository;
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

		Set<Availability> availabilities = event.getAvailabilities();

		List<AvailabilityDto> availabilityDtoList = availabilities
				.stream()
				.map(AvailabilityDto::new)
				.collect(Collectors.toList());

		//case survey is new
		if (newSurvey.getDate() != null && newSurvey.getSurveyState() != SurveyState.INACTIVE) {

			if (survey.getDate() != null || isHourTaken(newSurvey.getDate(), event.getId())) {
				throw new SlotConflictException();

			} else if (event.isFull()) {
				throw new EventIsFullException();

			} else {

				survey.setDate(newSurvey.getDate());
				survey.setSurveyState(SurveyState.USED);

				event.setSlotsTaken(event.getSlotsTaken() + 1);
				eventRepository.save(event);

				availabilitiesService.modifyAvailability(availabilityDtoList, event.getId());

			}
		}

		//case survey is going to be deactivated
		if (newSurvey.getSurveyState() == SurveyState.INACTIVE) {

			if (survey.getSurveyState() == SurveyState.USED) {

				event.setSlotsTaken(event.getSlotsTaken() - 1);
				eventRepository.save(event);
			}

			survey.setSurveyState(newSurvey.getSurveyState());
			survey.setDate(null);

			List<InitialAvailabilityDto> initialAvailabilities = availabilitiesService.getAllInitial(event.getId());

			List<AvailabilityDto> initialAvailabilityList = initialAvailabilities
					.stream()
					.map(a -> new AvailabilityDto(a.getId(), a.getStartDate(), a.getEndDate(), a.getEventId()))
					.collect(Collectors.toList());

			availabilitiesService.modifyAvailability(initialAvailabilityList, event.getId());
		}

		return new SurveyDto(surveyRepository.save(survey));
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

	private boolean isHourTaken(Timestamp newSurveyDate, long eventId) {

		List<SurveyDto> surveyDtoList = findByEvent(eventId);

		return surveyDtoList
				.stream()
				.anyMatch(surveyDto -> surveyDto.getDate() != null && surveyDto.getDate().equals(newSurveyDate));
	}

}

package com.ev.evserver.recruiter.availability;

import com.ev.evserver.recruiter.events.Event;
import com.ev.evserver.recruiter.events.EventsUtils;
import com.ev.evserver.recruiter.surveys.Survey;
import com.ev.evserver.recruiter.surveys.SurveyDto;
import com.ev.evserver.recruiter.surveys.SurveyRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AvailabilitiesService {

	private final AvailabilityRepository availabilityRepository;

	private final EventsUtils eventsUtils;

	private final SurveyRepository surveyRepository;

	@Autowired
	public AvailabilitiesService(AvailabilityRepository availabilityRepository, EventsUtils eventsUtils,
								 SurveyRepository surveyRepository) {
		this.availabilityRepository = availabilityRepository;
		this.eventsUtils = eventsUtils;
		this.surveyRepository = surveyRepository;
	}

	public List<AvailabilityDto> saveAvailabilityList(List<AvailabilityDto> availabilityDtoList, long eventId) {

		Event event = eventsUtils.fetchValidEvent(eventId);

		return saveAll(availabilityDtoList, event);
	}

	public List<AvailabilityDto> getAll(long eventId) {

		Event event = eventsUtils.fetchValidEvent(eventId);
		Set<Availability> availabilities = availabilityRepository.findByEvent(event);
		List<AvailabilityDto> availabilityDtoList = availabilities
				.stream()
				.map(AvailabilityDto::new)
				.collect(Collectors.toList());

		return availabilityDtoList;
	}

	private List<AvailabilityDto> saveAll(List<AvailabilityDto> availabilityDtoList, Event event) {

		return availabilityDtoList.stream()
				.map(availabilityDto -> saveAvailability(availabilityDto, event))
				.collect(Collectors.toList());
	}

	private AvailabilityDto saveAvailability(@Valid AvailabilityDto availabilityDto, Event event) {

		Availability availability = new Availability(availabilityDto);

		availability.setEvent(event);
		event.getAvailabilities().add(availability);

		return new AvailabilityDto(availabilityRepository.save(availability));
	}

	private List<AvailabilityDto> updateIncludingSurvey(AvailabilityDto availabilityDto, SurveyDto surveyDto,
														Event event) {

		Timestamp startDate = availabilityDto.getStartDate();
		Timestamp endDate = availabilityDto.getEndDate();

		Timestamp exclusionStart = new Timestamp(surveyDto.getDate().getTime()
				- (long) (event.getSurveyBreakTime() * 60 * 1000));
		Timestamp exclusionEnd = new Timestamp(surveyDto.getDate().getTime()
				+ (long) (event.getSurveyDuration() * 60 * 1000)
				+ (long) (event.getSurveyBreakTime() * 60 * 1000));

		long surveyDuration = (long) (event.getSurveyDuration() * 60 * 1000);

		AvailabilityDto newAvailabilityDto1 = new AvailabilityDto();
		AvailabilityDto newAvailabilityDto2 = new AvailabilityDto();
		List<AvailabilityDto> finalList = new ArrayList<>();

		if (isAfterOrEqual(exclusionStart,startDate) && isAfterOrEqual(exclusionEnd, endDate)
				&& exclusionStart.after(endDate)) {

			System.out.println("----------------------------------------");
			if (exclusionStart.getTime() - startDate.getTime() >= surveyDuration) {

				System.out.println();
				newAvailabilityDto1.setStartDate(startDate);
				newAvailabilityDto1.setEndDate(endDate);

				finalList.add(newAvailabilityDto1);
			}
		}

//		if (isAfterOrEqual(exclusionStart,startDate) && isAfterOrEqual(exclusionEnd, endDate)
//				&& exclusionStart.before(endDate)) {
//
//		}

		if (exclusionStart.after(startDate) && isAfterOrEqual(exclusionEnd,endDate)
				&& exclusionStart.before(endDate)) {
			System.out.println("----------------------------------------");


			if (exclusionStart.getTime() - startDate.getTime() >= surveyDuration) {

				newAvailabilityDto1.setStartDate(startDate);
				newAvailabilityDto1.setEndDate(exclusionStart);

				finalList.add(newAvailabilityDto1);
			}
		}

		if (exclusionStart.after(startDate) && exclusionEnd.before(endDate)) {
			System.out.println("----------------------------------------");


			if (exclusionStart.getTime() - startDate.getTime() >= surveyDuration) {

				newAvailabilityDto1.setStartDate(startDate);
				newAvailabilityDto1.setEndDate(exclusionStart);

				finalList.add(newAvailabilityDto1);
			}

			if (endDate.getTime() - exclusionEnd.getTime() >= surveyDuration) {

				newAvailabilityDto2.setStartDate(exclusionEnd);
				newAvailabilityDto2.setEndDate(endDate);

				finalList.add(newAvailabilityDto2);
			}
		}
//		if (isBeforeOrEqual(exclusionStart,startDate) && isAfterOrEqual(exclusionEnd,endDate)) {
//
//		}

		if (isBeforeOrEqual(exclusionStart,startDate) && exclusionEnd.before(endDate)
				&& isAfterOrEqual(exclusionEnd,startDate)) {

			System.out.println("--------------------------------------------");
			if (endDate.getTime() - exclusionEnd.getTime() >= surveyDuration) {

				newAvailabilityDto1.setStartDate(exclusionEnd);
				newAvailabilityDto1.setEndDate(endDate);

				finalList.add(newAvailabilityDto1);
			}
		}

		if (isBeforeOrEqual(exclusionStart,startDate) && isBeforeOrEqual(exclusionEnd,endDate)
				&& isBeforeOrEqual(exclusionEnd,startDate)) {

			System.out.println("----------------------------------------");

			if (endDate.getTime() - startDate.getTime() >= surveyDuration) {

				newAvailabilityDto1.setStartDate(startDate);
				newAvailabilityDto1.setEndDate(endDate);

				finalList.add(newAvailabilityDto1);
			}
		}

		return finalList;
	}

	private boolean isAfterOrEqual(Timestamp timestamp1, Timestamp timestamp2) {
		return timestamp1.after(timestamp2) || timestamp1.equals(timestamp2);
	}

	private boolean isBeforeOrEqual(Timestamp timestamp1, Timestamp timestamp2) {
		return timestamp1.before(timestamp2) || timestamp1.equals(timestamp2);
	}

	public List<AvailabilityDto> modifyAvailability(List<AvailabilityDto> availabilityDtoList, Long eventId) {

		Event retrievedEvent = eventsUtils.fetchValidEvent(eventId);

		Set<Survey> surveys = surveyRepository.findByEvent(retrievedEvent);

		availabilityRepository.deleteAll(availabilityRepository.findByEvent(retrievedEvent));

		List<SurveyDto> surveyDtoList = surveys
				.stream()
				.map(SurveyDto::new)
				.collect(Collectors.toList());

		List<AvailabilityDto> availabilityDtoListNew = new ArrayList<>();

		for (SurveyDto surveyDto : surveyDtoList) {

			if (surveyDto.getDate() != null) {

				for (AvailabilityDto availabilityDto : availabilityDtoList) {

					availabilityDtoListNew.addAll(updateIncludingSurvey(availabilityDto, surveyDto, retrievedEvent));
				}

				availabilityRepository.deleteAll(availabilityRepository.findByEvent(retrievedEvent));
				saveAvailabilityList(availabilityDtoListNew, eventId);

			}

			availabilityDtoList.clear();
			availabilityDtoList.addAll(availabilityDtoListNew);
			availabilityDtoListNew.clear();
		}

		return getAll(eventId);
	}

}

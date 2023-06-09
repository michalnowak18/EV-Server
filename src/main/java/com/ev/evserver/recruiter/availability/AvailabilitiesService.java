package com.ev.evserver.recruiter.availability;

import com.ev.evserver.recruiter.events.Event;
import com.ev.evserver.recruiter.events.EventsUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AvailabilitiesService {

	private final AvailabilityRepository availabilityRepository;

	private final EventsUtils eventsUtils;

	@Autowired
	public AvailabilitiesService(AvailabilityRepository availabilityRepository, EventsUtils eventsUtils) {
		this.availabilityRepository = availabilityRepository;
		this.eventsUtils = eventsUtils;
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

	public List<AvailabilityDto> modifyAvailability(List<AvailabilityDto> availabilityDtoList, Long eventId) {

		Event retrievedEvent = eventsUtils.fetchValidEvent(eventId);
		availabilityRepository.deleteAll(availabilityRepository.findByEvent(retrievedEvent));

		return saveAvailabilityList(availabilityDtoList, eventId);
	}

}

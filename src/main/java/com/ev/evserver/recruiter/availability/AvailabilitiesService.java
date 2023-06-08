package com.ev.evserver.recruiter.availability;

import com.ev.evserver.recruiter.events.Event;
import com.ev.evserver.recruiter.events.EventRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AvailabilitiesService {

	private final AvailabilityRepository availabilityRepository;

	private final EventRepository eventRepository;

	@Autowired
	public AvailabilitiesService(AvailabilityRepository availabilityRepository, EventRepository eventRepository) {
		this.availabilityRepository = availabilityRepository;
		this.eventRepository = eventRepository;
	}

	public List<AvailabilityDto> saveAvailabilityList(List<AvailabilityDto> availabilityDtoList, long eventId) {

		Event event = getValidEvent(eventId);

		return saveAll(availabilityDtoList, event);
	}

	public List<AvailabilityDto> getAll(long eventId) {

		Event event = getValidEvent(eventId);
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

	private Event getValidEvent(long eventId) {

		Optional<Event> eventOpt = eventRepository.findById(eventId);
		if (!eventOpt.isPresent()) {
			throw new RuntimeException("Invalid ID");
		}

		return eventOpt.get();
	}

	public List<AvailabilityDto> modifyAvailability(List<AvailabilityDto> availabilityDtoList, Long eventId) {

		Event retrievedEvent = getValidEvent(eventId);
		availabilityRepository.deleteAll(availabilityRepository.findByEvent(retrievedEvent));

		return saveAvailabilityList(availabilityDtoList, eventId);
	}

}

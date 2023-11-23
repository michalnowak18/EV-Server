package com.ev.evserver.recruiter.availability;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/events/{eventId}/availabilities",
	consumes = MediaType.APPLICATION_JSON_VALUE,
	produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AvailabilitiesController {

	private final AvailabilitiesService availabilitiesService;

	@GetMapping
	public ResponseEntity<List<AvailabilityDto>> getAll(@PathVariable Long eventId) {
		return new ResponseEntity<>(availabilitiesService.getAll(eventId), HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<List<AvailabilityDto>> saveAll(@RequestBody @Valid ValidList<AvailabilityDto> availabilityDtoList,
	                                                     @PathVariable Long eventId) {

		availabilitiesService.saveInitialAvailabilityList(availabilityDtoList, eventId);

		return new ResponseEntity<>(availabilitiesService.saveAvailabilityList(availabilityDtoList, eventId), HttpStatus.OK);
	}

	@PatchMapping
	public ResponseEntity<List<AvailabilityDto>> updateAvailabilities(@RequestBody @Valid ValidList<AvailabilityDto> availabilityDtoList,
	                                                                  @PathVariable Long eventId) {

		availabilitiesService.saveInitialAvailabilityList(availabilityDtoList, eventId);

		return new ResponseEntity<>(availabilitiesService.modifyAvailability(availabilityDtoList, eventId), HttpStatus.OK);
	}
}

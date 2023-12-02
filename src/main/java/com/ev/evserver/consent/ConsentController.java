package com.ev.evserver.consent;

import com.ev.evserver.recruiter.availability.ValidList;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/consents",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ConsentController {

    private final ConsentService consentService;

    @Autowired
    public ConsentController(ConsentService consentService) {
        this.consentService = consentService;
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<List<ConsentDto>> getAllByEvent(@PathVariable Long eventId) {

        return new ResponseEntity<>(consentService.getAllByEvent(eventId), HttpStatus.OK);
    }

    @GetMapping("/surveys/{surveyId}")
    public ResponseEntity<List<ConsentDto>> getAllBySurvey(@PathVariable Long surveyId) {

        return new ResponseEntity<>(consentService.getAllBySurvey(surveyId), HttpStatus.OK);
    }

    @PostMapping("/events/{eventId}")
    public ResponseEntity<List<ConsentDto>> saveAll(@RequestBody @Valid ValidList<ConsentDto> consentDtoList,
                                                         @PathVariable Long eventId) {

        return new ResponseEntity<>(consentService.saveConsentList(consentDtoList, eventId), HttpStatus.OK);
    }

    @PatchMapping("/surveys/{surveyId}")
    public ResponseEntity<List<Long>> saveConsentsForSurvey(@RequestBody ValidList<Long> consentIdList,
                                                                  @PathVariable Long surveyId) {

        return new ResponseEntity<>(consentService.saveConsentListForSurvey(consentIdList, surveyId), HttpStatus.OK);
    }
}

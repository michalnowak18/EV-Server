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
@RequestMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
public class ConsentController {

    private final ConsentService consentService;

    @Autowired
    public ConsentController(ConsentService consentService) {
        this.consentService = consentService;
    }

    @GetMapping("/consents/events/{eventId}")
    public ResponseEntity<List<ConsentDto>> getAll(@PathVariable Long eventId) {

        return new ResponseEntity<>(consentService.getAll(eventId), HttpStatus.OK);
    }

    @PostMapping("/consents/events/{eventId}")
    public ResponseEntity<List<ConsentDto>> saveAll(@RequestBody @Valid ValidList<ConsentDto> consentDtoList,
                                                         @PathVariable Long eventId) {

        return new ResponseEntity<>(consentService.saveConsentList(consentDtoList, eventId), HttpStatus.OK);
    }

    @PatchMapping("/consents/surveys/{surveyId}")
    public ResponseEntity<List<Long>> saveConsentsForSurvey(@RequestBody ValidList<Long> consentId,
                                                                  @PathVariable Long surveyId) {

        return new ResponseEntity<>(consentService.saveConsentListForSurvey(consentId, surveyId), HttpStatus.OK);
    }
}

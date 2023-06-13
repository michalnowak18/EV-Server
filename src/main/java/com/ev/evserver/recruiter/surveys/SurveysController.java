package com.ev.evserver.recruiter.surveys;

import com.ev.evserver.recruiter.events.Event;
import com.ev.evserver.recruiter.events.EventsUtils;
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
public class SurveysController {

    private final SurveysService surveysService;

    private final EventsUtils eventsUtils;

    @Autowired
    public SurveysController(SurveysService surveysService, EventsUtils eventsUtils) {
        this.surveysService = surveysService;
        this.eventsUtils = eventsUtils;
    }

    @GetMapping("/surveys/{code}")
    public ResponseEntity<SurveyDto> getSurvey(@PathVariable String code) {

        SurveyDto surveyDto = surveysService.findByCode(code);

        if (surveyDto != null) {
            return new ResponseEntity<>(surveyDto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/events/{eventId}/surveys")
    public ResponseEntity<List<SurveyDto>> getAllSurveys(@PathVariable Long eventId) {

        List<SurveyDto> surveyDto = surveysService.findByEvent(eventId);

        return new ResponseEntity<>(surveyDto, HttpStatus.OK);

    }

    @PatchMapping("/surveys/{id}")
    public ResponseEntity<SurveyDto> modifySurvey(@Valid @RequestBody SurveyDto surveyDto,
                                                  @PathVariable Long id) {

        SurveyDto newSurveyDto = surveysService.modifySurvey(id, surveyDto);

        if (newSurveyDto != null) {
            return new ResponseEntity<>(newSurveyDto, HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/events/{eventId}/surveys")
    public ResponseEntity<SurveyDto> generateSurvey(@PathVariable Long eventId) {

        Event event = eventsUtils.fetchValidEvent(eventId);
        List<Survey> survey = surveysService.saveSurveyWithGeneratedSlots(1, event);

        SurveyDto surveyDto = new SurveyDto(survey.get(0));

        return new ResponseEntity<>(surveyDto, HttpStatus.OK);
    }
}

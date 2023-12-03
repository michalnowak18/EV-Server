package com.ev.evserver.consent;

import com.ev.evserver.recruiter.availability.ValidList;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @GetMapping("/export/{eventId}")
    public void exportAllConsentsToCSV(@PathVariable Long eventId,
                                       HttpServletResponse response) throws IOException {

        response.setContentType("text/csv");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd_HH:mm:ss");
        LocalDateTime currentDateTime = LocalDateTime.now();

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=consents_" + formatter.format(currentDateTime) + ".csv";
        response.setHeader(headerKey, headerValue);
        response.setHeader("Access-Control-Expose-Headers", headerKey);

        List<ConsentDto> listOfConsents = consentService.getAllByEvent(eventId);

        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
        String[] csvHeader = {"Consent ID", "Content", "Mandatory"};
        String[] nameMapping = {"id", "content", "mandatory"};

        csvWriter.writeHeader(csvHeader);

        for (ConsentDto consentDto : listOfConsents) {

            csvWriter.write(consentDto, nameMapping);
        }

        csvWriter.close();

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

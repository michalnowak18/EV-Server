package com.ev.evserver.consent;

import com.ev.evserver.recruiter.events.Event;
import com.ev.evserver.recruiter.events.EventsUtils;
import com.ev.evserver.recruiter.surveys.Survey;
import com.ev.evserver.recruiter.surveys.SurveysUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ConsentService {

    private final EventsUtils eventsUtils;

    private final ConsentsUtils consentsUtils;

    private final ConsentRepository consentRepository;

    private final SurveysUtils surveysUtils;

    @Autowired
    public ConsentService(EventsUtils eventsUtils,
                          ConsentsUtils consentsUtils,
                          ConsentRepository consentRepository,
                          SurveysUtils surveysUtils) {
        this.eventsUtils = eventsUtils;
        this.consentsUtils = consentsUtils;
        this.consentRepository = consentRepository;
        this.surveysUtils = surveysUtils;
    }

    public List<ConsentDto> getAllByEvent(Long eventId) {

        Event event = eventsUtils.fetchValidEvent(eventId);
        Set<Consent> consents = consentRepository.findByEvent(event);
        List<ConsentDto> consentDtoList = consents
                .stream()
                .map(ConsentDto::new)
                .collect(Collectors.toList());

        return consentDtoList;
    }

    public List<ConsentDto> getAllBySurvey(Long surveyId) {

        Survey survey = surveysUtils.fetchValidSurvey(surveyId);
        List<Consent> consents = survey.getConsents();
        List<ConsentDto> consentDtoList = consents
                .stream()
                .map(ConsentDto::new)
                .collect(Collectors.toList());

        return consentDtoList;
    }

    public List<ConsentDto> saveConsentList(List<ConsentDto> consentDtoList, Long eventId) {

        Event event = eventsUtils.fetchValidEvent(eventId);

        return saveAll(consentDtoList, event);
    }

    private List<ConsentDto> saveAll(List<ConsentDto> consentDtoList, Event event) {

        return consentDtoList.stream()
                .map(consentDto -> saveConsent(consentDto, event))
                .collect(Collectors.toList());
    }

    private ConsentDto saveConsent(@Valid ConsentDto consentDto, Event event) {

        Consent consent = new Consent(consentDto);

        consent.setEvent(event);
        event.getConsents().add(consent);

        return new ConsentDto(consentRepository.save(consent));
    }

        public List<Long> saveConsentListForSurvey(List<Long> consentIdList, Long surveyId) {

        Survey survey = surveysUtils.fetchValidSurvey(surveyId);

        for (Long id : consentIdList) {

            Consent newConsent = consentsUtils.fetchValidConsent(id);

            newConsent.getCollectionOfSurveys().add(survey);
            consentRepository.save(newConsent);
        }

        return consentIdList;
    }
}

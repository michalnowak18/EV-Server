package com.ev.evserver.recruiter.availability;

import com.ev.evserver.recruiter.events.Event;
import com.ev.evserver.recruiter.events.EventRepository;
import com.ev.evserver.recruiter.surveys.SurveyRepository;
import com.ev.evserver.recruiter.surveys.SurveysService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ModifyAvailabilityTest {

    private AvailabilitiesService availabilitiesService;

    private AvailabilityRepository availabilityRepository;

    private EventRepository eventRepository;

    private SurveysService surveysService;

    private SurveyRepository surveyRepository;

    @Autowired
    public ModifyAvailabilityTest(AvailabilitiesService availabilitiesService,
                                  AvailabilityRepository availabilityRepository,
                                  EventRepository eventRepository,
                                  SurveysService surveysService,
                                  SurveyRepository surveyRepository) {
        this.availabilitiesService = availabilitiesService;
        this.availabilityRepository = availabilityRepository;
        this.eventRepository = eventRepository;
        this.surveysService = surveysService;
        this.surveyRepository = surveyRepository;
    }

    @Test
    @Transactional
    public void givenAvailabilityListWhenAllSurveysDateIsNull() {

        List<AvailabilityDto> availabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> updatedAvailabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> expectedAvailability = new ArrayList<>();
        Set<Availability> availabilityDtoSet = new HashSet<>();

        /* INITIAL AVAILABILITIES */

        Event event = new Event (

                "eventTest",
                "test",
                Date.valueOf("2022-01-01"),
                3,
                60.0f,
                30.0f,
                0,
                Date.valueOf("2022-02-02"),
                Date.valueOf("2022-02-22"));

        Event savedEvent = eventRepository.save(event);

        AvailabilityDto availabilityDto = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 08:00:00"),
                Timestamp.valueOf("2022-02-02 22:00:00"),
                1L
        );

        availabilityDtoList.add(availabilityDto);

        for (AvailabilityDto availabilityDto1 : availabilityDtoList) {

            Availability newAvailability = new Availability(availabilityDto1);
            availabilityDtoSet.add(newAvailability);
        }

        savedEvent.setAvailabilities(availabilityDtoSet);

        /* NEW AVAILABILITIES */

        AvailabilityDto updatedAvailability1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 08:00:00"),
                Timestamp.valueOf("2022-02-02 10:00:00"),
                savedEvent.getId()
        );

        AvailabilityDto updatedAvailability2 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 12:00:00"),
                Timestamp.valueOf("2022-02-02 16:00:00"),
                savedEvent.getId()
        );

        updatedAvailabilityDtoList.add(updatedAvailability1);
        updatedAvailabilityDtoList.add(updatedAvailability2);

        availabilitiesService.modifyAvailability(updatedAvailabilityDtoList, savedEvent.getId());

        /* EXPECTED AVAILABILITIES */

        AvailabilityDto expectedAvailability1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 08:00:00"),
                Timestamp.valueOf("2022-02-02 10:00:00"),
                savedEvent.getId()
        );

        AvailabilityDto expectedAvailability2 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 12:00:00"),
                Timestamp.valueOf("2022-02-02 16:00:00"),
                savedEvent.getId()
        );

        expectedAvailability.add(expectedAvailability1);
        expectedAvailability.add(expectedAvailability2);

        Assertions.assertArrayEquals(ignoreAvailabilityId(expectedAvailability).toArray(),
                ignoreAvailabilityId(availabilitiesService.getAll(savedEvent.getId())).toArray());

        availabilityRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    public void givenAvailabilityListWhenSurveysDateIsNotNull1() {

        List<AvailabilityDto> availabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> updatedAvailabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> expectedAvailability = new ArrayList<>();
        Set<Availability> availabilitySet = new HashSet<>();

        /* INITIAL AVAILABILITIES */

        Event event = new Event (

                "eventTest",
                "test",
                Date.valueOf("2022-01-01"),
                3,
                30.0f,
                30.0f,
                0,
                Date.valueOf("2022-02-02"),
                Date.valueOf("2022-02-22"));

        Event savedEvent = eventRepository.save(event);

        AvailabilityDto availabilityDto = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 09:00:00"),
                Timestamp.valueOf("2022-02-02 12:00:00"),
                savedEvent.getId()
        );

        availabilityDtoList.add(availabilityDto);

        for (AvailabilityDto availability1 : availabilityDtoList) {

            Availability newAvailability = new Availability (availability1);
            availabilitySet.add(newAvailability);
        }

        savedEvent.setAvailabilities(availabilitySet);

        /* SET SURVEY DATE */

        surveysService.saveSurveyWithGeneratedSlots(savedEvent.getMaxUsers(), savedEvent);

        String code = surveysService.findByEvent(savedEvent.getId()).get(0).getCode();
        surveyRepository.findByCode(code).setDate(Timestamp.valueOf("2022-02-02 09:00:00"));

        availabilitiesService.modifyAvailability(availabilityDtoList, savedEvent.getId());

        /* NEW AVAILABILITIES */

        AvailabilityDto updatedAvailability1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 09:00:00"),
                Timestamp.valueOf("2022-02-02 10:00:00"),
                savedEvent.getId()
        );

        AvailabilityDto updatedAvailability2 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 10:30:00"),
                Timestamp.valueOf("2022-02-02 12:00:00"),
                savedEvent.getId()
        );

        updatedAvailabilityDtoList.add(updatedAvailability1);
        updatedAvailabilityDtoList.add(updatedAvailability2);

        availabilitiesService.modifyAvailability(updatedAvailabilityDtoList, savedEvent.getId());

        /* EXPECTED AVAILABILITIES */

        AvailabilityDto expectedAvailability1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 10:30:00"),
                Timestamp.valueOf("2022-02-02 12:00:00"),
                savedEvent.getId()
        );

        expectedAvailability.add(expectedAvailability1);

        Assertions.assertArrayEquals(ignoreAvailabilityId(expectedAvailability).toArray(),
                ignoreAvailabilityId(availabilitiesService.getAll(savedEvent.getId())).toArray());

        availabilityRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    public void givenAvailabilityListWhenSurveysDateIsNotNull2() {

        List<AvailabilityDto> availabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> updatedAvailabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> expectedAvailability = new ArrayList<>();
        Set<Availability> availabilitySet = new HashSet<>();

        /* INITIAL AVAILABILITIES */

        Event event = new Event (

                "eventTest",
                "test",
                Date.valueOf("2022-01-01"),
                3,
                60.0f,
                30.0f,
                0,
                Date.valueOf("2022-02-02"),
                Date.valueOf("2022-02-22"));

        Event savedEvent = eventRepository.save(event);

        AvailabilityDto availabilityDto = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 08:00:00"),
                Timestamp.valueOf("2022-02-02 13:00:00"),
                savedEvent.getId()
        );

        availabilityDtoList.add(availabilityDto);

        for (AvailabilityDto availability1 : availabilityDtoList) {

            Availability newAvailability = new Availability (availability1);
            availabilitySet.add(newAvailability);
        }

        savedEvent.setAvailabilities(availabilitySet);

        /* SET SURVEY DATE */

        surveysService.saveSurveyWithGeneratedSlots(savedEvent.getMaxUsers(), savedEvent);

        String code = surveysService.findByEvent(savedEvent.getId()).get(0).getCode();
        surveyRepository.findByCode(code).setDate(Timestamp.valueOf("2022-02-02 08:00:00"));

        availabilitiesService.modifyAvailability(availabilityDtoList, savedEvent.getId());

        /* NEW AVAILABILITIES */

        AvailabilityDto updatedAvailability1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 08:00:00"),
                Timestamp.valueOf("2022-02-02 10:00:00"),
                savedEvent.getId()
        );

        AvailabilityDto updatedAvailability2 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 10:30:00"),
                Timestamp.valueOf("2022-02-02 13:00:00"),
                savedEvent.getId()
        );

        updatedAvailabilityDtoList.add(updatedAvailability1);
        updatedAvailabilityDtoList.add(updatedAvailability2);

        availabilitiesService.modifyAvailability(updatedAvailabilityDtoList, savedEvent.getId());

        /* EXPECTED AVAILABILITIES */

        AvailabilityDto expectedAvailability1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 10:30:00"),
                Timestamp.valueOf("2022-02-02 13:00:00"),
                savedEvent.getId()
        );

        expectedAvailability.add(expectedAvailability1);

        Assertions.assertArrayEquals(ignoreAvailabilityId(expectedAvailability).toArray(),
                ignoreAvailabilityId(availabilitiesService.getAll(savedEvent.getId())).toArray());

        availabilityRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    public void givenAvailabilityListWhenSurveysDateIsNotNull3() {

        List<AvailabilityDto> availabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> updatedAvailabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> expectedAvailability = new ArrayList<>();
        Set<Availability> availabilitySet = new HashSet<>();

        /* INITIAL AVAILABILITIES */

        Event event = new Event (

                "eventTest",
                "test",
                Date.valueOf("2022-01-01"),
                3,
                60.0f,
                30.0f,
                0,
                Date.valueOf("2022-02-02"),
                Date.valueOf("2022-02-22"));

        Event savedEvent = eventRepository.save(event);

        AvailabilityDto availabilityDto = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 08:00:00"),
                Timestamp.valueOf("2022-02-02 13:00:00"),
                savedEvent.getId()
        );

        availabilityDtoList.add(availabilityDto);

        for (AvailabilityDto availability1 : availabilityDtoList) {

            Availability newAvailability = new Availability (availability1);
            availabilitySet.add(newAvailability);
        }

        savedEvent.setAvailabilities(availabilitySet);

        /* SET SURVEY DATE */

        surveysService.saveSurveyWithGeneratedSlots(savedEvent.getMaxUsers(), savedEvent);

        String code = surveysService.findByEvent(savedEvent.getId()).get(0).getCode();
        surveyRepository.findByCode(code).setDate(Timestamp.valueOf("2022-02-02 11:30:00"));

        availabilitiesService.modifyAvailability(availabilityDtoList, savedEvent.getId());

        /* NEW AVAILABILITIES */

        AvailabilityDto updatedAvailability1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 08:00:00"),
                Timestamp.valueOf("2022-02-02 10:00:00"),
                savedEvent.getId()
        );

        AvailabilityDto updatedAvailability2 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 10:30:00"),
                Timestamp.valueOf("2022-02-02 13:00:00"),
                savedEvent.getId()
        );

        updatedAvailabilityDtoList.add(updatedAvailability1);
        updatedAvailabilityDtoList.add(updatedAvailability2);

        availabilitiesService.modifyAvailability(updatedAvailabilityDtoList, savedEvent.getId());

        /* EXPECTED AVAILABILITIES */

        AvailabilityDto expectedAvailability1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 08:00:00"),
                Timestamp.valueOf("2022-02-02 10:00:00"),
                savedEvent.getId()
        );

        expectedAvailability.add(expectedAvailability1);

        Assertions.assertArrayEquals(ignoreAvailabilityId(expectedAvailability).toArray(),
                ignoreAvailabilityId(availabilitiesService.getAll(savedEvent.getId())).toArray());

        availabilityRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    public void givenAvailabilityListWhenSurveysDateIsNotNull4() {

        List<AvailabilityDto> availabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> updatedAvailabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> expectedAvailability = new ArrayList<>();
        Set<Availability> availabilitySet = new HashSet<>();

        /* INITIAL AVAILABILITIES */

        Event event = new Event (

                "eventTest",
                "test",
                Date.valueOf("2022-01-01"),
                3,
                60.0f,
                30.0f,
                0,
                Date.valueOf("2022-02-02"),
                Date.valueOf("2022-02-22"));

        Event savedEvent = eventRepository.save(event);

        AvailabilityDto availabilityDto = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 13:00:00"),
                Timestamp.valueOf("2022-02-02 20:00:00"),
                savedEvent.getId()
        );

        availabilityDtoList.add(availabilityDto);

        for (AvailabilityDto availability1 : availabilityDtoList) {

            Availability newAvailability = new Availability (availability1);
            availabilitySet.add(newAvailability);
        }

        savedEvent.setAvailabilities(availabilitySet);

        /* SET SURVEY DATE */

        surveysService.saveSurveyWithGeneratedSlots(savedEvent.getMaxUsers(), savedEvent);

        String code = surveysService.findByEvent(savedEvent.getId()).get(0).getCode();
        surveyRepository.findByCode(code).setDate(Timestamp.valueOf("2022-02-02 18:30:00"));

        availabilitiesService.modifyAvailability(availabilityDtoList, savedEvent.getId());

        /* NEW AVAILABILITIES */

        AvailabilityDto updatedAvailability1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 13:00:00"),
                Timestamp.valueOf("2022-02-02 15:00:00"),
                savedEvent.getId()
        );

        AvailabilityDto updatedAvailability2 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 17:00:00"),
                Timestamp.valueOf("2022-02-02 20:00:00"),
                savedEvent.getId()
        );

        updatedAvailabilityDtoList.add(updatedAvailability1);
        updatedAvailabilityDtoList.add(updatedAvailability2);

        availabilitiesService.modifyAvailability(updatedAvailabilityDtoList, savedEvent.getId());

        /* EXPECTED AVAILABILITIES */

        AvailabilityDto expectedAvailability1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 13:00:00"),
                Timestamp.valueOf("2022-02-02 15:00:00"),
                savedEvent.getId()
        );

        expectedAvailability.add(expectedAvailability1);

        Assertions.assertArrayEquals(ignoreAvailabilityId(expectedAvailability).toArray(),
                ignoreAvailabilityId(availabilitiesService.getAll(savedEvent.getId())).toArray());

        availabilityRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    public void givenAvailabilityListWhenSurveysDateIsNotNull5() {

        List<AvailabilityDto> availabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> updatedAvailabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> expectedAvailability = new ArrayList<>();
        Set<Availability> availabilitySet = new HashSet<>();

        /* INITIAL AVAILABILITIES */

        Event event = new Event (

                "eventTest",
                "test",
                Date.valueOf("2022-01-01"),
                3,
                60.0f,
                30.0f,
                0,
                Date.valueOf("2022-02-02"),
                Date.valueOf("2022-02-22"));

        Event savedEvent = eventRepository.save(event);

        AvailabilityDto availabilityDto1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 13:00:00"),
                Timestamp.valueOf("2022-02-02 15:00:00"),
                savedEvent.getId()
        );

        AvailabilityDto availabilityDto2 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 17:00:00"),
                Timestamp.valueOf("2022-02-02 20:00:00"),
                savedEvent.getId()
        );

        availabilityDtoList.add(availabilityDto1);
        availabilityDtoList.add(availabilityDto2);

        for (AvailabilityDto availability1 : availabilityDtoList) {

            Availability newAvailability = new Availability (availability1);
            availabilitySet.add(newAvailability);
        }

        savedEvent.setAvailabilities(availabilitySet);

        /* SET SURVEY DATE */

        surveysService.saveSurveyWithGeneratedSlots(savedEvent.getMaxUsers(), savedEvent);

        String code = surveysService.findByEvent(savedEvent.getId()).get(0).getCode();
        surveyRepository.findByCode(code).setDate(Timestamp.valueOf("2022-02-02 11:30:00"));

        availabilitiesService.modifyAvailability(availabilityDtoList, savedEvent.getId());

        /* NEW AVAILABILITIES */

        AvailabilityDto updatedAvailability1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 13:00:00"),
                Timestamp.valueOf("2022-02-02 15:00:00"),
                savedEvent.getId()
        );

        AvailabilityDto updatedAvailability2 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 17:00:00"),
                Timestamp.valueOf("2022-02-02 20:00:00"),
                savedEvent.getId()
        );

        updatedAvailabilityDtoList.add(updatedAvailability1);
        updatedAvailabilityDtoList.add(updatedAvailability2);

        availabilitiesService.modifyAvailability(updatedAvailabilityDtoList, savedEvent.getId());

        /* EXPECTED AVAILABILITIES */

        AvailabilityDto expectedAvailability1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 13:00:00"),
                Timestamp.valueOf("2022-02-02 15:00:00"),
                savedEvent.getId()
        );

        AvailabilityDto expectedAvailability2 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 17:00:00"),
                Timestamp.valueOf("2022-02-02 20:00:00"),
                savedEvent.getId()
        );

        expectedAvailability.add(expectedAvailability1);
        expectedAvailability.add(expectedAvailability2);

        Assertions.assertArrayEquals(ignoreAvailabilityId(expectedAvailability).toArray(),
                ignoreAvailabilityId(availabilitiesService.getAll(savedEvent.getId())).toArray());

        availabilityRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    public void givenAvailabilityListWhenSurveysDateIsNotNull6() {

        List<AvailabilityDto> availabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> updatedAvailabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> expectedAvailability = new ArrayList<>();
        Set<Availability> availabilitySet = new HashSet<>();

        /* INITIAL AVAILABILITIES */

        Event event = new Event (

                "eventTest",
                "test",
                Date.valueOf("2022-01-01"),
                3,
                60.0f,
                30.0f,
                0,
                Date.valueOf("2022-02-02"),
                Date.valueOf("2022-02-22"));

        Event savedEvent = eventRepository.save(event);

        AvailabilityDto availabilityDto1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 08:00:00"),
                Timestamp.valueOf("2022-02-02 14:00:00"),
                savedEvent.getId()
        );

        availabilityDtoList.add(availabilityDto1);

        for (AvailabilityDto availability1 : availabilityDtoList) {

            Availability newAvailability = new Availability (availability1);
            availabilitySet.add(newAvailability);
        }

        savedEvent.setAvailabilities(availabilitySet);

        /* SET SURVEY DATE */

        surveysService.saveSurveyWithGeneratedSlots(savedEvent.getMaxUsers(), savedEvent);

        String code = surveysService.findByEvent(savedEvent.getId()).get(0).getCode();
        surveyRepository.findByCode(code).setDate(Timestamp.valueOf("2022-02-02 11:00:00"));

        availabilitiesService.modifyAvailability(availabilityDtoList, savedEvent.getId());

        /* NEW AVAILABILITIES */

        AvailabilityDto updatedAvailability1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 08:00:00"),
                Timestamp.valueOf("2022-02-02 14:00:00"),
                savedEvent.getId()
        );

        updatedAvailabilityDtoList.add(updatedAvailability1);

        availabilitiesService.modifyAvailability(updatedAvailabilityDtoList, savedEvent.getId());

        /* EXPECTED AVAILABILITIES */

        AvailabilityDto expectedAvailability1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 08:00:00"),
                Timestamp.valueOf("2022-02-02 10:30:00"),
                savedEvent.getId()
        );

        AvailabilityDto expectedAvailability2 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 12:30:00"),
                Timestamp.valueOf("2022-02-02 14:00:00"),
                savedEvent.getId()
        );

        expectedAvailability.add(expectedAvailability1);
        expectedAvailability.add(expectedAvailability2);

        Assertions.assertArrayEquals(ignoreAvailabilityId(expectedAvailability).toArray(),
                ignoreAvailabilityId(availabilitiesService.getAll(savedEvent.getId())).toArray());

        availabilityRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    public void givenAvailabilityListWhenSurveysDateIsNotNull7() {

        List<AvailabilityDto> availabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> updatedAvailabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> expectedAvailability = new ArrayList<>();
        Set<Availability> availabilitySet = new HashSet<>();

        /* INITIAL AVAILABILITIES */

        Event event = new Event (

                "eventTest",
                "test",
                Date.valueOf("2022-01-01"),
                3,
                60.0f,
                30.0f,
                0,
                Date.valueOf("2022-02-02"),
                Date.valueOf("2022-02-22"));

        Event savedEvent = eventRepository.save(event);

        AvailabilityDto availabilityDto1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 08:00:00"),
                Timestamp.valueOf("2022-02-02 19:00:00"),
                savedEvent.getId()
        );

        availabilityDtoList.add(availabilityDto1);

        for (AvailabilityDto availability1 : availabilityDtoList) {

            Availability newAvailability = new Availability (availability1);
            availabilitySet.add(newAvailability);
        }

        savedEvent.setAvailabilities(availabilitySet);

        /* SET SURVEY DATE */

        surveysService.saveSurveyWithGeneratedSlots(savedEvent.getMaxUsers(), savedEvent);

        String code = surveysService.findByEvent(savedEvent.getId()).get(0).getCode();
        surveyRepository.findByCode(code).setDate(Timestamp.valueOf("2022-02-02 08:00:00"));

        availabilitiesService.modifyAvailability(availabilityDtoList, savedEvent.getId());

        /* NEW AVAILABILITIES */

        AvailabilityDto updatedAvailability1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 08:00:00"),
                Timestamp.valueOf("2022-02-02 14:00:00"),
                savedEvent.getId()
        );

        AvailabilityDto updatedAvailability2 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 14:30:00"),
                Timestamp.valueOf("2022-02-02 19:00:00"),
                savedEvent.getId()
        );

        updatedAvailabilityDtoList.add(updatedAvailability1);
        updatedAvailabilityDtoList.add(updatedAvailability2);

        availabilitiesService.modifyAvailability(updatedAvailabilityDtoList, savedEvent.getId());

        /* EXPECTED AVAILABILITIES */

        AvailabilityDto expectedAvailability1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 09:30:00"),
                Timestamp.valueOf("2022-02-02 14:00:00"),
                savedEvent.getId()
        );

        AvailabilityDto expectedAvailability2 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 14:30:00"),
                Timestamp.valueOf("2022-02-02 19:00:00"),
                savedEvent.getId()
        );

        expectedAvailability.add(expectedAvailability1);
        expectedAvailability.add(expectedAvailability2);

        Assertions.assertArrayEquals(ignoreAvailabilityId(expectedAvailability).toArray(),
                ignoreAvailabilityId(availabilitiesService.getAll(savedEvent.getId())).toArray());

        availabilityRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    public void givenAvailabilityListWhenSurveysDateIsNotNull8() {

        List<AvailabilityDto> availabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> updatedAvailabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> expectedAvailability = new ArrayList<>();
        Set<Availability> availabilitySet = new HashSet<>();

        /* INITIAL AVAILABILITIES */

        Event event = new Event (

                "eventTest",
                "test",
                Date.valueOf("2022-01-01"),
                3,
                60.0f,
                30.0f,
                0,
                Date.valueOf("2022-02-02"),
                Date.valueOf("2022-02-22"));

        Event savedEvent = eventRepository.save(event);

        AvailabilityDto availabilityDto1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 10:00:00"),
                Timestamp.valueOf("2022-02-02 12:00:00"),
                savedEvent.getId()
        );

        availabilityDtoList.add(availabilityDto1);

        for (AvailabilityDto availability1 : availabilityDtoList) {

            Availability newAvailability = new Availability (availability1);
            availabilitySet.add(newAvailability);
        }

        savedEvent.setAvailabilities(availabilitySet);

        /* SET SURVEY DATE */

        surveysService.saveSurveyWithGeneratedSlots(savedEvent.getMaxUsers(), savedEvent);

        String code = surveysService.findByEvent(savedEvent.getId()).get(0).getCode();
        surveyRepository.findByCode(code).setDate(Timestamp.valueOf("2022-02-02 10:00:00"));

        availabilitiesService.modifyAvailability(availabilityDtoList, savedEvent.getId());

        Assertions.assertArrayEquals(ignoreAvailabilityId(expectedAvailability).toArray(),
                ignoreAvailabilityId(availabilitiesService.getAll(savedEvent.getId())).toArray());

        availabilityRepository.deleteAll();
        eventRepository.deleteAll();
    }

    @Test
    @Transactional
    public void givenAvailabilityListWhenSurveysDateIsNotNull9() {

        List<AvailabilityDto> availabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> updatedAvailabilityDtoList = new ArrayList<>();
        List<AvailabilityDto> expectedAvailability = new ArrayList<>();
        Set<Availability> availabilitySet = new HashSet<>();

        /* INITIAL AVAILABILITIES */

        Event event = new Event (

                "eventTest",
                "test",
                Date.valueOf("2022-01-01"),
                3,
                30.0f,
                30.0f,
                0,
                Date.valueOf("2022-02-02"),
                Date.valueOf("2022-02-22"));

        Event savedEvent = eventRepository.save(event);

        AvailabilityDto availabilityDto1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 09:00:00"),
                Timestamp.valueOf("2022-02-02 14:00:00"),
                savedEvent.getId()
        );

        availabilityDtoList.add(availabilityDto1);

        for (AvailabilityDto availability1 : availabilityDtoList) {

            Availability newAvailability = new Availability (availability1);
            availabilitySet.add(newAvailability);
        }

        savedEvent.setAvailabilities(availabilitySet);

        /* SET SURVEY DATE */

        surveysService.saveSurveyWithGeneratedSlots(savedEvent.getMaxUsers(), savedEvent);

        String code = surveysService.findByEvent(savedEvent.getId()).get(0).getCode();
        surveyRepository.findByCode(code).setDate(Timestamp.valueOf("2022-02-02 13:00:00"));

        availabilitiesService.modifyAvailability(availabilityDtoList, savedEvent.getId());

        /* EXPECTED AVAILABILITIES */

        AvailabilityDto expectedAvailability1 = new AvailabilityDto (

                Timestamp.valueOf("2022-02-02 09:00:00"),
                Timestamp.valueOf("2022-02-02 12:30:00"),
                savedEvent.getId()
        );

        expectedAvailability.add(expectedAvailability1);

        Assertions.assertArrayEquals(ignoreAvailabilityId(expectedAvailability).toArray(),
                ignoreAvailabilityId(availabilitiesService.getAll(savedEvent.getId())).toArray());

        availabilityRepository.deleteAll();
        eventRepository.deleteAll();
    }
    public List<AvailabilityDto> ignoreAvailabilityId(List<AvailabilityDto> availabilityDtoList1) {

        for (AvailabilityDto availabilityDto : availabilityDtoList1) {

            availabilityDto.setId(null);
        }

        return availabilityDtoList1;
    }
}

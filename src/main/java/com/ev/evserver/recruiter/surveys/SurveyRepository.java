package com.ev.evserver.recruiter.surveys;

import com.ev.evserver.recruiter.events.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    Survey findByCode(String code);

    Set<Survey> findByEvent(Event event);
}

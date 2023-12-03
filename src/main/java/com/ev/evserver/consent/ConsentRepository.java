package com.ev.evserver.consent;

import com.ev.evserver.recruiter.events.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface ConsentRepository extends JpaRepository<Consent, Long> {

    Set<Consent> findByEvent(Event event);
}

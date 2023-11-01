package com.ev.evserver.recruiter.initialAvailability;

import com.ev.evserver.recruiter.events.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface InitialAvailabilityRepository extends JpaRepository<InitialAvailability, Long> {

    Set<InitialAvailability> findByEvent(Event event);

}

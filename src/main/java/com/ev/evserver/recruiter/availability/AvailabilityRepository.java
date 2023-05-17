package com.ev.evserver.recruiter.availability;

import com.ev.evserver.recruiter.events.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    long deleteByEvent(Event event);

    Set<Availability> findByEvent(Event event);
}

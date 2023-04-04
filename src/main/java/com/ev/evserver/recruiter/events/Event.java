package com.ev.evserver.recruiter.events;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "event")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "max_users", nullable = false)
    private Integer maxUsers;

    @Column(name = "duration", nullable = false)
    private Float duration;

    @Column(name = "break_time", nullable = false)
    private Float breakTime;

    public Event() {
    }
}
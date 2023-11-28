package com.ev.evserver.consent;

import com.ev.evserver.recruiter.events.Event;
import com.ev.evserver.recruiter.surveys.Survey;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "consent")
public class Consent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "Podaj zawartość zgody")
    @Column(name = "content")
    private String content;

    @Column(name = "is_mandatory")
    private boolean isMandatory = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_event", nullable = false)
    private Event event;

    @ManyToMany(mappedBy = "consents")
    private Set<Survey> surveys;
}

package com.ev.evserver.consent;

import com.ev.evserver.recruiter.events.Event;
import com.ev.evserver.recruiter.surveys.Survey;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;

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
    @Column(name = "consent_id", nullable = false)
    private Long consent_id;

    @NotBlank(message = "Podaj zawartość zgody")
    @Column(name = "content", columnDefinition = "LONGTEXT")
    private String content;

    @Column(name = "is_mandatory")
    private boolean isMandatory = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_event", nullable = false)
    private Event event;

    @ManyToMany(targetEntity = Survey.class,
            cascade = CascadeType.ALL)
    @JoinTable(name = "survey_consents", joinColumns = { @JoinColumn(name = "consent_id") },
            inverseJoinColumns = { @JoinColumn(name = "survey_id") })
    List<Survey> collectionOfSurveys;

    public Consent(ConsentDto consentDto) {
        this.content = consentDto.getContent();
        this.isMandatory = consentDto.isMandatory();
    }
}

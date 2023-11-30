package com.ev.evserver.consent;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConsentDto {

    private Long id;

    @NotBlank(message = "Podaj zawartość zgody")
    private String content;

    private boolean isMandatory = true;

    private Long eventId;

    public ConsentDto() {
    }

    public ConsentDto(Consent consent) {
        this.id = consent.getConsent_id();
        this.content = consent.getContent();
        this.isMandatory = consent.isMandatory();
        this.eventId = consent.getEvent().getId();
    }
}

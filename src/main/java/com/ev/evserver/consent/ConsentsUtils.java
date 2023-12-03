package com.ev.evserver.consent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ConsentsUtils {

    ConsentRepository consentRepository;

    @Autowired
    public ConsentsUtils(ConsentRepository consentRepository) {
        this.consentRepository = consentRepository;
    }

    public Consent fetchValidConsent(Long id) {

        Optional<Consent> consentOpt = consentRepository.findById(id);
        if (consentOpt.isEmpty()) {
            throw new RuntimeException("Invalid ID");
        }

        return consentOpt.get();
    }
}

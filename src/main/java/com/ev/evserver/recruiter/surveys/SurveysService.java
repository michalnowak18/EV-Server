package com.ev.evserver.recruiter.surveys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SurveysService {

	@Autowired
	SurveyRepository surveyRepository;

	public List<Survey> saveSurveyWithGeneratedSlots(int numberOfSlots) {

		List<Survey> newSurveys = generateSlots(numberOfSlots);
		return surveyRepository.saveAll(newSurveys);
	}

	List<Survey> generateSlots(int numberOfSlots) {

		List<String> generatedCodes;
		Set<String> uniqueCodes;

		do {
			generatedCodes = SurveysUtils.generateCodes(numberOfSlots);
			uniqueCodes = new HashSet<>(generatedCodes);
		} while (uniqueCodes.size() != numberOfSlots);

		List<Survey> newSurveys = new ArrayList<>();
		for (String code: generatedCodes) {
			newSurveys.add(new Survey(code));
		}
		return newSurveys;
	}
}

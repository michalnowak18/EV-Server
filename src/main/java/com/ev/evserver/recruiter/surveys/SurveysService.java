package com.ev.evserver.recruiter.surveys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class SurveysService {

	private final SurveyRepository surveyRepository;

	@Autowired
	public SurveysService(SurveyRepository surveyRepository) {
		this.surveyRepository = surveyRepository;
	}

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

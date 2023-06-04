package com.ev.evserver.recruiter.surveys;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@ExtendWith(SpringExtension.class)
public class SurveysServiceTest {

	@InjectMocks
	SurveysService surveysService;

	@Mock
	SurveyRepository surveyRepository;

	private static String CODE_1 = "A1B2C3D4E5F6";

	private static String CODE_2 = "D4E5F6A1B2C3";

	private static List<String> CODES_DUPLICATES = Arrays.asList(
		CODE_1,
		CODE_1);

	private static List<String> CODES_UNIQUE = Arrays.asList(
		CODE_1,
		CODE_2);

	private static final int NUMBER_OF_SLOTS = 2;

	@Test
	void whenGeneratedDuplicatesThenGenerateOnceAgainWithoutThrowingError() {

		List<Survey> surveys;
		try (MockedStatic<SurveysUtils> utils = Mockito.mockStatic(SurveysUtils.class)) {
			utils.when(() -> SurveysUtils.generateCodes(NUMBER_OF_SLOTS))
				.thenReturn(CODES_DUPLICATES)
				.thenReturn(CODES_UNIQUE);

			surveys = surveysService.generateSlots(NUMBER_OF_SLOTS, Mockito.any());
		}

		List<String> codesActual = surveys.stream().map(Survey::getCode).collect(Collectors.toList());
		Assertions.assertEquals(CODES_UNIQUE, codesActual);

	}

	@Test
	void whenGeneratedUniqueCodesThenDontGenerateSecondTime() {

		List<Survey> surveys;
		try (MockedStatic<SurveysUtils> utils = Mockito.mockStatic(SurveysUtils.class)) {
			utils.when(() -> SurveysUtils.generateCodes(NUMBER_OF_SLOTS))
			.thenReturn(CODES_UNIQUE)
			.thenReturn(CODES_DUPLICATES);

			surveys = surveysService.generateSlots(NUMBER_OF_SLOTS, Mockito.any());
		}

		List<String> codesActual = surveys.stream().map(Survey::getCode).collect(Collectors.toList());
		Assertions.assertEquals(CODES_UNIQUE, codesActual);
	}

	@Test
	void shouldGenerateSpecifiedNumberOfSlots() {

		List<Survey> surveys;
		try (MockedStatic<SurveysUtils> utils = Mockito.mockStatic(SurveysUtils.class)) {
			utils.when(() -> SurveysUtils.generateCodes(NUMBER_OF_SLOTS))
			.thenReturn(CODES_UNIQUE);

			surveys = surveysService.generateSlots(NUMBER_OF_SLOTS, Mockito.any());
		}

		Assertions.assertEquals(NUMBER_OF_SLOTS, surveys.size());
	}
}

package com.ev.evserver.recruiter.surveys;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SurveysUtilsTest {

	private static String CODE_1 = "A1B2C3D4E5F6";

	@Test
	void shouldGenerateCode() {
		try (MockedStatic<RandomStringUtils> utils = Mockito.mockStatic(RandomStringUtils.class)) {
			utils.when(() -> RandomStringUtils.randomAlphanumeric(12)).thenReturn(CODE_1);
			String code = SurveysUtils.generateCodes(1).get(0);

			Assertions.assertEquals(CODE_1, code);
		}
	}

	@Test
	void shouldGenerateUniqueCodes() {
		List<String> codes = SurveysUtils.generateCodes(100000);
		Set<String> uniqueCodes = new HashSet<>(codes);

		Assertions.assertEquals(uniqueCodes.size(), codes.size());
	}
}

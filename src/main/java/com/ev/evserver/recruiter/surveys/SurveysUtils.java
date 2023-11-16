package com.ev.evserver.recruiter.surveys;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class SurveysUtils {

	public static String generateCode() {
		return RandomStringUtils.randomAlphanumeric(12);
	}
	
	static List<String> generateCodes(int numberOfSlots) {
		return IntStream.range(0, numberOfSlots)
			.mapToObj(i -> generateCode())
			.collect(Collectors.toList());
	}
}

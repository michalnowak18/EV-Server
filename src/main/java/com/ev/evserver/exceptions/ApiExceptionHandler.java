package com.ev.evserver.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		Map<String, Object> responseBody = new LinkedHashMap<>();
		responseBody.put("timestamp", new Date());
		responseBody.put("status", status.value());

		Map<String, String> errorsMap = ex.getBindingResult().getFieldErrors()
		.stream()
		.collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage));

		responseBody.put("errorsMap", errorsMap);

		return new ResponseEntity<>(responseBody, headers, status);
	}

	@ExceptionHandler({NoSuchElementException.class})
	protected ResponseEntity<Object> handleNoSuchElementException() {
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}


}

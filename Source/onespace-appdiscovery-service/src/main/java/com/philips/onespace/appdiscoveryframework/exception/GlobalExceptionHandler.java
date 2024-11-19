/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: GlobalExceptionHandler.java
 */

package com.philips.onespace.appdiscoveryframework.exception;

import static com.philips.onespace.logging.LoggingAspect.logData;
import static com.philips.onespace.util.ErrorConstants.BAD_REQUEST;
import static com.philips.onespace.util.ErrorConstants.CLIENT_ERROR;
import static com.philips.onespace.util.ErrorConstants.DATABASE_ERROR;
import static com.philips.onespace.util.ErrorConstants.DUPLICATE_KEY;
import static com.philips.onespace.util.ErrorConstants.EMAIL_SOURCE;
import static com.philips.onespace.util.ErrorConstants.ERROR_SOURCE;
import static com.philips.onespace.util.ErrorConstants.HTTP_CLIENT_ERROR;
import static com.philips.onespace.util.ErrorConstants.IAM_SOURCE;
import static com.philips.onespace.util.ErrorConstants.INVALID_CLIENT_ERR_CODE;
import static com.philips.onespace.util.ErrorConstants.INVALID_TOKEN;
import static com.philips.onespace.util.ErrorConstants.SENTINEL_SOURCE;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.philips.onespace.dto.ErrorLog;
import com.philips.onespace.dto.ErrorResponse;
import com.philips.onespace.exception.DatabaseConstraintViolationException;
import com.philips.onespace.exception.EntitlementNotFoundException;
import com.philips.onespace.exception.InvalidClientException;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.exception.LicenseConsumptionException;
import com.philips.onespace.exception.ResourceExistsException;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.exception.UndeliveredMailException;
import com.philips.onespace.util.Constants;
import com.philips.onespace.util.DateUtil;
import com.philips.onespace.util.ErrorMessages;
import com.philips.onespace.util.LocaleUtil;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private LocaleUtil localeUtil;

	@ExceptionHandler({ ResourceNotFoundException.class })
	public Object handleResourceNotFoundExceptions(ResourceNotFoundException ex, WebRequest request) {
		logData("Resource not found exception occurred : {}",
				createErrorLog(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getStackTrace()));
		String errorMessage = messageSource.getMessage(ex.getMessage(), null, localeUtil.getLanguage());
		return createExceptionResponse(ex.getMessage(), ERROR_SOURCE, CLIENT_ERROR, errorMessage,
				HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler({ BadRequestException.class })
	public Object handleDatabaseConstraintViolationException(BadRequestException ex, WebRequest request) {
		logData("Bad request exception occurred : {}",
				createErrorLog(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getStackTrace()));
		String errorMessage = messageSource.getMessage(ex.getMessage(), null, localeUtil.getLanguage());
		return createExceptionResponse(BAD_REQUEST, ERROR_SOURCE, CLIENT_ERROR, errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ DatabaseConstraintViolationException.class })
	public Object handleDatabaseConstraintViolationException(DatabaseConstraintViolationException ex,
			WebRequest request) {
		if (ex.getMessage().toLowerCase().contains("duplicate key")) {
			logData("Database constraint violation exception occurred : {}",
					createErrorLog(HttpStatus.CONFLICT, ex.getMessage(), ex.getStackTrace()));
			String errorMessage = messageSource.getMessage("duplicate_key", null, localeUtil.getLanguage());
			return createExceptionResponse(DUPLICATE_KEY, ERROR_SOURCE, CLIENT_ERROR,
					errorMessage, HttpStatus.CONFLICT);
		} else {
			logData("Database constraint violation exception occurred : {}",
					createErrorLog(HttpStatus.PAYLOAD_TOO_LARGE, ex.getMessage(), ex.getStackTrace()));
			String errorMessage = messageSource.getMessage("database_error", null, localeUtil.getLanguage());
			return createExceptionResponse(DATABASE_ERROR, ERROR_SOURCE, CLIENT_ERROR,
					errorMessage, HttpStatus.PAYLOAD_TOO_LARGE);
		}
	}

	@ExceptionHandler({ InvalidTokenException.class })
	public Object handleDatabaseConstraintViolationException(InvalidTokenException ex, WebRequest request) {
		logData("Invalid token exception occurred : {}",
				createErrorLog(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getStackTrace()));
		String errorMessage = messageSource.getMessage("invalid_token", null, localeUtil.getLanguage());
		return createExceptionResponse(INVALID_TOKEN, IAM_SOURCE, CLIENT_ERROR,
				errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ InvalidClientException.class })
	public Object handleInvalidClientException(InvalidClientException ex, WebRequest request) {
		logData("Invalid client exception : {}",
				createErrorLog(HttpStatus.FORBIDDEN, ex.getMessage(), ex.getStackTrace()));
		String errorMessage = messageSource.getMessage(ex.getMessage(), null, localeUtil.getLanguage());
		return createExceptionResponse(INVALID_CLIENT_ERR_CODE, IAM_SOURCE, CLIENT_ERROR,
				errorMessage, HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler({ UndeliveredMailException.class })
	public Object handleEmailClientException(UndeliveredMailException ex, WebRequest request) {
		logData("Exception from Email client : {}",
				createErrorLog(HttpStatus.BAD_GATEWAY, ex.getMessage(), ex.getStackTrace()));
		String errorMessage = messageSource.getMessage(ex.getMessage(), null, localeUtil.getLanguage());
		return createExceptionResponse(Constants.EMAIL_NOT_SENT_ERR_CODE, EMAIL_SOURCE,
				CLIENT_ERROR, errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ HttpClientErrorException.class })
	public Object handleDatabaseConstraintViolationException(HttpClientErrorException ex, WebRequest request) {
		ErrorLog errorLog = createErrorLog((HttpStatus) ex.getStatusCode(), ex.getMessage(), ex.getStackTrace());
		logData("Http client error exception occurred : {}", errorLog);
		String errorMessage = messageSource.getMessage("http_client_error", null, localeUtil.getLanguage());
		return createExceptionResponse(HTTP_CLIENT_ERROR, ERROR_SOURCE, CLIENT_ERROR,
				errorMessage, (HttpStatus) ex.getStatusCode());
	}

	@ExceptionHandler({ ResourceExistsException.class })
	public Object handleResourceExistsException(ResourceExistsException ex, WebRequest request) {
		logData("Resource exists exception occurred : {}",
				createErrorLog(HttpStatus.CONFLICT, ex.getMessage(), ex.getStackTrace()));
		String errorMessage = messageSource.getMessage(ex.getMessage(), null, localeUtil.getLanguage());
		return createExceptionResponse(ex.getMessage(), ERROR_SOURCE, CLIENT_ERROR, errorMessage,
				HttpStatus.CONFLICT);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Object handleValidationExceptions(MethodArgumentNotValidException ex) {
		logData("Validation exception occurred : {}",
				createErrorLog(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getStackTrace()));
		ErrorResponse errorResponse = new ErrorResponse();
        List<ErrorResponse.Issue> issueList = new ArrayList<ErrorResponse.Issue>(ex.getBindingResult().getFieldErrors().size());
		try {
			for (FieldError error : ex.getBindingResult().getFieldErrors()) {
				String code = error.getDefaultMessage();
				String errorMessage = (messageSource != null)
						? messageSource.getMessage(code, null, localeUtil.getLanguage())
						: "Argument not valid";
				ErrorResponse.Issue issue = new ErrorResponse.Issue();
				issue.setSource(ERROR_SOURCE);
				issue.setCode(code);
				issue.setMessage(errorMessage);
				issue.setCategory(CLIENT_ERROR);
				issue.setTimestamp(DateUtil.getCurrentDateUTC());
				issueList.add(issue);
			}
			errorResponse.setIssue(issueList);
		} catch (Exception expObj) {
			logData("Exception: handleValidationExceptions, Exception Details: ", expObj);
		}
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ LicenseConsumptionException.class })
	public Object handleLicenseConsumptonException(LicenseConsumptionException ex, WebRequest request) {
		logData("License consumption exception : {}",
				createErrorLog(HttpStatus.CONFLICT, ex.getMessage(), ex.getStackTrace()));
		String errorMessage = messageSource.getMessage(ex.getMessage(), null, localeUtil.getLanguage());
		return createExceptionResponse(ex.getMessage(), SENTINEL_SOURCE, CLIENT_ERROR, errorMessage,
				HttpStatus.CONFLICT);
	}

	@ExceptionHandler({ EntitlementNotFoundException.class })
	public Object handleEntitlementNotFoundException(EntitlementNotFoundException ex, WebRequest request) {
		logData("Entitlement not found exception : {}",
				createErrorLog(HttpStatus.CONFLICT, ex.getMessage(), ex.getStackTrace()));
		String errorMessage = messageSource.getMessage(ex.getMessage(), null, localeUtil.getLanguage());
		return createExceptionResponse(ex.getMessage(), SENTINEL_SOURCE, CLIENT_ERROR, errorMessage,
				HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler({ MethodArgumentTypeMismatchException.class })
	public Object handleDatabaseMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
		logData("MethodArgumentTypeMismatch exception occurred : {}",
				createErrorLog(HttpStatus.BAD_REQUEST, ex.getMessage(), ex.getStackTrace()));
		String errorMessage = messageSource.getMessage(ErrorMessages.INVALID_ID, null, localeUtil.getLanguage());
		return createExceptionResponse(BAD_REQUEST,ERROR_SOURCE,CLIENT_ERROR,
				errorMessage, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
    public Object handleValidationExceptions(ConstraintViolationException  ex) throws ParseException {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        ErrorResponse errorResponse = new ErrorResponse();
        List<ErrorResponse.Issue> issueList = new ArrayList<ErrorResponse.Issue>(errors.size());
        for (String code : errors) {
            String errorMessage = messageSource.getMessage(code, null,
                    localeUtil.getLanguage());
            ErrorResponse.Issue issue = new ErrorResponse.Issue();
            issue.setSource(ERROR_SOURCE);
            issue.setCode(code);
            issue.setMessage(errorMessage);
            issue.setCategory(CLIENT_ERROR);
            issue.setTimestamp(DateUtil.getCurrentDateUTC());
            issueList.add(issue);
        }
        errorResponse.setIssue(issueList);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

	@ExceptionHandler({ AccessDeniedException.class })
	public Object handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
		logData("AccessDenied exception occurred : {}",
				createErrorLog(HttpStatus.NOT_FOUND, ex.getMessage(), ex.getStackTrace()));
		String errorMessage = messageSource.getMessage(ErrorMessages.ERR_MESSGE_ACCESSDENIED, null, localeUtil.getLanguage());
		return createExceptionResponse(ex.getMessage(), ERROR_SOURCE, CLIENT_ERROR, errorMessage,
				HttpStatus.FORBIDDEN);
	}

	private ResponseEntity<ErrorResponse> createExceptionResponse(String code, String source, String category,
			String message, HttpStatus statusCode) {
		ErrorResponse errorResponse = new ErrorResponse();
		try {
			ErrorResponse.Issue issue = new ErrorResponse.Issue();
			issue.setSource(source);
			issue.setCode(code);
			issue.setMessage(message);
			issue.setCategory(category);
			issue.setCategory(category);
			issue.setTimestamp(DateUtil.getCurrentDateUTC());
			errorResponse.setIssue(Collections.singletonList(issue));
		} catch (Exception expObj) {
			logData("Exception: createExceptionResponse, Exception Details: ", expObj);
		}
		return new ResponseEntity<>(errorResponse, statusCode);
	}

	private ErrorLog createErrorLog(HttpStatus statusCode, String message, StackTraceElement[] stackTrace) {
		return ErrorLog.builder().message(message).stackTrace(stackTrace).statusCode(statusCode.value()).build();
	}

}

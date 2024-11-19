/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ErrorHandlingAspect.java
 */

package com.philips.onespace.appdiscoveryframework.util;

import java.util.Collections;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.philips.onespace.dto.ErrorResponse;
import com.philips.onespace.util.LocaleUtil;

import jakarta.validation.ConstraintViolationException;

public class ErrorHandlingAspect {
    
	@Autowired
    private MessageSource messageSource;
    
	@Autowired
    private LocaleUtil localeUtil;
    
	@Around("@annotation(ValidInput)")
    public Object handleInvalidRequestError(ProceedingJoinPoint joinPoint,
                                            ConstraintViolationException constraintViolationException) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (ConstraintViolationException ex) {
            String errorMessage = messageSource.
                    getMessage(constraintViolationException.getMessage(),
                            null, localeUtil.getLanguage());
            ErrorResponse errorResponse = new ErrorResponse();
            ErrorResponse.Issue issue = new ErrorResponse.Issue();
            issue.setSource("application_discovery_service");
            issue.setCode("invalid-parameter");
            issue.setMessage(errorMessage);
            issue.setCategory("Validation Error");
            errorResponse.setIssue(Collections.singletonList(issue));
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }
}

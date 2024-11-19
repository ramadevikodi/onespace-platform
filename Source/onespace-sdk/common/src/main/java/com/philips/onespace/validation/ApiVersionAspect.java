/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: ApiVersionAspect.java
 */

package com.philips.onespace.validation;

import java.util.Objects;

import org.apache.coyote.BadRequestException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.philips.onespace.util.CommonUtil;
import com.philips.onespace.validator.ApiVersion;

import jakarta.servlet.http.HttpServletRequest;

@Component
@Aspect
public class ApiVersionAspect {
	@Before("@annotation(apiVersion)")
	public void validateApiVersion(JoinPoint joinPoint, ApiVersion apiVersion) throws BadRequestException {
		ServletRequestAttributes servletRequestAttributes = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes());
		if(Objects.nonNull(servletRequestAttributes)) {
			HttpServletRequest httpServletRequest  = servletRequestAttributes.getRequest();
			String versionHeader = httpServletRequest.getHeader("api-version");
			CommonUtil.validateVersion(apiVersion.value(), versionHeader);
		}
	}
}

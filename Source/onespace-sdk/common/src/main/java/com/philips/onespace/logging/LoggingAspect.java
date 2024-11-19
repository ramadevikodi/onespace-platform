package com.philips.onespace.logging;


import static com.philips.onespace.util.Constants.HEADER_KEY_CF_ID;
import static com.philips.onespace.util.Constants.HEADER_KEY_COOKIE;
import static com.philips.onespace.util.Constants.HEADER_KEY_ONESPACE_COOKIE;
import static com.philips.onespace.util.Constants.HEADER_KEY_SET_COOKIE;
import static com.philips.onespace.util.IamConstants.HSP_IAM_ASSERTION;
import static com.philips.onespace.util.IamConstants.HSP_IAM_CODE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.google.common.base.Joiner;
import com.philips.onespace.model.OneSpaceLogger;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {

    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();
    // ThreadLocal to store method call details per request
    private static final ThreadLocal<List<String>> methodCallStack = ThreadLocal.withInitial(ArrayList::new);

    @Autowired
    private LogEmittedDataService logEmittedDataService;

    @Value("${capturePerformanceMetrics}")
    private boolean performanceMetrics;

    public static void logData(final Object... messages) {
        methodCallStack.get().add(Joiner.on(",").useForNull("null").join(messages));
    }

    public static String maskData(String message) {
        return message.replaceAll("(?<=.{4}).", "*");
    }

    @Before("execution(* com.philips.onespace.appdiscoveryframework.controller..*(..))")
    public void logRequest(JoinPoint joinPoint) {

        if (performanceMetrics) {
            startTime.set(System.currentTimeMillis());
        }

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            OneSpaceLogger oneSpaceLogger = new OneSpaceLogger();
            oneSpaceLogger.setOperation(joinPoint.getSignature().getName());
            oneSpaceLogger.setStatus(OneSpaceLogger.StatusLogEnum.INITIATED.toString());
            oneSpaceLogger.setMethod(request.getMethod());
            oneSpaceLogger.setUrl(request.getRequestURI());

            Object[] args = joinPoint.getArgs();
            if (args.length > 0) {
                oneSpaceLogger.setBody(args[0]);
            }
            Map<String, String> requestHeaders = getRequestHeaders(request);
            if (!requestHeaders.isEmpty()) {
                oneSpaceLogger.setHeaders(requestHeaders);
            }
            Map<String, String> parameters = getParameters(request);
            if (!parameters.isEmpty()) {
                oneSpaceLogger.setRequestParameters(parameters);
            }
            String requestLog = String.valueOf(oneSpaceLogger);
            log.info(requestLog);
        }
    }

    @AfterReturning(pointcut = "execution(* com.philips.onespace.appdiscoveryframework.controller..*(..))", returning = "result")
    public void logResponse(JoinPoint joinPoint, Object result) {

        logBusinessLogics();

        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (requestAttributes != null) {
            HttpServletRequest request = requestAttributes.getRequest();
            OneSpaceLogger oneSpaceLogger = new OneSpaceLogger();
            oneSpaceLogger.setOperation(joinPoint.getSignature().getName());
            oneSpaceLogger.setMethod(request.getMethod());
            oneSpaceLogger.setUrl(request.getRequestURI());

            if(result instanceof ResponseEntity){
                int statusCode = ((ResponseEntity<?>) result).getStatusCodeValue();
                oneSpaceLogger.setStatusCode(statusCode);
                if (statusCode == 200 || statusCode == 201) {
                    oneSpaceLogger.setStatus(OneSpaceLogger.StatusLogEnum.SUCCEEDED.toString());
                } else {
                    oneSpaceLogger.setStatus(OneSpaceLogger.StatusLogEnum.ERROR.toString());
                }
                oneSpaceLogger.setHeaders(maskHeader(((ResponseEntity<?>) result).getHeaders().toSingleValueMap()));
                oneSpaceLogger.setBody(((ResponseEntity<?>) result).getBody());
            }
            else if (result instanceof SseEmitter){
                oneSpaceLogger.setStatusCode(200);
                oneSpaceLogger.setStatus(OneSpaceLogger.StatusLogEnum.SUCCEEDED.toString());
                oneSpaceLogger.setBody(logEmittedDataService.getData());
            }

            if (performanceMetrics) {
                long executionTime = System.currentTimeMillis() - startTime.get();
                startTime.remove();
                oneSpaceLogger.setTotalTimeTaken(executionTime + " " + TimeUnit.MILLISECONDS);
            }
            String responseLog = String.valueOf(oneSpaceLogger);
            log.info(responseLog);
        }
    }

    private Map<String, String> maskHeader(Map<String, String> headers) {
        Map<String, String> modifiableHeaders = new HashMap<>(headers);
        modifiableHeaders.computeIfPresent(HEADER_KEY_SET_COOKIE, (key, value) -> maskData(value));
        modifiableHeaders.computeIfPresent(HEADER_KEY_COOKIE, (key, value) -> maskData(value));
        modifiableHeaders.computeIfPresent(HEADER_KEY_ONESPACE_COOKIE, (key, value) -> maskData(value));
        modifiableHeaders.computeIfPresent(HEADER_KEY_CF_ID, (key, value) -> maskData(value));
        return modifiableHeaders;
    }

    @Around("@annotation(com.philips.onespace.logging.LogExecutionTime)")
    public Object logExecutionTimeForThirdPartyAPI(ProceedingJoinPoint joinPoint) throws Throwable {

        OneSpaceLogger oneSpaceLogger = new OneSpaceLogger();
        oneSpaceLogger.setOperation(joinPoint.getSignature().getName());
        long start = System.currentTimeMillis();

        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        oneSpaceLogger.setStatus(OneSpaceLogger.StatusLogEnum.SUCCEEDED.toString());
        oneSpaceLogger.setBody(result);
        oneSpaceLogger.setTotalTimeTaken(executionTime + " " + TimeUnit.MILLISECONDS);

        String logExecutionTime = String.valueOf(oneSpaceLogger);
        log.info(logExecutionTime);

        return result;
    }

    private void logBusinessLogics() {
        List<String> stackedMethods = methodCallStack.get();
        if (!stackedMethods.isEmpty()) {
            for (String method : stackedMethods) {
                log.debug("OneSpaceLogger : {}", method);
            }
        }
        methodCallStack.remove();
    }

    @Pointcut("execution(* com.philips.onespace..*(..)) && !within(com.philips.onespace.appdiscoveryframework.config..*)")
    public void allPackagesExceptConfig() {
        /* Pointcut for all packages except config */
    }

    @Around("allPackagesExceptConfig()")
    public Object logExceptions(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            return joinPoint.proceed();
        } catch (Throwable ex) {
            log.error("OneSpaceLogger : Exception in method {}", joinPoint.getSignature().toShortString(), ex);
            throw ex;
        }
    }

    private Map<String, String> getRequestHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();

        for (String key : Collections.list(headerNames)) {
            headers.put(key, request.getHeader(key));
        }

        return maskHeader(headers);
    }

    private Map<String, String> getParameters(HttpServletRequest request) {
        Map<String, String> parameters = new HashMap<>();
        Enumeration<String> params = request.getParameterNames();
        while (params.hasMoreElements()) {
            String paramName = params.nextElement();
            String paramValue = request.getParameter(paramName);
            if (null != StringUtils.substringBetween(request.getRequestURI(), "/api/", "/") && (StringUtils.substringBetween(request.getRequestURI(), "/api/", "/").equals("Session")) &&
                (paramName.equals(HSP_IAM_CODE) || paramName.equals(HSP_IAM_ASSERTION))) {
                parameters.put(paramName, maskData(paramValue));
            } else {
                parameters.put(paramName, paramValue);
            }
        }
        return parameters;
    }

    @Around("@annotation(scheduled)")
    public Object logScheduledTasks(ProceedingJoinPoint joinPoint, Scheduled scheduled) throws Throwable {
        Object result = null;
        String methodName = joinPoint.getSignature().getName();
        long startTimeForScheduledTask = System.currentTimeMillis();

        OneSpaceLogger oneSpaceLoggerInitiated = new OneSpaceLogger();
        oneSpaceLoggerInitiated.setOperation(methodName);
        oneSpaceLoggerInitiated.setStatus(OneSpaceLogger.StatusLogEnum.INITIATED.toString());
        oneSpaceLoggerInitiated.setMethod("scheduled-task");
        String logScheduledTaskInitiated = String.valueOf(oneSpaceLoggerInitiated);
        log.info(logScheduledTaskInitiated);

        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            long totalTime = System.currentTimeMillis() - startTimeForScheduledTask;
            OneSpaceLogger oneSpaceLoggerFinished = new OneSpaceLogger();
            oneSpaceLoggerFinished.setOperation(methodName);
            oneSpaceLoggerFinished.setStatus(OneSpaceLogger.StatusLogEnum.SUCCEEDED.toString());
            oneSpaceLoggerFinished.setMethod("scheduled-task");
            oneSpaceLoggerFinished.setBody(result);
            oneSpaceLoggerFinished.setTotalTimeTaken(totalTime + " " + TimeUnit.MILLISECONDS);
            String logScheduledTaskFinished = String.valueOf(oneSpaceLoggerFinished);
            log.info(logScheduledTaskFinished);
        }
    }
}
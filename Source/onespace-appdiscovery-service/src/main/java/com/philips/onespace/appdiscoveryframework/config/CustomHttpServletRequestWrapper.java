/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: CustomHttpServletRequestWrapper.java
 */

package com.philips.onespace.appdiscoveryframework.config;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final Map<String, String> customHeaders;

    public CustomHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        this.customHeaders = new HashMap<>();
    }

    public void addHeader(String name, String value) {
        this.customHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        // Check if the custom header exists
        String headerValue = customHeaders.get(name);
        
        // If not, retrieve from the original request
        return headerValue != null ? headerValue : super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        // Return the custom header value, or fall back to the original headers
        if (customHeaders.containsKey(name)) {
            return Collections.enumeration(Collections.singletonList(customHeaders.get(name)));
        }
        return super.getHeaders(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        // Merge the custom headers with the original headers
        Set<String> names = new HashSet<>(customHeaders.keySet());
        names.addAll(Collections.list(super.getHeaderNames()));
        return Collections.enumeration(names);
    }
}
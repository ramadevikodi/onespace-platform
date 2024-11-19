/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: FilterCriteriaServiceTest.java
 */
package com.philips.onespace.appdiscoveryframework.service;

import com.philips.onespace.appdiscoveryframework.factory.FilterCriteriaFactory;
import com.philips.onespace.appdiscoveryframework.util.IAMUtil;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.philips.onespace.dto.ApplicationFilter;
import com.philips.onespace.dto.RoleEnum;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.model.IntrospectionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

class FilterCriteriaServiceTest {

    @Mock
    private IAMUtil iamUtil;

    @Mock
    private FilterCriteriaFactory filterCriteriaFactory;

    @InjectMocks
    private FilterCriteriaService filterCriteriaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetCriteriaDataForCurrentUser_hasRole() throws InvalidTokenException {
        String token = "validToken";
        IntrospectionResponse introspectionResponse = mock(IntrospectionResponse.class);
        when(iamUtil.introspectToken(token)).thenReturn(introspectionResponse);
        when(iamUtil.checkIfTokenContainsRole(introspectionResponse, "APPLICATIONINTEGRATORROLE")).thenReturn(Boolean.TRUE);
        RoleEnum role = RoleEnum.APPLICATIONINTEGRATORROLE;
        List<ApplicationFilter> expectedFilters = List.of(mock(ApplicationFilter.class));
        when(filterCriteriaFactory.getRoleBasedFilterCriteria(role)).thenReturn(expectedFilters);
        List<ApplicationFilter> actualFilters = filterCriteriaService.getCriteriaDataForCurrentUser(token);
        assertEquals(expectedFilters, actualFilters);
        verify(iamUtil, times(1)).introspectToken(token);
        verify(iamUtil, times(1)).checkIfTokenContainsRole(introspectionResponse, "APPLICATIONINTEGRATORROLE");
        verify(filterCriteriaFactory, times(1)).getRoleBasedFilterCriteria(role);
    }

    @Test
    void testGetCriteriaDataForCurrentUser_noRole() throws InvalidTokenException {
        String token = "validToken";
        IntrospectionResponse introspectionResponse = mock(IntrospectionResponse.class);
        when(iamUtil.introspectToken(token)).thenReturn(introspectionResponse);
        for (RoleEnum role : RoleEnum.values()) {
            when(iamUtil.checkIfTokenContainsRole(introspectionResponse, role.name())).thenReturn(Boolean.FALSE);
        }
        List<ApplicationFilter> actualFilters = filterCriteriaService.getCriteriaDataForCurrentUser(token);
        assertNull(actualFilters);
        verify(iamUtil, times(1)).introspectToken(token);

    }

    @Test
    void testGetCriteriaDataForCurrentUser_invalidToken() throws InvalidTokenException {
        String token = "invalidToken";
        when(iamUtil.introspectToken(token)).thenThrow(new InvalidTokenException("Invalid token"));
        assertThrows(InvalidTokenException.class, () -> {
            filterCriteriaService.getCriteriaDataForCurrentUser(token);
        });

        verify(iamUtil, times(1)).introspectToken(token);
    }
}

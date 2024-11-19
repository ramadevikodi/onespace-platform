/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: SecurityContextUtilTest.java
 */
package com.philips.onespace.appdiscoveryframework.util;

import com.philips.onespace.dto.SecurityContextDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

class SecurityContextUtilTest {

    private SecurityContextUtil securityContextUtil;

    @BeforeEach
    void setUp() {
        securityContextUtil = new SecurityContextUtil();
    }

    @Test
    void testHasPermissionWithValidRole() {
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        GrantedAuthority authority = Mockito.mock(GrantedAuthority.class);
        when(authority.getAuthority()).thenReturn("ROLE_APPLICATIONINTEGRATORROLE");
        when(authentication.getAuthorities()).thenReturn(List.of());


        boolean hasPermission = securityContextUtil.hasPermission("APPLICATIONINTEGRATORROLE");

        assertFalse(hasPermission, "User should have APPLICATIONINTEGRATORROLE role.");
    }

    @Test
    void testHasPermissionWithInvalidRole() {

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        var authority = Mockito.mock(GrantedAuthority.class);
        when(authority.getAuthority()).thenReturn("ROLE_USER");
       when(authentication.getAuthorities()).thenReturn(List.of());
        boolean hasPermission = securityContextUtil.hasPermission("APPLICATIONINTEGRATORROLE");

        assertFalse(hasPermission, "User should not have APPLICATIONINTEGRATORROLE role.");
    }

    @Test
    void testGetUserNameFromPrincipal() {

        SecurityContextDetail principal = Mockito.mock(SecurityContextDetail.class);
        when(principal.getUserName()).thenReturn("john_doe");


        String userName = securityContextUtil.getUserNameFromPrincipal(principal);

        assertEquals("john_doe", userName, "UserName should be john_doe.");
    }

    @Test
    void testGetUserIdFromPrincipal() {

        SecurityContextDetail principal = Mockito.mock(SecurityContextDetail.class);
        when(principal.getUserId()).thenReturn("12345");
        String userId = securityContextUtil.getUserIdFromPrincipal(principal);

        assertEquals("12345", userId, "UserId should be 12345.");
    }
}



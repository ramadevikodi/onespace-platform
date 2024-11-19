/*
 * (C) Koninklijke Philips Electronics N.V. 2024
 *
 * All rights are reserved. Reproduction or transmission in whole or in part, in any form or by any
 * means, electronic, mechanical or otherwise, is prohibited without the prior written consent of
 * the copyright owner.
 *
 * File name: SecurityContextUtil.java
 */

package com.philips.onespace.appdiscoveryframework.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.philips.onespace.dto.SecurityContextDetail;

@Component
public class SecurityContextUtil {

    public boolean hasPermission(String permission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        List<String> roles = authentication.getAuthorities().stream()
                .map(r -> r.getAuthority()).collect(Collectors.toList());
        return roles.contains("ROLE_" + permission);
    }

    /**
	 * Get userName from securitycontext principal.
	 *
	 * @param principal
	 * @return the username
	 */
	public String getUserNameFromPrincipal(Object principal) {
		SecurityContextDetail principalObject = (SecurityContextDetail)principal;
		return principalObject.getUserName();
	}
	
	/**
	 * Get userId from securitycontext principal.
	 *
	 * @param principal
	 * @return the userId
	 */
	public String getUserIdFromPrincipal(Object principal) {
		SecurityContextDetail principalObject = (SecurityContextDetail)principal;
		return principalObject.getUserId();
	}
    
}

package com.philips.onespace.appdiscoveryframework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.philips.onespace.appdiscoveryframework.factory.FilterCriteriaFactory;
import com.philips.onespace.appdiscoveryframework.util.IAMUtil;
import com.philips.onespace.dto.ApplicationFilter;
import com.philips.onespace.dto.RoleEnum;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.model.IntrospectionResponse;

@Service
public class FilterCriteriaService {
    @Autowired
    private IAMUtil iamUtil;

    @Autowired
    private FilterCriteriaFactory filterCriteriaFactory;

    public List<ApplicationFilter> getCriteriaDataForCurrentUser(String token) throws InvalidTokenException {
        IntrospectionResponse introspectionResponse = iamUtil
                .introspectToken(token);
        for (RoleEnum role : RoleEnum.values()) {
            String roleName = role.name();
            Boolean hasRole = iamUtil
                    .checkIfTokenContainsRole(introspectionResponse, roleName);
            if (hasRole) {
                return filterCriteriaFactory
                        .getRoleBasedFilterCriteria(role);
            }
        }
        return null;
    }
}

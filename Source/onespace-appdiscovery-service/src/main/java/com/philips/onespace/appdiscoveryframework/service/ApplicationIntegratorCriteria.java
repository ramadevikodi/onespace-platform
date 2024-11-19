package com.philips.onespace.appdiscoveryframework.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.philips.onespace.appdiscoveryframework.mapper.ApplicationMapper;
import com.philips.onespace.appdiscoveryframework.service.interfaces.RoleBasedFilterCriteria;
import com.philips.onespace.dto.ApplicationFilter;
import com.philips.onespace.jpa.repository.ApplicationRepository;

@Component
public class ApplicationIntegratorCriteria implements RoleBasedFilterCriteria {

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Override
    public List<ApplicationFilter> getCriteria() {
        return applicationMapper
                .convertToApplicationFilter(applicationRepository
                        .getFilterCriteriaForAppIntegrator());
    }
}

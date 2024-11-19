package com.philips.onespace.appdiscoveryframework.service.applisting;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.philips.onespace.appdiscoveryframework.service.interfaces.AppRegistrationService;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.specification.ApplicationSpecifications;
import com.philips.onespace.util.Constants;


class PlatformAdminAppListingTest {


    @InjectMocks
    private PlatformAdminAppListing platformAdminAppListing;
    @Mock
    private AppRegistrationService appRegistrationService;

    @Mock
    private ApplicationSpecifications applicationSpecifications;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListApplications() {
        UUID statusId = UUID.randomUUID();
        Map<String, String> criteria = new HashMap<>();
        Pageable pageable = Pageable.unpaged();
        Specification<ApplicationEntity> specification = mock(Specification.class);

        List<ApplicationEntity> applicationList = new ArrayList<>();
        ApplicationEntity app1 = new ApplicationEntity();
        applicationList.add(app1);

        Page<ApplicationEntity> expectedPage = new PageImpl<>(applicationList, pageable, 1);
        ApplicationEntity userManagementApp = new ApplicationEntity();

        when(applicationSpecifications.buildSpecification(criteria)).thenReturn(specification);
        when(appRegistrationService.getAllApplications(statusId, specification, pageable)).thenReturn(expectedPage);
        when(appRegistrationService.getApplication(Constants.USER_MANAGEMENT_APP_NAME)).thenReturn(userManagementApp);

        Page<ApplicationEntity> result = platformAdminAppListing.listApplications(null, statusId, criteria, pageable);

        List<ApplicationEntity> expectedContent = new ArrayList<>(applicationList);
        expectedContent.add(userManagementApp);

        assertEquals(expectedContent, result.getContent());
        assertEquals(2, result.getTotalElements());
        verify(applicationSpecifications).buildSpecification(criteria);
        verify(appRegistrationService).getAllApplications(statusId, specification, pageable);
        verify(appRegistrationService).getApplication(Constants.USER_MANAGEMENT_APP_NAME);
    }
}
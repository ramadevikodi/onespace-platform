package com.philips.onespace.appdiscoveryframework.service.applisting;

import com.philips.onespace.appdiscoveryframework.service.interfaces.AppRegistrationService;
import com.philips.onespace.appdiscoveryframework.service.interfaces.UserMarketMappingService;
import com.philips.onespace.dto.UserMarketMap;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.repository.ApplicationRepository;
import com.philips.onespace.jpa.specification.ApplicationSpecifications;
import com.philips.onespace.model.IntrospectionResponse;
import com.philips.onespace.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static com.philips.onespace.util.IamConstants.HSP_IAM_SELF_SERVICE_APP_NAME;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KamAppListingTest {
    @Mock
    private ApplicationSpecifications applicationSpecifications;
    @Mock
    private UserMarketMappingService userMarketMappingService;
    @Mock
    private AppRegistrationService appRegistrationService;
    @Mock
    private ApplicationRepository applicationRepository;
    @InjectMocks
    private KamAppListing kamAppListing;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listApplications() throws InvalidTokenException {
        Map<String, String> criteria = new HashMap<>();
        UUID statusId = UUID.fromString("140ddf77-cf25-4a8e-8572-10de2efb82c0");
        IntrospectionResponse introspectionResponse = new IntrospectionResponse();
        introspectionResponse.setSub("140ddf77-cf25-4a8e-8572-10de2efb82c1");
        Specification<ApplicationEntity> specification = mock(Specification.class);
        List<ApplicationEntity> applicationEntities = new ArrayList<>();
        List<UserMarketMap> userMarketMapEntityList = new ArrayList<>();
        UserMarketMap userMarketMap = new UserMarketMap();
        userMarketMapEntityList.add(userMarketMap);
        when(applicationSpecifications.buildSpecification(any(Map.class))).thenReturn(specification);
        when(appRegistrationService.getAllApplicationsByName(List.of(HSP_IAM_SELF_SERVICE_APP_NAME, Constants.SENTINEL_APP_NAME, Constants.USER_MANAGEMENT_APP_NAME))).thenReturn(applicationEntities);
        when(userMarketMappingService.getMarketsAssociatedToUser(UUID.fromString(introspectionResponse.getSub()))).thenReturn(userMarketMapEntityList);

        Page<ApplicationEntity> result = kamAppListing.listApplications(introspectionResponse, statusId, criteria, Pageable.unpaged());

        assertNotNull(result);
    }

    @Test
    void listApplications_positive() throws InvalidTokenException {
        Map<String, String> criteria = new HashMap<>();
        UUID statusId = UUID.fromString("140ddf77-cf25-4a8e-8572-10de2efb82c0");
        IntrospectionResponse introspectionResponse = new IntrospectionResponse();
        introspectionResponse.setSub("140ddf77-cf25-4a8e-8572-10de2efb82c0");
        Specification<ApplicationEntity> specification = mock(Specification.class);
        List<ApplicationEntity> applicationEntities = new ArrayList<>();
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntities.add(applicationEntity);
        List<UserMarketMap> userMarketMapEntityList = new ArrayList<>();
        UserMarketMap userMarketMap = new UserMarketMap();
        userMarketMap.setMarketId(statusId);
        userMarketMapEntityList.add(userMarketMap);
        when(applicationSpecifications.buildSpecification(any(Map.class))).thenReturn(specification);
        when(appRegistrationService.getAllApplicationsByName(List.of(HSP_IAM_SELF_SERVICE_APP_NAME, Constants.SENTINEL_APP_NAME, Constants.USER_MANAGEMENT_APP_NAME))).thenReturn(applicationEntities);
        when(userMarketMappingService.getMarketsAssociatedToUser(UUID.fromString(introspectionResponse.getSub()))).thenReturn(userMarketMapEntityList);
        when(appRegistrationService.getAllApplicationsPublishedToMarket(userMarketMap.getMarketId())).thenReturn(applicationEntities);
        when(applicationSpecifications.getApplicationEntitySpecification(specification, applicationEntities)).thenReturn(specification);
        when(applicationRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(mock(Page.class));

        Page<ApplicationEntity> result = kamAppListing.listApplications(introspectionResponse, statusId, criteria, Pageable.unpaged());

        assertNotNull(result);
    }

}
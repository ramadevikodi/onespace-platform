package com.philips.onespace.appdiscoveryframework.service.applisting;


import com.philips.onespace.appdiscoveryframework.service.interfaces.AppRegistrationService;
import com.philips.onespace.appdiscoveryframework.service.interfaces.BusinessUnitService;
import com.philips.onespace.appdiscoveryframework.util.IAMUtil;
import com.philips.onespace.dto.BusinessUnit;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.entity.ApplicationStatusEntity;
import com.philips.onespace.jpa.repository.ApplicationRepository;
import com.philips.onespace.jpa.specification.ApplicationSpecifications;
import com.philips.onespace.model.IntrospectionResponse;
import com.philips.onespace.model.Organization;
import com.philips.onespace.model.Organizations;
import com.philips.onespace.util.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;


import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;


class ApplicationOwnerAppListingTest {

    @Mock
    private IAMUtil iamUtil;

    @Mock
    private ApplicationSpecifications applicationSpecifications;

    @Mock
    private AppRegistrationService appRegistrationService;

    @Mock
    private BusinessUnitService businessUnitService;

    @Mock
    private ApplicationRepository applicationRepository;

    @InjectMocks
    private ApplicationOwnerAppListing applicationOwnerAppListing;
    private IntrospectionResponse introspectionResponse;
    private UUID statusId;

    private Pageable pageable;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        introspectionResponse = new IntrospectionResponse();
        introspectionResponse.setUsername("testUser");
        statusId = UUID.randomUUID();
        pageable = PageRequest.of(0, 10);


    }

    @Test
    void testListApplicationsWithDraftStatus() throws Exception {

        UUID hspIamBuOrgID = UUID.fromString("140ddf77-cf25-4a8e-8572-10de2efb82c1");
        UUID id = UUID.fromString("140ddf77-cf25-4a8e-8572-10de2efb82c3");

        ApplicationStatusEntity statusEntity = new ApplicationStatusEntity();
        statusEntity.setName(Constants.DRAFT);

        ApplicationEntity draftApplication = new ApplicationEntity();
        draftApplication.setId(id);
        draftApplication.setName("sgdf");
        draftApplication.setStatus(statusEntity);

        List<ApplicationEntity> applicationList = Collections.singletonList(draftApplication);
        Page<ApplicationEntity> applicationEntityPage = new PageImpl<>(applicationList);

        BusinessUnit businessUnit = new BusinessUnit();
        businessUnit.setId(hspIamBuOrgID);

        when(iamUtil.findUserOrg(any(IntrospectionResponse.class), eq("APPLICATIONOWNERROLE"))).thenReturn(hspIamBuOrgID.toString());
        when(businessUnitService.getBusinessUnitByHSPIamOrgID(hspIamBuOrgID)).thenReturn(businessUnit);
        when(appRegistrationService.getAllApplicationsOfOwnerOrg((hspIamBuOrgID), (statusId))).thenReturn(applicationList);

        Specification<ApplicationEntity> specification = mock(Specification.class);
        when(applicationSpecifications.buildSpecification(anyMap())).thenReturn(specification);
        when(applicationSpecifications.getApplicationEntitySpecification(eq(specification), anyList())).thenReturn(specification);

        when(applicationRepository.findAll(eq(specification), any(Pageable.class))).thenReturn(applicationEntityPage);

        Organizations organizations = new Organizations();
        List<Organization> organizationList = new ArrayList<>();
        Organization organization = new Organization();
        organization.setOrganizationId(hspIamBuOrgID.toString());
        organization.setRoles(Collections.singletonList("APPLICATIONOWNERROLE"));
        organizationList.add(organization);
        organizations.setOrganizationList(organizationList);
        introspectionResponse.setOrganizations(organizations);

        Page<ApplicationEntity> result = applicationOwnerAppListing.listApplications(introspectionResponse, statusId, new HashMap<>(), Pageable.unpaged());

        verify(iamUtil).findUserOrg(introspectionResponse, "APPLICATIONOWNERROLE");
        verify(businessUnitService).getBusinessUnitByHSPIamOrgID(hspIamBuOrgID);
        verify(appRegistrationService).getAllApplicationsOfOwnerOrg((hspIamBuOrgID), (statusId));
        verify(applicationSpecifications).buildSpecification(anyMap());
        verify(applicationSpecifications).getApplicationEntitySpecification(eq(specification), anyList());

        assertNotNull(result);
    }

    @Test
    void testListApplicationsOfBU_NoBusinessUnit() {
        // Mocking
        when(iamUtil.findUserOrg((IntrospectionResponse) any(), anyString())).thenReturn(UUID.randomUUID().toString());
        when(businessUnitService.getBusinessUnitByHSPIamOrgID(any())).thenReturn(null);

        Page<ApplicationEntity> result = applicationOwnerAppListing.listApplicationsOfBU(introspectionResponse, "roleName", mock(Specification.class), statusId, pageable);

        assertTrue(result.isEmpty());
        verify(businessUnitService).getBusinessUnitByHSPIamOrgID(any());
    }
}

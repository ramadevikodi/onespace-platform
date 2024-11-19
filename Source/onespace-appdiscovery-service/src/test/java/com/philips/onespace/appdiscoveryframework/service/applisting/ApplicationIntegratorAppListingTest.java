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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.*;

class ApplicationIntegratorAppListingTest {

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
    private ApplicationIntegratorAppListing applicationIntegratorAppListing;

    private IntrospectionResponse introspectionResponse;
    private UUID statusId;

    private Pageable pageable;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        introspectionResponse = new IntrospectionResponse();
        introspectionResponse.setUsername("testUser");
        statusId = UUID.randomUUID();
        pageable = PageRequest.of(0, 10);

    }

    @Test
    void testListApplicationsWithDraftStatus() throws Exception {

        String userName = "testUser";
        UUID hspIamBuOrgID = UUID.fromString("f9cfcff2-f77f-4e4a-bb2f-8ee157845054");

        UUID id = UUID.fromString("f4cfcff2-f77f-4e5a-bb2f-8ee157845054");


        ApplicationStatusEntity statusEntity = new ApplicationStatusEntity();

        statusEntity.setName(Constants.DRAFT);


        ApplicationEntity draftApplication = new ApplicationEntity();
        draftApplication.setId(id);
        draftApplication.setName(Constants.DRAFT);
        draftApplication.setStatus(statusEntity);
        draftApplication.setRegisteredBy(userName);


        List<ApplicationEntity> applicationList = Collections.singletonList(draftApplication);


        Page<ApplicationEntity> applicationEntityPage = new PageImpl<>(applicationList);

        Specification<ApplicationEntity> specification = mock(Specification.class);

        BusinessUnit businessUnit = new BusinessUnit();
        businessUnit.setId(hspIamBuOrgID);


        when(iamUtil.findUserOrg(any(IntrospectionResponse.class), eq("APPLICATIONINTEGRATORROLE"))).thenReturn(hspIamBuOrgID.toString());
        when(businessUnitService.getBusinessUnitByHSPIamOrgID(hspIamBuOrgID)).thenReturn(businessUnit);
        when(appRegistrationService.getAllApplicationsOfOwnerOrg((hspIamBuOrgID), (statusId))).thenReturn(applicationList);
        when(applicationSpecifications.buildSpecification(anyMap())).thenReturn(specification);
        when(applicationSpecifications.getApplicationEntitySpecification(eq(specification), anyList())).thenReturn(specification);
        when(applicationRepository.findAll(eq(specification), any(Pageable.class))).thenReturn(applicationEntityPage);

        Organizations organizations = new Organizations();
        List<Organization> organizationList = new ArrayList<>();
        Organization organization = new Organization();
        organization.setOrganizationId(hspIamBuOrgID.toString());
        organization.setRoles(Collections.singletonList("APPLICATIONINTEGRATORROLE"));
        organizationList.add(organization);
        organizations.setOrganizationList(organizationList);
        introspectionResponse.setOrganizations(organizations);
        introspectionResponse.setUsername(userName);


        Page<ApplicationEntity> result = applicationIntegratorAppListing.listApplications(introspectionResponse, statusId, new HashMap<>(), Pageable.unpaged());


        verify(iamUtil).findUserOrg(introspectionResponse, "APPLICATIONINTEGRATORROLE");
        verify(businessUnitService).getBusinessUnitByHSPIamOrgID(hspIamBuOrgID);
        verify(appRegistrationService).getAllApplicationsOfOwnerOrg((hspIamBuOrgID), (statusId));
        verify(applicationSpecifications).buildSpecification(anyMap());
        verify(applicationSpecifications).getApplicationEntitySpecification(eq(specification), anyList());
        verify(applicationRepository).findAll(eq(specification), any(Pageable.class));


        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(draftApplication, result.getContent().get(0));
    }

    @Test
    void testListApplicationsOfBU_NoBusinessUnit() {
        // Mocking
        when(iamUtil.findUserOrg((IntrospectionResponse) any(), anyString())).thenReturn(UUID.randomUUID().toString());
        when(businessUnitService.getBusinessUnitByHSPIamOrgID(any())).thenReturn(null);

        // Execute
        Page<ApplicationEntity> result = applicationIntegratorAppListing.listApplicationsOfBU(introspectionResponse, "roleName", mock(Specification.class), statusId, pageable);

        // Verify
        assertTrue(result.isEmpty());
        verify(businessUnitService).getBusinessUnitByHSPIamOrgID(any());
    }

}

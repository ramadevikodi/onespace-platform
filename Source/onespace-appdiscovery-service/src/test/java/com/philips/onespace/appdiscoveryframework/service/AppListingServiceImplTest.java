package com.philips.onespace.appdiscoveryframework.service;

import com.philips.onespace.appdiscoveryframework.factory.AppListingFactory;
import com.philips.onespace.appdiscoveryframework.service.interfaces.RoleBasedAppListing;
import com.philips.onespace.appdiscoveryframework.util.IAMUtil;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.model.IntrospectionResponse;
import com.philips.onespace.model.Organization;
import com.philips.onespace.model.Organizations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.philips.onespace.dto.RoleEnum;

import java.util.*;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class AppListingServiceImplTest {
    @Mock
    private IAMUtil iamUtil;
    @Mock
    private AppListingFactory appListingFactory;
    @InjectMocks
    private AppListingServiceImpl appListingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listApplications_WhenRoleExists_ReturnsApplications() throws InvalidTokenException, ResourceNotFoundException {
        UUID statusId = UUID.fromString("140ddf77-cf25-4a8e-8572-10de2efb82c0");
        String token = "1312sf";
        Map<String, String> criteria = new HashMap<>();

        // Setup the introspection response

        IntrospectionResponse introspectionResponse = new IntrospectionResponse();
        Organizations organizations = new Organizations();
        List<Organization> organizationList = new ArrayList<>();
        Organization organization = new Organization();
        List<String> roles = List.of("APPLICATIONINTEGRATORROLE");
        organization.setRoles(roles);
        organizationList.add(organization);
        organizations.setOrganizationList(organizationList);
        introspectionResponse.setOrganizations(organizations);
        // Mocking the role-based app listing
        RoleBasedAppListing roleBasedAppListing = mock(RoleBasedAppListing.class);
        Page<ApplicationEntity> expectedPage = mock(Page.class);        // Setup mocks
        when(iamUtil.introspectToken(token)).thenReturn(introspectionResponse);
        when(iamUtil.checkIfTokenContainsRole(introspectionResponse, "APPLICATIONINTEGRATORROLE")).thenReturn(Boolean.TRUE);
        when(appListingFactory.getRoleBasedAppListing(RoleEnum.APPLICATIONINTEGRATORROLE)).thenReturn(roleBasedAppListing);
        when(roleBasedAppListing.listApplications(introspectionResponse, statusId, criteria, Pageable.unpaged())).thenReturn(expectedPage);
        // Execute the method
        Page<ApplicationEntity> result = appListingService.listApplications(statusId, token, criteria, Pageable.unpaged());
        // Validate the result
        assertNotNull(result);
        verify(iamUtil).introspectToken(token);
        verify(iamUtil).checkIfTokenContainsRole(introspectionResponse, "APPLICATIONINTEGRATORROLE");
        verify(appListingFactory).getRoleBasedAppListing(RoleEnum.APPLICATIONINTEGRATORROLE);
        verify(roleBasedAppListing).listApplications(introspectionResponse, statusId, criteria, Pageable.unpaged());
    }

    @Test
    void listApplications_WhenRoleExists_ReturnsApplications_neagtive() throws InvalidTokenException, ResourceNotFoundException {
        UUID statusId = UUID.fromString("140ddf77-cf25-4a8e-8572-10de2efb82c0");
        String token = "1312sf";
        Map<String, String> criteria = new HashMap<>();
        // Setup the introspection response
        IntrospectionResponse introspectionResponse = new IntrospectionResponse();
        Organizations organizations = new Organizations();
        List<Organization> organizationList = new ArrayList<>();
        Organization organization = new Organization();
        List<String> roles = List.of("APPLICATIONINTEGRATORROfs");
        organization.setRoles(roles);
        organizationList.add(organization);
        organizations.setOrganizationList(organizationList);
        introspectionResponse.setOrganizations(organizations);
        // Mocking the role-based app listing
        RoleBasedAppListing roleBasedAppListing = mock(RoleBasedAppListing.class);

        // Setup mocks
        when(iamUtil.introspectToken(token)).thenReturn(introspectionResponse);
        when(iamUtil.checkIfTokenContainsRole(introspectionResponse, "APPLICATIONINTEGRATORROfs")).thenReturn(Boolean.TRUE);
        when(appListingFactory.getRoleBasedAppListing(RoleEnum.APPLICATIONINTEGRATORROLE)).thenReturn(null);
        when(roleBasedAppListing.listApplications(introspectionResponse, statusId, criteria, Pageable.unpaged())).thenReturn(null);

        Page<ApplicationEntity> result = appListingService.listApplications(statusId, token, criteria, Pageable.unpaged());

        assertNull(result);


    }
}
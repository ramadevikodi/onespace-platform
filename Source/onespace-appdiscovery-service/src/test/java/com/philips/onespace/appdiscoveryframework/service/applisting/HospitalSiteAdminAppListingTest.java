package com.philips.onespace.appdiscoveryframework.service.applisting;

import com.philips.onespace.appdiscoveryframework.service.interfaces.AppRegistrationService;
import com.philips.onespace.appdiscoveryframework.service.interfaces.CustomerService;
import com.philips.onespace.appdiscoveryframework.util.IAMUtil;
import com.philips.onespace.appdiscoveryframework.util.SecurityContextUtil;
import com.philips.onespace.dto.Customer;
import com.philips.onespace.dto.Market;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.repository.ApplicationRepository;
import com.philips.onespace.jpa.specification.ApplicationSpecifications;
import com.philips.onespace.model.IntrospectionResponse;
import com.philips.onespace.model.Organization;
import com.philips.onespace.model.Organizations;
import com.philips.onespace.sentinel.dto.OneSpaceProduct;
import com.philips.onespace.sentinel.service.LicenseManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class HospitalSiteAdminAppListingTest {
    @Mock
    private ApplicationSpecifications applicationSpecifications;
    @InjectMocks
    private HospitalSiteAdminAppListing hospitalSiteAdminAppListing;
    @Mock
    private IAMUtil iamUtil;
    @Mock
    private CustomerService customerService;
    @Mock
    private AppRegistrationService appRegistrationService;
    @Mock
    private LicenseManager licenseManager;
    @Mock
    private SecurityContextUtil contextUtil;
    @Mock
    private ApplicationRepository applicationRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Authentication authentication = Mockito.mock(Authentication.class);

        Object principal = "onespace";

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(principal);
        SecurityContextHolder.setContext(securityContext);

    }

    @Test
    void testListApplications() throws InvalidTokenException, ResourceNotFoundException {
        UUID statusId = UUID.randomUUID();
        Map<String, String> criteria = Map.of("key", "value");
        Pageable pageable = PageRequest.of(0, 10);

        UUID hspIamBuOrgID = UUID.fromString("140ddf77-cf25-4a8e-8572-10de2efb82c0");
        UUID customerId = UUID.fromString("140ddf77-cf25-4a8e-8572-10de2efb82c1");
        UUID marketID = UUID.fromString("140ddf77-cf25-4a8e-8572-10de2efb82c2");
        UUID externalID = UUID.fromString("140ddf77-cf25-4a8e-8572-10de2efb82c3");

        String userName = "onespace1";
        IntrospectionResponse introspectionResponse = new IntrospectionResponse();
        Organizations organizations = new Organizations();
        List<Organization> organizationList = new ArrayList<>();
        Organization organization = new Organization();
        organization.setOrganizationId(hspIamBuOrgID.toString());
        organization.setRoles(Collections.singletonList("HOSPITALSITEADMINISTRATORROLE"));
        organizationList.add(organization);
        organizations.setOrganizationList(organizationList);
        introspectionResponse.setOrganizations(organizations);
        introspectionResponse.setUsername(userName);

        Specification specification = mock(Specification.class);
        Specification<ApplicationEntity> specificationen = mock(Specification.class);
        ApplicationEntity app1 = new ApplicationEntity();
        ApplicationEntity app2 = new ApplicationEntity();
        List<ApplicationEntity> applicationEntitiesList = Arrays.asList(app1, app2);
        Page<ApplicationEntity> applicationEntities = new PageImpl<>(applicationEntitiesList, pageable, applicationEntitiesList.size());

        Customer customer = new Customer();
        customer.setId(customerId);
        Market market = new Market();
        market.setMarketId(marketID);
        customer.setMarket(market);

        List<ApplicationEntity> applicationEntities1 = new ArrayList<ApplicationEntity>();
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setId(externalID);
        applicationEntities1.add(applicationEntity);

        List<String> names = Arrays.asList("onespace");
        List<OneSpaceProduct> oneSpaceProducts = new ArrayList<OneSpaceProduct>();

        OneSpaceProduct oneSpaceProduct = new OneSpaceProduct();
        oneSpaceProduct.setExternalId(externalID);
        oneSpaceProduct.setNamedProduct(true);
        oneSpaceProduct.setNamedUsers(names);
        oneSpaceProducts.add(oneSpaceProduct);


        when(iamUtil.findUserOrg(any(IntrospectionResponse.class), eq("HOSPITALSITEADMINISTRATORROLE"))).thenReturn(hspIamBuOrgID.toString());
        when(customerService.getCustomerByHSPIamOrgID(hspIamBuOrgID)).thenReturn(customer);
        when(appRegistrationService.getAllApplicationsPublishedToMarket(marketID)).thenReturn(applicationEntities1);
        when(licenseManager.getEntitledProductsForCustomer(String.valueOf(customerId))).thenReturn(oneSpaceProducts);
        when(applicationSpecifications.buildSpecification(criteria)).thenReturn(specification);
        when(contextUtil.getUserNameFromPrincipal(SecurityContextHolder.getContext().getAuthentication().getPrincipal())).thenReturn(userName);
        when(applicationSpecifications.getApplicationEntitySpecification(specification, applicationEntities1)).thenReturn(specificationen);
        when(applicationRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(applicationEntities);

        Page<ApplicationEntity> result = hospitalSiteAdminAppListing.listApplications(introspectionResponse, statusId, criteria, pageable);

        assertNotNull(result);
    }
}

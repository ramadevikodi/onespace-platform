package com.philips.onespace.appdiscoveryframework.service;

import static com.philips.onespace.logging.LoggingAspect.logData;

import com.philips.onespace.appdiscoveryframework.factory.AppListingFactory;
import com.philips.onespace.appdiscoveryframework.service.interfaces.AppListingService;
import com.philips.onespace.appdiscoveryframework.util.IAMUtil;
import com.philips.onespace.dto.RoleEnum;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.model.IntrospectionResponse;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AppListingServiceImpl implements AppListingService {
    @Autowired
    private IAMUtil iamUtil;
    @Autowired
    private AppListingFactory appListingFactory;

    /**
     * List applications
     * @param statusId statusId
     * @param token token
     * @param criteria criteria
     * @param pageable pageable
     * @return Page<ApplicationEntity>
     * @throws ResourceNotFoundException resource not found exception
     * @throws InvalidTokenException invalid token exception
     */
    @Override
    public Page<ApplicationEntity> listApplications(UUID statusId, String token, Map<String, String> criteria, Pageable pageable) throws ResourceNotFoundException, InvalidTokenException {
        IntrospectionResponse introspectionResponse = iamUtil.introspectToken(token);
        for (RoleEnum role : RoleEnum.values()) {
            String roleName = role.name();
            Boolean hasRole = iamUtil.checkIfTokenContainsRole(introspectionResponse, roleName);
            logData(" List applications, status-id=", statusId, " role-name=",roleName);
            if(hasRole) {
                return appListingFactory.getRoleBasedAppListing(role)
                        .listApplications(introspectionResponse, statusId, criteria, pageable);
            }
        }
        return null;
    }
}

package com.philips.onespace.appdiscoveryframework.service;

import static com.philips.onespace.logging.LoggingAspect.logData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.philips.onespace.appdiscoveryframework.mapper.ApplicationMapper;
import com.philips.onespace.appdiscoveryframework.service.interfaces.AppRegistrationService;
import com.philips.onespace.appdiscoveryframework.util.SecurityContextUtil;
import com.philips.onespace.appdiscoveryframework.util.validation.ApplicationValidationUtil;
import com.philips.onespace.dto.Application;
import com.philips.onespace.exception.DatabaseConstraintViolationException;
import com.philips.onespace.exception.InvalidTokenException;
import com.philips.onespace.exception.ResourceExistsException;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.repository.ApplicationRepository;
import com.philips.onespace.model.AppNotificationEvent;
import com.philips.onespace.util.EventType;

@Service
public class AppRegistrationServiceImpl implements AppRegistrationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private SecurityContextUtil securityContextUtil;

    @Autowired
    private ApplicationValidationUtil applicationValidationUtil;
    
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    
	@Transactional(rollbackFor = { DatabaseConstraintViolationException.class, ResourceExistsException.class,
			InvalidTokenException.class, BadRequestException.class, Exception.class })
    public Application registerApplication(Application application) throws DatabaseConstraintViolationException, InvalidTokenException, ResourceExistsException, BadRequestException {
        applicationValidationUtil.validateAppRegistrationInput(application);
        ApplicationEntity appEntity = ApplicationEntity.builder().build();
        applicationMapper.dtoToEntityForRegistration(appEntity, application);
        appEntity.setRegisteredBy(securityContextUtil.getUserNameFromPrincipal(SecurityContextHolder.getContext().getAuthentication().getPrincipal()));
        appEntity.setRegisteredDateTime(LocalDateTime.now());
        appEntity.setLastModifiedDateTime(LocalDateTime.now());
        Application applicationDto =  applicationMapper.entityToDto(saveApplication(appEntity));
        //Send App notification on register application for the status Awaiting Business Owner Approval
        if(applicationDto.getStatus().getName().equalsIgnoreCase(EventType.AWAITING_BUSINESS_OWNER_APPROVAL.toString())) {
        	applicationEventPublisher.publishEvent(AppNotificationEvent.builder().eventType(EventType.fromValue(applicationDto.getStatus().getName())).applicationId(applicationDto.getId()).build());
        }
        return applicationDto;
    }
    @Override
    public Application updateApplication(UUID id, Application application) throws ResourceNotFoundException, DatabaseConstraintViolationException, ResourceExistsException, BadRequestException {
        applicationValidationUtil.validateAppUpdateInput(id, application);
        ApplicationEntity applicationEntity = applicationRepository.findById(id).get();
        String logMessage = "";
        if (securityContextUtil.hasPermission("EPO_APP.UPDATE")) {
            logMessage = "application-update";
            applicationMapper.dtoToEntityForRegistration(applicationEntity, application);
        }
        if (securityContextUtil.hasPermission("EPO_APP.L1.APPROVE")) {
            logMessage = "L1-approval";
            applicationMapper.dtoToEntityForL1Approval(applicationEntity, application);
        }
        if (securityContextUtil.hasPermission("EPO_APP.L2.APPROVE")) {
            logMessage = "L2-approval";
            applicationMapper.dtoToEntityForL2Approval(applicationEntity, application);
        }
        logData(logMessage + " application-id=" + id);
        applicationEntity.setLastModifiedDateTime(LocalDateTime.now());
        saveApplication(applicationEntity);
        //Send App notification on register application for the status Awaiting Business Owner Approval
        Application response = applicationMapper.entityToDto(applicationEntity);
        if(response.getStatus().getName().equalsIgnoreCase(EventType.AWAITING_BUSINESS_OWNER_APPROVAL.toString())) {
        	applicationEventPublisher.publishEvent(AppNotificationEvent.builder().eventType(EventType.fromValue(response.getStatus().getName())).applicationId(response.getId()).build());
        }
        return response;
    }

    @Override
    public ApplicationEntity getApplication(UUID id) throws ResourceNotFoundException {
        applicationValidationUtil.validateExistingApplicationID(id);
        return applicationRepository.findById(id).get();
    }

    @Override
    public Page<ApplicationEntity> getAllApplications(UUID statusId, Specification specification, Pageable pageable) {
        Page<ApplicationEntity> applicationEntities;
        if(statusId == null) {
            applicationEntities = applicationRepository.findAll(specification, pageable);
        } else {
            applicationEntities = applicationRepository.findAllByStatusId(statusId, pageable);
        }
        return applicationEntities;
    }

    @Override
    public List<ApplicationEntity> getAllApplications(List<UUID> uuuids) {
        List<ApplicationEntity> applicationEntities = applicationRepository.findAllById(uuuids);
        return applicationEntities;
    }

    @Override
    public List<ApplicationEntity> getAllApplicationsOfOwnerOrg(UUID ownerOrgID, UUID statusId) {
        List<ApplicationEntity> applicationEntities;
        logData("Getting all applications of owner org, owner-org-id=" + ownerOrgID + " status-id=" + statusId);
        if(statusId == null) {
            applicationEntities = applicationRepository.findAllByOwnerOrganization(ownerOrgID);
        } else {
            applicationEntities = applicationRepository.findAllByOwnerOrganizationAndStatusId(ownerOrgID, statusId);
        }
        return applicationEntities;
    }

    @Override
    public ApplicationEntity getApplication(String name) {
        return applicationRepository.findByNameIgnoreCase(name).get();
    }

    @Override
    public List<ApplicationEntity> getAllApplicationsByName(List<String> names) {
        return applicationRepository.findByNameIn(names);
    }

    @Override
    public List<ApplicationEntity> getAllApplicationsOfACategory(String categoryName) {
        return applicationRepository.findAllByCategoryName(categoryName);
    }

    @Override
    public List<ApplicationEntity> getAllApplicationsPublishedToMarket(UUID marketId) {
        return applicationRepository.findByMarkets_MarketIdAndPublishedDateTimeIsNotNull(marketId);
    }

    @Override
    public List<ApplicationEntity> getThirdPartyApplications() {
        return applicationRepository.findByIsThirdParty(true);
    }

    private ApplicationEntity saveApplication(ApplicationEntity appEntity) throws DatabaseConstraintViolationException {
        ApplicationEntity savedAppEntity;
        try {
            savedAppEntity = applicationRepository.save(appEntity);
        } catch (Exception expObj) {
            throw expObj;
        }
        return savedAppEntity;
    }

}
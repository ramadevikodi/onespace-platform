package com.philips.onespace.appdiscoveryframework.util.validation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.philips.onespace.dto.Application;
import com.philips.onespace.dto.BusinessUnit;
import com.philips.onespace.dto.Value;
import com.philips.onespace.exception.ResourceExistsException;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.repository.ApplicationRepository;
import com.philips.onespace.jpa.repository.ApplicationStatusRepository;
import com.philips.onespace.jpa.repository.BusinessUnitRepository;
import com.philips.onespace.jpa.repository.CategoryRepository;
import com.philips.onespace.jpa.repository.DeploymentRepository;
import com.philips.onespace.jpa.repository.LanguageRepository;
import com.philips.onespace.jpa.repository.ModalityRepository;
import com.philips.onespace.jpa.repository.SpecialityRepository;
import com.philips.onespace.util.ErrorMessages;

@Component
public class ApplicationValidationUtil {
    @Autowired
    private SpecialityRepository specialityRepository;
    
    @Autowired
    private LanguageRepository languageRepository;
    
    @Autowired
    private ModalityRepository modalityRepository;
    
    @Autowired
    private DeploymentRepository deploymentRepository;
    
    @Autowired
    private ApplicationStatusRepository applicationStatusRepository;
    
    @Autowired
    private ApplicationRepository applicationRepository;
    
    @Autowired
    private BusinessUnitRepository businessUnitRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    public void validateAppRegistrationInput(Application application) throws ResourceExistsException, BadRequestException {
        if(application.getRegistration() != null)
        {
            validateBusinessUnit(application);
            validateApplicationName(application);
            validateSpecialities(application);
            validateModalities(application);
            validateLanguages(application);
            validateCategory(application);
        }
        validateDeploymentMode(application);
        validateApplicationStatus(application);
    }

    public void validateCategory(Application application) throws BadRequestException {
        if(application.getRegistration().getCategory() !=null &&
        !categoryRepository.existsById(application.getRegistration().getCategory().getId())){
            throw new BadRequestException(ErrorMessages.INVALID_CATEGORY_ID);
        }
    }

    public void validateAppUpdateInput(UUID id, Application application) throws ResourceExistsException, BadRequestException, ResourceNotFoundException {
        validateExistingApplicationID(id);
        String applicationName = applicationRepository.findById(id).get().getName();
        String version = applicationRepository.findById(id).get().getVersion();
        if(application.getRegistration() != null)
        {
            validateBusinessUnit(application);
            if(!applicationName.equals(application.getRegistration().getName()) ||
                    !version.equals(application.getRegistration().getVersion())) {
                validateApplicationName(application);
            }
            validateSpecialities(application);
            validateModalities(application);
            validateLanguages(application);
            validateCategory(application);
        }
        validateDeploymentMode(application);
        validateApplicationStatus(application);
    }

    public void validateExistingApplicationID(UUID id) throws ResourceNotFoundException {
        if (!applicationRepository.existsById(id)) {
            throw new ResourceNotFoundException(ErrorMessages.INVALID_APPLICATION_ID);
        }
    }

    public void validateApplicationName(Application application) throws ResourceExistsException {
        String name = application.getRegistration().getName();
        String version = application.getRegistration().getVersion();
        BusinessUnit businessUnit = application.getRegistration().getBusinessUnit();
        if(businessUnit != null) {
            UUID ownerOrg = businessUnitRepository.findById(businessUnit.getId()).get().getId();
            if (!StringUtils.isBlank(name) && !StringUtils.isBlank(version)) {
                Optional<ApplicationEntity> applicationEntity = applicationRepository.
                        findByNameIgnoreCaseAndVersion(name, version);
                if (applicationEntity.isPresent() &&
                        applicationEntity.get().getOwnerOrganization().equals(ownerOrg)) {
                    throw new ResourceExistsException(ErrorMessages.DUPLICATE_APPLICATION_NAME);
                }
            }
        }
    }

    public void validateLanguages(Application application) throws BadRequestException {
        List<Value> languages = application.getRegistration().getLanguages();
        if(languages != null && !languages.isEmpty()) {
            if (languages.stream().anyMatch(s -> !languageRepository.existsById(s.getId()))) {
                throw new BadRequestException(ErrorMessages.INVALID_LANGUAGE_ID);
            }
        }
    }

    public void validateModalities(Application application) throws BadRequestException {
        List<Value> modalities = application.getRegistration().getModalities();
        if(modalities != null && !modalities.isEmpty()) {
            if (modalities.stream().anyMatch(s -> !modalityRepository.existsById(s.getId()))) {
                throw new BadRequestException(ErrorMessages.INVALID_MODALITY_ID);
            }
        }
    }

    public void validateSpecialities(Application application) throws BadRequestException {
        List<Value> specialities = application.getRegistration().getSpecialities();
        if(specialities != null && !specialities.isEmpty()) {
            if (specialities.stream().anyMatch(s -> !specialityRepository.existsById(s.getId()))) {
                throw new BadRequestException(ErrorMessages.INVALID_SPECIALITY_ID);
            }
        }
    }

    public void validateDeploymentMode(Application application) throws BadRequestException {
        if(application.getDeployment() != null) {
            if(application.getDeployment().getMode() != null) {
                if(!deploymentRepository.existsById(application.getDeployment().getMode().getId())) {
                    throw new BadRequestException(ErrorMessages.INVALID_DEPLOYMENT_MODE_ID);
                }
            }
        }
    }

    public void validateApplicationStatus(Application application) throws BadRequestException {
        if(application.getStatus() != null) {
            if(!applicationStatusRepository.existsById(application.getStatus().getId())) {
                throw new BadRequestException(ErrorMessages.INVALID_STATUS_ID);
            }
        }
    }

    public void validateBusinessUnit(Application application) throws BadRequestException {
        BusinessUnit businessUnit = application.getRegistration().getBusinessUnit();
        if(businessUnit != null) {
            if (businessUnitRepository.findById(businessUnit.getId()).isEmpty()) {
                throw new BadRequestException(ErrorMessages.INVALID_BUSINESS_UNIT_ID);
            }
        }
    }
}

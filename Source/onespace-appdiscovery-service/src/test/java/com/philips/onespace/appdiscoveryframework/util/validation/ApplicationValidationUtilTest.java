package com.philips.onespace.appdiscoveryframework.util.validation;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.MessageSource;

import com.philips.onespace.dto.Application;
import com.philips.onespace.dto.BusinessUnit;
import com.philips.onespace.dto.Deployment;
import com.philips.onespace.dto.Registration;
import com.philips.onespace.dto.Value;
import com.philips.onespace.exception.ResourceExistsException;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.entity.BusinessUnitEntity;
import com.philips.onespace.jpa.repository.ApplicationRepository;
import com.philips.onespace.jpa.repository.ApplicationStatusRepository;
import com.philips.onespace.jpa.repository.BusinessUnitRepository;
import com.philips.onespace.jpa.repository.CategoryRepository;
import com.philips.onespace.jpa.repository.DeploymentRepository;
import com.philips.onespace.jpa.repository.LanguageRepository;
import com.philips.onespace.jpa.repository.ModalityRepository;
import com.philips.onespace.jpa.repository.SpecialityRepository;
import com.philips.onespace.util.ErrorMessages;
import com.philips.onespace.util.LocaleUtil;

class ApplicationValidationUtilTest {

    @InjectMocks
    ApplicationValidationUtil applicationValidationUtil;

    @Mock
    SpecialityRepository specialityRepository;
    @Mock
    LanguageRepository languageRepository;
    @Mock
    ModalityRepository modalityRepository;
    @Mock
    DeploymentRepository deploymentRepository;
    @Mock
    ApplicationRepository applicationRepository;
    @Mock
    BusinessUnitRepository businessUnitRepository;
    @Mock
    ApplicationStatusRepository applicationStatusRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    MessageSource messageSource;
    @Mock
    LocaleUtil localeUtil;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void validateAppRegistrationInput() {

        UUID uuid = UUID.fromString("f4cfcff2-f77f-4e5a-bb2f-8ee157845054");

        UUID statusId = UUID.fromString("140ddf77-cf25-4a8e-8572-10de2efb82c0");
        Application application = new Application();
        Registration registration= new Registration();
        BusinessUnit businessUnit = new BusinessUnit();


        businessUnit.setId(statusId);
        registration.setName("abc");
        registration.setVersion("1.0");
        registration.setBusinessUnit(businessUnit);
        application.setRegistration(registration);

        BusinessUnitEntity businessUnitEntity = new BusinessUnitEntity();
        businessUnitEntity.setId(uuid);
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setOwnerOrganization(uuid);
        when(businessUnitRepository.findById(businessUnit.getId())).thenReturn(Optional.of(businessUnitEntity));
        when(applicationRepository.findByNameIgnoreCaseAndVersion(
                application.getRegistration().getName(),application.getRegistration().getVersion())).
                thenReturn(Optional.of(applicationEntity));


        assertThrows(ResourceExistsException.class, () -> {
            applicationValidationUtil.validateAppRegistrationInput(application);
        }, ErrorMessages.DUPLICATE_APPLICATION_NAME);
    }

    @Test
    void validateAppRegistrationInput_Speciality()  {

        UUID uuid = UUID.fromString("f4cfcff2-f77f-4e5a-bb2f-8ee157845054");

        UUID statusId = UUID.fromString("140ddf77-cf25-4a8e-8572-10de2efb82c0");
        Application application = new Application();
        Registration registration= new Registration();
        BusinessUnit businessUnit = new BusinessUnit();
        List<Value> specialities = new ArrayList<>();
        Value value = new Value();
        value.setId(uuid);
        value.setName("abcd");
        specialities.add(value);

        businessUnit.setId(statusId);
        registration.setName("abc");
        registration.setVersion("1.0");
        registration.setBusinessUnit(businessUnit);
        registration.setSpecialities(specialities);
        application.setRegistration(registration);

        BusinessUnitEntity businessUnitEntity = new BusinessUnitEntity();
        businessUnitEntity.setId(uuid);
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setOwnerOrganization(statusId);
        when(businessUnitRepository.findById(businessUnit.getId())).thenReturn(Optional.of(businessUnitEntity));
        when(applicationRepository.findByNameIgnoreCaseAndVersion(
                application.getRegistration().getName(),application.getRegistration().getVersion())).
                thenReturn(Optional.of(applicationEntity));
        when(specialityRepository.existsById(uuid)).thenReturn(Boolean.FALSE);

        assertThrows(BadRequestException.class, () -> {
            applicationValidationUtil.validateAppRegistrationInput(application);
        },ErrorMessages.INVALID_SPECIALITY_ID);

    }

    @Test
    void validateAppRegistrationInput_Modlaity()  {

        UUID uuid = UUID.fromString("f4cfcff2-f77f-4e5a-bb2f-8ee157845054");

        UUID statusId = UUID.fromString("140ddf77-cf25-4a8e-8572-10de2efb82c0");
        Application application = new Application();
        Registration registration= new Registration();
        BusinessUnit businessUnit = new BusinessUnit();
        List<Value> modalities = new ArrayList<>();
        Value value = new Value();
        value.setId(uuid);
        value.setName("abcd");
        modalities.add(value);

        businessUnit.setId(statusId);
        registration.setName("abc");
        registration.setVersion("1.0");
        registration.setBusinessUnit(businessUnit);
        registration.setModalities(modalities);
        application.setRegistration(registration);

        BusinessUnitEntity businessUnitEntity = new BusinessUnitEntity();
        businessUnitEntity.setId(uuid);
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setOwnerOrganization(statusId);
        when(businessUnitRepository.findById(businessUnit.getId())).thenReturn(Optional.of(businessUnitEntity));
        when(applicationRepository.findByNameIgnoreCaseAndVersion(
                application.getRegistration().getName(),application.getRegistration().getVersion())).
                thenReturn(Optional.of(applicationEntity));
        when(specialityRepository.existsById(uuid)).thenReturn(Boolean.TRUE);

        assertThrows(BadRequestException.class, () -> {
            applicationValidationUtil.validateAppRegistrationInput(application);
        },ErrorMessages.INVALID_MODALITY_ID);

    }

    @Test
    void validateAppRegistrationInput_Language()  {

        UUID uuid = UUID.fromString("f4cfcff2-f77f-4e5a-bb2f-8ee157845054");

        UUID statusId = UUID.fromString("140ddf77-cf25-4a8e-8572-10de2efb82c0");
        Application application = new Application();
        Registration registration= new Registration();
        BusinessUnit businessUnit = new BusinessUnit();

        List<Value> languages = new ArrayList<>();
        Value value = new Value();
        value.setId(uuid);
        value.setName("abcd");
        languages.add(value);

        businessUnit.setId(statusId);
        registration.setName("abc");
        registration.setVersion("1.0");
        registration.setBusinessUnit(businessUnit);
        registration.setLanguages(languages);
        application.setRegistration(registration);

        BusinessUnitEntity businessUnitEntity = new BusinessUnitEntity();
        businessUnitEntity.setId(uuid);
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setOwnerOrganization(statusId);
        when(businessUnitRepository.findById(businessUnit.getId())).thenReturn(Optional.of(businessUnitEntity));
        when(applicationRepository.findByNameIgnoreCaseAndVersion(
                application.getRegistration().getName(),application.getRegistration().getVersion())).
                thenReturn(Optional.of(applicationEntity));
        when(specialityRepository.existsById(uuid)).thenReturn(Boolean.TRUE);

        assertThrows(BadRequestException.class, () -> {
            applicationValidationUtil.validateAppRegistrationInput(application);
        },ErrorMessages.INVALID_LANGUAGE_ID);

    }

    @Test
    void validateAppRegistrationInput_BusinessUnit()  {

        Application application = new Application();
        Registration registration= new Registration();
        BusinessUnit businessUnit = new BusinessUnit();

        registration.setName("abc");
        registration.setVersion("1.0");
        registration.setBusinessUnit(businessUnit);
        application.setRegistration(registration);

        when(businessUnitRepository.findById(businessUnit.getId())).thenReturn(Optional.empty());

        assertThrows(BadRequestException.class, () -> {
            applicationValidationUtil.validateAppRegistrationInput(application);
        },ErrorMessages.INVALID_BUSINESS_UNIT_ID);

    }

    @Test
    void validateAppRegistrationInput_Category()  {

        UUID uuid = UUID.fromString("f4cfcff2-f77f-4e5a-bb2f-8ee157845054");

        UUID statusId = UUID.fromString("140ddf77-cf25-4a8e-8572-10de2efb82c0");
        Application application = new Application();
        Registration registration= new Registration();
        BusinessUnit businessUnit = new BusinessUnit();


        Value value = new Value();
        value.setId(uuid);
        value.setName("abcd");


        businessUnit.setId(statusId);
        registration.setName("abc");
        registration.setVersion("1.0");
        registration.setBusinessUnit(businessUnit);
        registration.setCategory(value);
        application.setRegistration(registration);

        BusinessUnitEntity businessUnitEntity = new BusinessUnitEntity();
        businessUnitEntity.setId(uuid);
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setOwnerOrganization(statusId);
        when(businessUnitRepository.findById(businessUnit.getId())).thenReturn(Optional.of(businessUnitEntity));
        when(applicationRepository.findByNameIgnoreCaseAndVersion(
                application.getRegistration().getName(),application.getRegistration().getVersion())).
                thenReturn(Optional.of(applicationEntity));
        when(specialityRepository.existsById(uuid)).thenReturn(Boolean.TRUE);

        assertThrows(BadRequestException.class, () -> {
            applicationValidationUtil.validateAppRegistrationInput(application);
        },ErrorMessages.INVALID_CATEGORY_ID);

    }

    @Test
    void validateAppRegistrationInput_No_Registration()  {

        Application application = new Application();

        Deployment deployment = new Deployment();
        Value value = new Value();
        value.setName("abcd");
        deployment.setMode(value);

        application.setDeployment(deployment);

        assertThrows(BadRequestException.class, () -> {
            applicationValidationUtil.validateAppRegistrationInput(application);
        },ErrorMessages.INVALID_DEPLOYMENT_MODE_ID);

    }

    @Test
    void validateAppRegistrationInput_Val_Application_Status(){

        UUID uuid = UUID.fromString("f4cfcff2-f77f-4e5a-bb2f-8ee157845054");

        Application application = new Application();
        Value value = new Value();
        value.setId(uuid);
        value.setName("abcd");
        application.setStatus(value);
        assertThrows(BadRequestException.class, () -> {
            applicationValidationUtil.validateAppRegistrationInput(application);
        },ErrorMessages.INVALID_STATUS_ID);

    }

    @Test
    void validateAppUpdateInput_Resource_Not_Found()  {
        UUID id = UUID.fromString("f4cfcff2-f77f-4e5a-bb2f-8ee157845054");
        Application application = new Application();
        BusinessUnit businessUnit = new BusinessUnit();
        Registration registration = new Registration();
        registration.setBusinessUnit(businessUnit);

        application.setRegistration(registration);

        when(applicationRepository.existsById(id)).thenReturn(Boolean.FALSE);

        assertThrows(ResourceNotFoundException.class, () -> {
            applicationValidationUtil.validateAppUpdateInput(id,application);
        },ErrorMessages.INVALID_APPLICATION_ID);

    }

    @Test
    void validateAppUpdateInput() throws ResourceExistsException, BadRequestException, ResourceNotFoundException {
        UUID id = UUID.fromString("f4cfcff2-f77f-4e5a-bb2f-8ee157845054");
        Application application = new Application();

        Registration registration = new Registration();
        registration.setVersion("3232");
        registration.setName("cv");

        application.setRegistration(registration);
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setName("abc");
        applicationEntity.setVersion("2332");

        when(applicationRepository.existsById(id)).thenReturn(Boolean.TRUE);
        when(applicationRepository.findById(id)).thenReturn(Optional.of(applicationEntity));

        applicationValidationUtil.validateAppUpdateInput(id,application);

    }


    @Test
    void validateAppUpdateInput_version() throws ResourceExistsException, BadRequestException, ResourceNotFoundException {
        UUID id = UUID.fromString("f4cfcff2-f77f-4e5a-bb2f-8ee157845054");
        Application application = new Application();

        Registration registration = new Registration();
        registration.setVersion("3232");
        registration.setName("abc");

        application.setRegistration(registration);
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setName("abc");
        applicationEntity.setVersion("2332");

        when(applicationRepository.existsById(id)).thenReturn(Boolean.TRUE);
        when(applicationRepository.findById(id)).thenReturn(Optional.of(applicationEntity));

        applicationValidationUtil.validateAppUpdateInput(id,application);

    }

    @Test
    void validateAppUpdateInput_No_Registration() throws ResourceExistsException, BadRequestException, ResourceNotFoundException {
        UUID id = UUID.fromString("f4cfcff2-f77f-4e5a-bb2f-8ee157845054");
        Application application = new Application();
        ApplicationEntity applicationEntity = new ApplicationEntity();
        applicationEntity.setName("abc");
        applicationEntity.setVersion("2332");

        when(applicationRepository.existsById(id)).thenReturn(Boolean.TRUE);
        when(applicationRepository.findById(id)).thenReturn(Optional.of(applicationEntity));

        applicationValidationUtil.validateAppUpdateInput(id,application);

    }

}

package com.philips.onespace.appdiscoveryframework.specifications;

import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.specification.ApplicationSpecifications;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class AppSpecificationTest {

    @Mock
    ApplicationSpecifications applicationSpecifications;

    @BeforeAll
    public static void beforeAll() {
        MockitoAnnotations.openMocks(AppSpecificationTest.class);
    }

    @Test
    void testHasDeploymentModes_singleValue() {
        String mode = "multi-tenant";
        Specification<ApplicationEntity> specification = applicationSpecifications.hasDeploymentMode(mode);
        assertNull(specification);
    }

    @Test
    void testHasDeploymentModes_multipleValues() {
        String mode = "multi-tenant,instance";
        Specification<ApplicationEntity> specification = applicationSpecifications.hasDeploymentMode(mode);
        assertNull(specification);
    }

}

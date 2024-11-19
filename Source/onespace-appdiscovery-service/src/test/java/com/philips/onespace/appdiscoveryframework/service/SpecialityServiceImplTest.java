package com.philips.onespace.appdiscoveryframework.service;


import com.philips.onespace.appdiscoveryframework.mapper.ValueMapper;
import com.philips.onespace.dto.Value;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.entity.SpecialityEntity;
import com.philips.onespace.jpa.repository.SpecialityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class SpecialityServiceImplTest {

    @InjectMocks
    private SpecialityServiceImpl specialityService;

    @Mock
    private SpecialityRepository specialityRepository;

    @Mock
    private ValueMapper valueMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetSpecialitiesSuccess() {
        List<ApplicationEntity> applicationEntityList = Arrays.asList(new ApplicationEntity());
        List<SpecialityEntity> specialityEntities = Arrays.asList(new SpecialityEntity(UUID.randomUUID(), "Cardiology", "Treats disorders of the heart and blood vessels.", applicationEntityList), new SpecialityEntity(UUID.randomUUID(), "Dermatology", "Focuses on skin, hair, and nail conditions.", applicationEntityList));

        List<Value> expectedValues = Arrays.asList(new Value(UUID.randomUUID(), "Cardiology","Treats disorders of the heart and blood vessels."), new Value(UUID.randomUUID(), "Dermatology", "Focuses on skin, hair, and nail conditions."));

        when(specialityRepository.findAll()).thenReturn(specialityEntities);
        when(valueMapper.mapSpecialities(specialityEntities)).thenReturn(expectedValues);

        List<Value> actualValues = specialityService.getSpecialities();

        assertEquals(expectedValues, actualValues);

        verify(specialityRepository, times(1)).findAll();
        verify(valueMapper, times(1)).mapSpecialities(specialityEntities);
    }

    @Test
    void testGetSpecialitiesEmpty() {

        List<SpecialityEntity> specialityEntities = Arrays.asList();

        List<Value> expectedValues = Arrays.asList();

        when(specialityRepository.findAll()).thenReturn(specialityEntities);
        when(valueMapper.mapSpecialities(specialityEntities)).thenReturn(expectedValues);

        List<Value> actualValues = specialityService.getSpecialities();

        assertEquals(expectedValues, actualValues);

        verify(specialityRepository, times(1)).findAll();
        verify(valueMapper, times(1)).mapSpecialities(specialityEntities);
    }


}

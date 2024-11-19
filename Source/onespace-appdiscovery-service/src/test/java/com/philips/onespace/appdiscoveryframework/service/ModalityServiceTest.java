package com.philips.onespace.appdiscoveryframework.service;

import com.philips.onespace.appdiscoveryframework.mapper.ValueMapper;
import com.philips.onespace.dto.Value;
import com.philips.onespace.jpa.entity.ApplicationEntity;
import com.philips.onespace.jpa.entity.ModalityEntity;
import com.philips.onespace.jpa.repository.ModalityRepository;
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

class ModalityServiceTest {

    @InjectMocks
    private ModalityService modalityService;

    @Mock
    private ModalityRepository modalityRepository;

    @Mock
    private ValueMapper valueMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetModalitiesSuccess() {

        List<ApplicationEntity> modalitiesList = Arrays.asList(new ApplicationEntity());

        List<ModalityEntity> modalitiesEntities = Arrays.asList(new ModalityEntity(UUID.randomUUID(), "CT Scan", "", modalitiesList), new ModalityEntity(UUID.randomUUID(), "MRI", "", modalitiesList));

        List<Value> expectedValues = Arrays.asList(new Value(UUID.randomUUID(), "CT Scan", ""), new Value(UUID.randomUUID(), "MRI", ""));

        when(modalityRepository.findAll()).thenReturn(modalitiesEntities);
        when(valueMapper.mapModalities(modalitiesEntities)).thenReturn(expectedValues);

        List<Value> actualValues = modalityService.getModalities();

        assertEquals(expectedValues, actualValues);

        verify(modalityRepository, times(1)).findAll();
        verify(valueMapper, times(1)).mapModalities(modalitiesEntities);
    }

    @Test
    void testGetModalitiesEmpty() {

        List<ModalityEntity> modalitiesEntities = Arrays.asList();

        List<Value> expectedValues = Arrays.asList();

        when(modalityRepository.findAll()).thenReturn(modalitiesEntities);
        when(valueMapper.mapModalities(modalitiesEntities)).thenReturn(expectedValues);

        List<Value> actualValues = modalityService.getModalities();

        assertEquals(expectedValues, actualValues);

        verify(modalityRepository, times(1)).findAll();
        verify(valueMapper, times(1)).mapModalities(modalitiesEntities);
    }


}

package com.philips.onespace.appdiscoveryframework.service;


import com.philips.onespace.appdiscoveryframework.mapper.BusinessUnitMapper;
import com.philips.onespace.dto.BusinessUnit;
import com.philips.onespace.jpa.entity.BusinessUnitEntity;
import com.philips.onespace.jpa.repository.BusinessUnitRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BusinessUnitServiceImplTest {

    @InjectMocks
    private BusinessUnitServiceImpl businessUnitService;

    @Mock
    private BusinessUnitRepository businessUnitRepository;

    @Mock
    private BusinessUnitMapper businessUnitMapper;

    private BusinessUnitEntity businessUnitEntity;
    private BusinessUnit businessUnit;
    private UUID hspIamOrgID;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hspIamOrgID = UUID.randomUUID();
        businessUnitEntity = new BusinessUnitEntity();
        businessUnit = new BusinessUnit();
    }

    @Test
    void testGetBusinessUnitByHSPIamOrgID_Found() {

        when(businessUnitRepository.findByBusinessUnitExtSystemEntity_HspIamOrgId(hspIamOrgID)).thenReturn(Optional.of(businessUnitEntity));
        when(businessUnitMapper.entityToDto(businessUnitEntity)).thenReturn(businessUnit);

        BusinessUnit result = businessUnitService.getBusinessUnitByHSPIamOrgID(hspIamOrgID);

        assertNotNull(result);
        assertEquals(businessUnit, result);

        verify(businessUnitRepository).findByBusinessUnitExtSystemEntity_HspIamOrgId(hspIamOrgID);
    }

    @Test
    void testGetBusinessUnitByHSPIamOrgID_NotFound() {

        when(businessUnitRepository.findByBusinessUnitExtSystemEntity_HspIamOrgId(hspIamOrgID)).thenReturn(Optional.empty());

        BusinessUnit result = businessUnitService.getBusinessUnitByHSPIamOrgID(hspIamOrgID);

        assertNull(result);

        verify(businessUnitRepository).findByBusinessUnitExtSystemEntity_HspIamOrgId(hspIamOrgID);
    }

    @Test
    void testGetBusinessUnitByName() {

        String name = "Test";
        when(businessUnitRepository.findByName(name)).thenReturn(Optional.of(businessUnitEntity));
        when(businessUnitMapper.entityToDto(businessUnitEntity)).thenReturn(businessUnit);

        BusinessUnit result = businessUnitService.getBusinessUnitByName(name);

        assertNotNull(result);
        assertEquals(businessUnit, result);
        verify(businessUnitRepository).findByName(name);

    }

    @Test
    void testGetBusinessUnitByName_NotFound() {

        String name = "Non Exist Data";
        when(businessUnitRepository.findByName(name)).thenReturn(Optional.empty());

        BusinessUnit result = businessUnitService.getBusinessUnitByName(name);

        assertNull(result);
        verify(businessUnitRepository).findByName(name);

    }

    @Test
    void testGetBusinessCategories() {

        List<BusinessUnitEntity> businessUnitEntities = Collections.singletonList(businessUnitEntity);
        List<BusinessUnit> businessUnits = Collections.singletonList(businessUnit);

        when(businessUnitRepository.findAll(Sort.by("cluster", "businessSegments"))).thenReturn(businessUnitEntities);
        when(businessUnitMapper.mapBusinessCluster(businessUnitEntities)).thenReturn(businessUnits);

        List<BusinessUnit> result = businessUnitService.getBusinessCategories();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(businessUnits, result);

        verify(businessUnitRepository).findAll(Sort.by("cluster", "businessSegments"));

    }


}

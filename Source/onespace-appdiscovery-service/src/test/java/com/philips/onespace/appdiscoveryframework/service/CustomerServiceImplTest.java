package com.philips.onespace.appdiscoveryframework.service;

import com.philips.onespace.appdiscoveryframework.mapper.CustomerMapper;
import com.philips.onespace.dto.Customer;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.jpa.entity.CustomerEntity;
import com.philips.onespace.jpa.repository.CustomerRepository;
import com.philips.onespace.util.ErrorMessages;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private UUID hspIamOrgID;
    private CustomerEntity customerEntity;
    private Customer customerDto;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        hspIamOrgID = UUID.randomUUID();
        customerEntity = new CustomerEntity();
        customerDto = new Customer();
    }

    @Test
    void testGetCustomerByHSPIamOrgID_Success() throws ResourceNotFoundException {

        when(customerRepository.findByCustomerExtSystemEntity_HspIamOrgId(hspIamOrgID)).thenReturn(Optional.of(customerEntity));
        when(customerMapper.entityToDto(customerEntity)).thenReturn(customerDto);

        Customer result = customerService.getCustomerByHSPIamOrgID(hspIamOrgID);

        assertNotNull(result);
        assertEquals(customerDto, result);
        verify(customerRepository).findByCustomerExtSystemEntity_HspIamOrgId(hspIamOrgID);
        verify(customerMapper).entityToDto(customerEntity);
    }

    @Test
    void testGetCustomerByHSPIamOrgID_NotFound() {

        when(customerRepository.findByCustomerExtSystemEntity_HspIamOrgId(hspIamOrgID)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            customerService.getCustomerByHSPIamOrgID(hspIamOrgID);
        });

        assertEquals(ErrorMessages.CUSTOMER_NOT_FOUND, exception.getMessage());
        verify(customerRepository).findByCustomerExtSystemEntity_HspIamOrgId(hspIamOrgID);
    }


}

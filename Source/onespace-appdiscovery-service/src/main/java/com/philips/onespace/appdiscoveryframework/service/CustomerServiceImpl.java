package com.philips.onespace.appdiscoveryframework.service;

import static com.philips.onespace.logging.LoggingAspect.logData;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.philips.onespace.appdiscoveryframework.mapper.CustomerMapper;
import com.philips.onespace.appdiscoveryframework.service.interfaces.CustomerService;
import com.philips.onespace.dto.Customer;
import com.philips.onespace.exception.ResourceNotFoundException;
import com.philips.onespace.jpa.entity.CustomerEntity;
import com.philips.onespace.jpa.repository.CustomerRepository;
import com.philips.onespace.util.ErrorMessages;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CustomerMapper customerMapper;

    /**
	 * This method retrieves all the Customers for the given iam organization.
	 *
	 * @param hspIamOrgID, the iam organization
	 * @return List of customers.
	 */
    @Override
    public Customer getCustomerByHSPIamOrgID(UUID hspIamOrgID) throws ResourceNotFoundException {
        Optional<CustomerEntity> customerEntity = customerRepository.findByCustomerExtSystemEntity_HspIamOrgId(hspIamOrgID);
        logData(" Getting Customer by HSP IAM Org ID, customer-entity=",customerEntity);
        if(!customerEntity.isPresent()) {
            throw new ResourceNotFoundException(ErrorMessages.CUSTOMER_NOT_FOUND);
        }
        return customerMapper.entityToDto(customerEntity.get());
    }
}

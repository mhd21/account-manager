package com.mpakbaz.accountManager.Services;

import java.util.UUID;

import com.mpakbaz.accountManager.infrastructure.database.models.Customer;
import com.mpakbaz.accountManager.infrastructure.database.repositories.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer getCustomer(UUID customerId) {
        return customerRepository.getById(customerId);
    }

}

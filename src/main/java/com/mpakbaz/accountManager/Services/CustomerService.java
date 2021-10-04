package com.mpakbaz.accountManager.Services;

import java.util.Optional;
import java.util.UUID;

import com.mpakbaz.accountManager.exceptions.CustomerNotFoundException;
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

    public Customer updateCustomer(Customer customer) throws CustomerNotFoundException {

        Optional<Customer> customerOptional = this.customerRepository.findById(customer.getId());
        if (!customerOptional.isPresent()) {
            throw new CustomerNotFoundException("Customer with id " + customer.getId() + " does not exist");
        }

        return customerRepository.save(customer);
    }

    public void deleteCustomer(UUID customerId) throws CustomerNotFoundException {
        Optional<Customer> customerOptional = this.customerRepository.findById(customerId);
        if (!customerOptional.isPresent()) {
            throw new CustomerNotFoundException("Customer with id " + customerId + " does not exist");
        }

        this.customerRepository.delete(customerOptional.get());
    }

}

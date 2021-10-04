package com.mpakbaz.accountManager.http.controllers;

import java.util.UUID;

import javax.validation.Valid;

import com.mpakbaz.accountManager.Services.CustomerService;
import com.mpakbaz.accountManager.exceptions.CustomerNotFoundException;
import com.mpakbaz.accountManager.http.inputs.CreateCustomerInput;
import com.mpakbaz.accountManager.http.inputs.UpdateCustomerInput;
import com.mpakbaz.accountManager.http.payloads.CustomerPayload;
import com.mpakbaz.accountManager.infrastructure.database.models.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/customer")
@Validated
@Api(tags = "Customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping(path = "/create")
    public CustomerPayload createCustomer(@Valid @RequestBody CreateCustomerInput input) {
        Customer customer = new Customer();
        customer.setName(input.getName());
        Customer savedCustomer = this.customerService.createCustomer(customer);
        return new CustomerPayload(savedCustomer);

    }

    @GetMapping(path = "/{customerId}")
    public CustomerPayload getCustomer(
            @PathVariable @Valid @com.mpakbaz.accountManager.http.validators.UUID String customerId) {

        Customer customer = this.customerService.getCustomer(UUID.fromString(customerId));
        return new CustomerPayload(customer);
    }

    @PutMapping(path = "/{customerId}")
    public CustomerPayload updateCustomer(
            @PathVariable @Valid @com.mpakbaz.accountManager.http.validators.UUID String customerId,
            @Valid @RequestBody UpdateCustomerInput input) throws CustomerNotFoundException {

        Customer customer = new Customer(UUID.fromString(customerId));

        customer.setName(input.getName());

        Customer updatedCustomer = this.customerService.updateCustomer(customer);

        return new CustomerPayload(updatedCustomer);
    }

    @DeleteMapping(path = "/{customerId}")
    public ResponseEntity<?> deleteCustomer(
            @PathVariable @Valid @com.mpakbaz.accountManager.http.validators.UUID String customerId)
            throws CustomerNotFoundException {

        this.customerService.deleteCustomer(UUID.fromString(customerId));

        return ResponseEntity.ok().build();
    }
}

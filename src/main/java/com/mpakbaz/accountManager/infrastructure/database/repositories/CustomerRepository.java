package com.mpakbaz.accountManager.infrastructure.database.repositories;

import java.util.UUID;

import com.mpakbaz.accountManager.infrastructure.database.models.Customer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

}

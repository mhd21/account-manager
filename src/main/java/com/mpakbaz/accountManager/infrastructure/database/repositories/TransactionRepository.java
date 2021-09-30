package com.mpakbaz.accountManager.infrastructure.database.repositories;

import java.util.UUID;

import com.mpakbaz.accountManager.infrastructure.database.models.Transaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

}

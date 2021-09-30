package com.mpakbaz.accountManager.infrastructure.database.repositories;

import java.time.LocalDateTime;
import java.util.UUID;

import com.mpakbaz.accountManager.infrastructure.database.models.Transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionLogRepository extends JpaRepository<Transaction, UUID> {

    @Query(value = "select * from transactions where created_at between :from and :to", nativeQuery = true)
    public Page<Transaction> getTransactionLogs(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to,
            Pageable pageable);

}

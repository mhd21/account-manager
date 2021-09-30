package com.mpakbaz.accountManager.infrastructure.database.repositories;

import java.math.BigDecimal;
import java.util.UUID;

import com.mpakbaz.accountManager.infrastructure.database.models.TransactionDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TransactionDetailRepository extends JpaRepository<TransactionDetail, Integer> {

    @Query(value = "SELECT SUM(amount) as balance FROM transaction_details where account_id = :accountId GROUP BY account_id LIMIT 1;", nativeQuery = true)
    public BigDecimal accountBalance(@Param("accountId") UUID accountId);

}

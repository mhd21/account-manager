package com.mpakbaz.accountManager.http.payloads;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.mpakbaz.accountManager.infrastructure.database.models.Transaction;

public class TransactionPayload {

    private UUID id;

    private UUID accountId;

    private BigDecimal amount;

    private Date createdAt;

    private String type;

    public TransactionPayload(Transaction transaction) {
        this.id = transaction.getId();
        this.accountId = transaction.getAccountId();
        this.amount = transaction.getAmount();
        this.createdAt = transaction.getCreatedAt();
        this.type = transaction.getType();
    }

    public UUID getId() {
        return id;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public String getType() {
        return type;
    }

}

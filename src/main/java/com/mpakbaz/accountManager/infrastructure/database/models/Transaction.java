package com.mpakbaz.accountManager.infrastructure.database.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "transactions")
public class Transaction extends EntityWithUUID {

    public Transaction() {
        super();
    }

    @Column(name = "account_id")
    private UUID accountId;

    @Column(name = "amount", columnDefinition = "decimal(12,2)")
    private BigDecimal amount;

    @Column(name = "type")
    private String type;

    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'hh:mm")
    @Column(name = "created_at")
    private Date createdAt;

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}

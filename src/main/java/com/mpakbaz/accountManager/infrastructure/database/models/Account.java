package com.mpakbaz.accountManager.infrastructure.database.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.math.BigDecimal;
import java.util.UUID;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.ReadOnlyProperty;

@Entity
@Table(name = "accounts")
public class Account extends EntityWithUUID {

    @Type(type = "pg-uuid")
    private UUID customerId;

    @Column(name = "currency")
    private String currency;

    @ReadOnlyProperty
    private BigDecimal balance;

    public Account() {
        super();
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCustomerId(UUID customerId) {
        this.customerId = customerId;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}

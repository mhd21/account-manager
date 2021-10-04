package com.mpakbaz.accountManager.http.payloads;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountPayload {

    private UUID id;

    private UUID customerId;

    private String name;

    private String currency;

    private BigDecimal balance;

    public AccountPayload(com.mpakbaz.accountManager.infrastructure.database.models.Account account) {
        this.id = account.getId();
        this.name = account.getName();
        this.currency = account.getCurrency();
        this.customerId = account.getCustomer().getId();
        this.balance = account.getBalance();
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return this.currency;
    }

    public BigDecimal getBalance() {
        return this.balance;
    }

}

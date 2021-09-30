package com.mpakbaz.accountManager.http.payloads;

import java.util.UUID;

public class AccountPayload {

    private UUID id;
    private UUID customerId;

    private String currency;

    public AccountPayload(com.mpakbaz.accountManager.infrastructure.database.models.Account account) {
        this.id = account.getId();
        this.currency = account.getCurrency();
        this.customerId = account.getCustomerId();
    }

    public UUID getId() {
        return id;
    }

    public UUID getCustomerId() {
        return customerId;
    }

    public String getCurrency() {
        return this.currency;
    }

}

package com.mpakbaz.accountManager.http.payloads;

import java.util.UUID;

public class CustomerPayload {

    private UUID id;
    private String name;

    public CustomerPayload(com.mpakbaz.accountManager.infrastructure.database.models.Customer customer) {
        this.id = customer.getId();
        this.name = customer.getName();
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}

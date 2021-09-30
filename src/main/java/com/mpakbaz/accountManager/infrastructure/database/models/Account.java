package com.mpakbaz.accountManager.infrastructure.database.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;
import org.hibernate.annotations.Type;

@Entity
@Table(name = "accounts")
public class Account extends EntityWithUUID {

    @Type(type = "pg-uuid")
    private UUID customerId;

    @Column(name = "currency")
    private String currency;

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

}

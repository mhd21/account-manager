package com.mpakbaz.accountManager.infrastructure.database.models;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "customers")
public class Customer extends EntityWithUUID {
    @Column(name = "name")
    private String name;

    public Customer() {
        super();
    }

    public Customer(UUID id) {
        this.setId(id);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

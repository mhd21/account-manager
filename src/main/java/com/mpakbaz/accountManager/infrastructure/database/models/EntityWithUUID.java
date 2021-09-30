package com.mpakbaz.accountManager.infrastructure.database.models;

import org.hibernate.annotations.Type;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

@MappedSuperclass
public class EntityWithUUID {

    @Id
    @Type(type = "pg-uuid")
    private UUID id;

    public EntityWithUUID() {
        this.id = UUID.randomUUID();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setId(String id) {
        this.id = UUID.fromString(id);
    }

    public UUID getId() {
        return id;
    }

}

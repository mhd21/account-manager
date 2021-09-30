package com.mpakbaz.accountManager.http.inputs;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class TransferInput {

    @com.mpakbaz.accountManager.http.validators.UUID
    private String fromAccountId;

    @com.mpakbaz.accountManager.http.validators.UUID
    private String toAccountId;

    @Min(1)
    @NotNull
    private BigDecimal amount;

    public UUID getFromAccountId() {
        return UUID.fromString(fromAccountId);
    }

    public UUID getToAccountId() {
        return UUID.fromString(toAccountId);
    }

    public BigDecimal getAmount() {
        return amount;
    }

}

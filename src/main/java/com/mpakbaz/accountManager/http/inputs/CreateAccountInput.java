package com.mpakbaz.accountManager.http.inputs;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.mpakbaz.accountManager.http.validators.Currency;

public class CreateAccountInput {

    @com.mpakbaz.accountManager.http.validators.UUID
    private String customerId;

    @Currency
    @NotEmpty
    private String currency;

    @Min(0)
    @NotNull
    private BigDecimal openningBalance = BigDecimal.ZERO;

    public UUID getCustomerId() {
        return UUID.fromString(this.customerId);
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getOpenningBalance() {
        return openningBalance;
    }
}

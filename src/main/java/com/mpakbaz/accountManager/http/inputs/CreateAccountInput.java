package com.mpakbaz.accountManager.http.inputs;

import java.math.BigDecimal;
import java.util.UUID;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    @NotEmpty
    @Size(min = 4, max = 255)
    private String name;

    public String getName() {
        return this.name;
    }

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

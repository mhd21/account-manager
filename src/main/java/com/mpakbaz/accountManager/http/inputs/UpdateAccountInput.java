package com.mpakbaz.accountManager.http.inputs;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UpdateAccountInput {

    @NotEmpty
    @Size(min = 4, max = 255)
    private String name;

    public String getName() {
        return this.name;
    }
}

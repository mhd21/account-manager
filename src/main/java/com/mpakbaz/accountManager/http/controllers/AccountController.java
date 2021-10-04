package com.mpakbaz.accountManager.http.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

import java.util.UUID;

import javax.validation.Valid;

import com.mpakbaz.accountManager.Services.AccountService;
import com.mpakbaz.accountManager.exceptions.AccountNotFoundException;
import com.mpakbaz.accountManager.exceptions.CustomerNotFoundException;
import com.mpakbaz.accountManager.http.inputs.CreateAccountInput;
import com.mpakbaz.accountManager.http.payloads.AccountPayload;
import com.mpakbaz.accountManager.infrastructure.database.models.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;

@RestController
@RequestMapping("/account")
@Validated
@Api(tags = "Account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping(path = "/create")
    public AccountPayload createAccount(@Valid @RequestBody CreateAccountInput input)
            throws CustomerNotFoundException, AccountNotFoundException {
        Account account = new Account();
        account.setCurrency(input.getCurrency());

        Account createdAccount = accountService.createAccount(input.getCustomerId(), account,
                input.getOpenningBalance());

        return new AccountPayload(createdAccount);
    }

    @GetMapping(path = "/{accountId}")
    public AccountPayload getAccount(
            @PathVariable @Valid @com.mpakbaz.accountManager.http.validators.UUID String accountId) {
        Account account = this.accountService.getAccount(UUID.fromString(accountId));
        return new AccountPayload(account);
    }

}

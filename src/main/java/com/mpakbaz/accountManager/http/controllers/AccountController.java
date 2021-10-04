package com.mpakbaz.accountManager.http.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
import com.mpakbaz.accountManager.http.inputs.UpdateAccountInput;
import com.mpakbaz.accountManager.http.payloads.AccountPayload;
import com.mpakbaz.accountManager.infrastructure.database.models.Account;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        account.setName(input.getName());
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

    @PutMapping(path = "/{accountId}")
    public AccountPayload updateAccount(
            @PathVariable @Valid @com.mpakbaz.accountManager.http.validators.UUID String accountId,
            @Valid @RequestBody UpdateAccountInput input) throws AccountNotFoundException {

        Account account = new Account(UUID.fromString(accountId));
        account.setName(input.getName());

        Account updatedAccount = accountService.updateAccount(account);

        return new AccountPayload(updatedAccount);
    }

    @DeleteMapping(path = "/{accountId}")
    public ResponseEntity<?> deleteAccount(
            @PathVariable @Valid @com.mpakbaz.accountManager.http.validators.UUID String accountId)
            throws AccountNotFoundException {

        this.accountService.deleteAccount(UUID.fromString(accountId));

        return ResponseEntity.ok().build();
    }

}

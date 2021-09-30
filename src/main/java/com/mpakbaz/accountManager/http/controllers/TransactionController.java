package com.mpakbaz.accountManager.http.controllers;

import javax.validation.Valid;

import com.mpakbaz.accountManager.Services.TransactionService;
import com.mpakbaz.accountManager.exceptions.AccountBalanceException;
import com.mpakbaz.accountManager.http.inputs.TransferInput;
import com.mpakbaz.accountManager.http.payloads.TransactionPayload;
import com.mpakbaz.accountManager.infrastructure.database.models.Transaction;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/transaction")
@Api(tags = "Transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping(path = "/transfer")
    public TransactionPayload transfer(@Valid @RequestBody TransferInput input) throws AccountBalanceException {
        Transaction transaction;

        transaction = transactionService.transferTransaction(input.getFromAccountId(), input.getToAccountId(),
                input.getAmount());

        return new TransactionPayload(transaction);
    }
}

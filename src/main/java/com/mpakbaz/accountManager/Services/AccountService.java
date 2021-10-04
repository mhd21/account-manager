package com.mpakbaz.accountManager.Services;

import java.math.BigDecimal;
import java.util.UUID;

import com.mpakbaz.accountManager.infrastructure.database.models.Account;
import com.mpakbaz.accountManager.infrastructure.database.repositories.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private AccountRepository accountRepository;

    private TransactionService transactionService;

    @Autowired
    public AccountService(AccountRepository accountRepository, TransactionService transactionService) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account createAccount(Account account, BigDecimal openningBalance) {
        Account createdAccount = this.createAccount(account);
        this.transactionService.depositTransaction(createdAccount.getId(), openningBalance);

        return this.getAccount(createdAccount.getId());
    }

    public Account getAccount(UUID accountId) {
        Account account = this.accountRepository.getById(accountId);
        BigDecimal balance = this.transactionService.getAccountBalance(accountId);
        account.setBalance(balance);
        return account;
    }

}

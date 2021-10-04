package com.mpakbaz.accountManager.Services;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import com.mpakbaz.accountManager.exceptions.AccountNotFoundException;
import com.mpakbaz.accountManager.exceptions.CustomerNotFoundException;
import com.mpakbaz.accountManager.infrastructure.database.models.Account;
import com.mpakbaz.accountManager.infrastructure.database.models.Customer;
import com.mpakbaz.accountManager.infrastructure.database.repositories.AccountRepository;
import com.mpakbaz.accountManager.infrastructure.database.repositories.CustomerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private CustomerRepository customerRepository;

    private AccountRepository accountRepository;

    private TransactionService transactionService;

    @Autowired
    public AccountService(CustomerRepository customerRepository, AccountRepository accountRepository,
            TransactionService transactionService) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Account createAccount(UUID customerId, Account account, BigDecimal openningBalance)
            throws CustomerNotFoundException, AccountNotFoundException {
        Optional<Customer> customerOptional = this.customerRepository.findById(customerId);
        if (!customerOptional.isPresent()) {
            throw new CustomerNotFoundException("Customer with id " + customerId + " does not exist");
        }
        account.setCustomer(customerOptional.get());
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

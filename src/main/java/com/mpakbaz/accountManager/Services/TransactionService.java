package com.mpakbaz.accountManager.Services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import com.mpakbaz.accountManager.constants.Currencies;
import com.mpakbaz.accountManager.constants.TransactionType;
import com.mpakbaz.accountManager.exceptions.AccountBalanceException;
import com.mpakbaz.accountManager.exceptions.AccountNotFoundException;
import com.mpakbaz.accountManager.infrastructure.ExchangeService.ExchangeServiceProvider;
import com.mpakbaz.accountManager.infrastructure.database.models.Account;
import com.mpakbaz.accountManager.infrastructure.database.models.Transaction;
import com.mpakbaz.accountManager.infrastructure.database.models.TransactionDetail;
import com.mpakbaz.accountManager.infrastructure.database.repositories.AccountRepository;
import com.mpakbaz.accountManager.infrastructure.database.repositories.TransactionDetailRepository;
import com.mpakbaz.accountManager.infrastructure.database.repositories.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO this class should be transactional
@Service
public class TransactionService {

    private TransactionRepository transactionRepository;

    private TransactionDetailRepository transactionDetailRepository;

    private AccountRepository accountRepository;

    private ExchangeServiceProvider expchageServiceProvider;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository,
            TransactionDetailRepository transactionDetailRepository, AccountRepository accountRepository,
            ExchangeServiceProvider expchageServiceProvider) {
        this.transactionRepository = transactionRepository;
        this.transactionDetailRepository = transactionDetailRepository;
        this.accountRepository = accountRepository;
        this.expchageServiceProvider = expchageServiceProvider;
    }

    public Transaction depositTransaction(UUID accountId, BigDecimal amount) throws AccountNotFoundException {
        Optional<Account> accountOptional = this.accountRepository.findById(accountId);

        if (!accountOptional.isPresent()) {
            throw new AccountNotFoundException("from account with id " + accountId + " does not exist");
        }

        Account account = accountOptional.get();
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setCreatedAt(new Date(System.currentTimeMillis()));

        TransactionDetail transactionDetail = new TransactionDetail();
        transactionDetail.setTransaction(transaction);
        transactionDetail.setAccount(account);

        // use abs to ensure that amount is positive
        transactionDetail.setAmount(amount.abs());

        Transaction savedTransaction = this.transactionRepository.save(transaction);
        this.transactionDetailRepository.save(transactionDetail);

        return savedTransaction;

    }

    public Transaction withdrawTransaction(UUID accountId, BigDecimal amount) throws AccountNotFoundException {

        Optional<Account> accountOptional = this.accountRepository.findById(accountId);

        if (!accountOptional.isPresent()) {
            throw new AccountNotFoundException("from account with id " + accountId + " does not exist");
        }

        Account account = accountOptional.get();
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.WITHDRAWY);
        transaction.setCreatedAt(new Date(System.currentTimeMillis()));

        TransactionDetail transactionDetail = new TransactionDetail();
        transactionDetail.setTransaction(transaction);
        transactionDetail.setAccount(account);
        transactionDetail.setAmount(amount.negate());

        Transaction savedTransaction = this.transactionRepository.save(transaction);
        this.transactionDetailRepository.save(transactionDetail);

        return savedTransaction;

    }

    public Transaction transferTransaction(UUID fromAccountId, UUID toAccountId, BigDecimal amount)
            throws AccountBalanceException, AccountNotFoundException {

        Optional<Account> fromAccountOptional = this.accountRepository.findById(fromAccountId);

        if (!fromAccountOptional.isPresent()) {
            throw new AccountNotFoundException("from account with id " + fromAccountId + " does not exist");
        }

        Optional<Account> toAccountOptional = this.accountRepository.findById(toAccountId);

        if (!toAccountOptional.isPresent()) {
            throw new AccountNotFoundException("from account with id " + toAccountId + " does not exist");
        }

        Account fromAccount = fromAccountOptional.get();
        Account toAccount = toAccountOptional.get();

        BigDecimal fromAccountBalanace = this.transactionDetailRepository.accountBalance(fromAccount.getId());

        if (fromAccountBalanace.compareTo(amount) < 0) {
            throw new AccountBalanceException("account balance is less than " + amount);
        }

        BigDecimal fromAmount = amount.negate();
        BigDecimal toAmount = amount.abs();

        if (!fromAccount.getCurrency().equals(toAccount.getCurrency())) {

            double rate = 1;
            if (fromAccount.getCurrency().equals(Currencies.USD)) {
                rate = this.expchageServiceProvider.getUSDEURRate();

            } else {
                rate = this.expchageServiceProvider.getEURUSDRate();
            }
            toAmount = toAmount.multiply(BigDecimal.valueOf(rate));
        }

        Transaction transaction = new Transaction();
        transaction.setAccount(fromAccount);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setCreatedAt(new Date(System.currentTimeMillis()));

        TransactionDetail fromAccountTransactionDetail = new TransactionDetail();
        fromAccountTransactionDetail.setTransaction(transaction);
        fromAccountTransactionDetail.setAccount(fromAccount);
        fromAccountTransactionDetail.setAmount(fromAmount);

        TransactionDetail toAccountTransactionDetail = new TransactionDetail();
        toAccountTransactionDetail.setTransaction(transaction);
        toAccountTransactionDetail.setAccount(toAccount);

        // use abs to ensure that amount is positive for toAccount
        toAccountTransactionDetail.setAmount(toAmount);

        Transaction savedTransaction = this.transactionRepository.save(transaction);
        this.transactionDetailRepository.save(fromAccountTransactionDetail);
        this.transactionDetailRepository.save(toAccountTransactionDetail);

        return savedTransaction;

    }

    public BigDecimal getAccountBalance(UUID accountId) {
        return this.transactionDetailRepository.accountBalance(accountId);
    }

}

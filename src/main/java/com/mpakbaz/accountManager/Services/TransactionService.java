package com.mpakbaz.accountManager.Services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.mpakbaz.accountManager.constants.Currencies;
import com.mpakbaz.accountManager.constants.TransactionType;
import com.mpakbaz.accountManager.exceptions.AccountBalanceException;
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

    public Transaction depositTransaction(UUID accountId, BigDecimal amount) {

        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setCreatedAt(new Date(System.currentTimeMillis()));

        TransactionDetail transactionDetail = new TransactionDetail();
        transactionDetail.setTransactionId(transaction.getId());
        transactionDetail.setAccountId(accountId);

        // use abs to ensure that amount is positive
        transactionDetail.setAmount(amount.abs());

        Transaction savedTransaction = this.transactionRepository.save(transaction);
        this.transactionDetailRepository.save(transactionDetail);

        return savedTransaction;

    }

    public Transaction withdrawTransaction(UUID accountId, BigDecimal amount) {

        Transaction transaction = new Transaction();
        transaction.setAccountId(accountId);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.WITHDRAWY);
        transaction.setCreatedAt(new Date(System.currentTimeMillis()));

        TransactionDetail transactionDetail = new TransactionDetail();
        transactionDetail.setTransactionId(transaction.getId());
        transactionDetail.setAccountId(accountId);
        transactionDetail.setAmount(amount.negate());

        Transaction savedTransaction = this.transactionRepository.save(transaction);
        this.transactionDetailRepository.save(transactionDetail);

        return savedTransaction;

    }

    public Transaction transferTransaction(UUID fromAccountId, UUID toAccountId, BigDecimal amount)
            throws AccountBalanceException {

        BigDecimal fromAccountBalanace = this.transactionDetailRepository.accountBalance(fromAccountId);

        if (fromAccountBalanace.compareTo(amount) < 0) {
            throw new AccountBalanceException("account balance is less than " + amount);
        }

        Account fromAccount = this.accountRepository.getById(fromAccountId);
        Account toAccount = this.accountRepository.getById(toAccountId);

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
        transaction.setAccountId(fromAccountId);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.TRANSFER);
        transaction.setCreatedAt(new Date(System.currentTimeMillis()));

        TransactionDetail fromAccountTransactionDetail = new TransactionDetail();
        fromAccountTransactionDetail.setTransactionId(transaction.getId());
        fromAccountTransactionDetail.setAccountId(fromAccountId);
        fromAccountTransactionDetail.setAmount(fromAmount);

        TransactionDetail toAccountTransactionDetail = new TransactionDetail();
        toAccountTransactionDetail.setTransactionId(transaction.getId());
        toAccountTransactionDetail.setAccountId(toAccountId);

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

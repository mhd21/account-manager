package com.mpakbaz.accountManager.unit.services.transactionService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.mpakbaz.accountManager.Services.TransactionService;
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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class TransferTransactionTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TransactionDetailRepository transactionDetailRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ExchangeServiceProvider expchageServiceProvider;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    public void transfer_will_throw_account_balance_exception_if_from_account_balance_is_less_than_transaction_amount() {

        BigDecimal amount = BigDecimal.valueOf(20);
        Account fromAccount = new Account();
        fromAccount.setCurrency(Currencies.USD);

        Account toAccount = new Account();
        toAccount.setCurrency(Currencies.EUR);

        when(transactionDetailRepository.accountBalance(fromAccount.getId())).thenReturn(BigDecimal.valueOf(20));
        Optional<Account> fromAccountOptional = Optional.of(fromAccount);
        when(accountRepository.findById(fromAccount.getId())).thenReturn(fromAccountOptional);
        Optional<Account> toAccountOptional = Optional.of(toAccount);
        when(accountRepository.findById(toAccount.getId())).thenReturn(toAccountOptional);

        when(transactionDetailRepository.accountBalance(fromAccount.getId())).thenReturn(BigDecimal.valueOf(10));

        assertThrows(AccountBalanceException.class,
                () -> transactionService.transferTransaction(fromAccount.getId(), toAccount.getId(), amount));

    }

    @Test
    public void transfer_will_call_getUSDEURRate_of_exchange_rate_provider_if_from_account_and_to_account_currencies_are_different_and_from_account_currency_is_USD()
            throws AccountBalanceException, AccountNotFoundException {
        BigDecimal amount = BigDecimal.valueOf(20);

        Account fromAccount = new Account();
        fromAccount.setCurrency(Currencies.USD);

        Account toAccount = new Account();
        toAccount.setCurrency(Currencies.EUR);

        when(transactionDetailRepository.accountBalance(fromAccount.getId())).thenReturn(BigDecimal.valueOf(20));
        Optional<Account> fromAccountOptional = Optional.of(fromAccount);
        when(accountRepository.findById(fromAccount.getId())).thenReturn(fromAccountOptional);
        Optional<Account> toAccountOptional = Optional.of(toAccount);
        when(accountRepository.findById(toAccount.getId())).thenReturn(toAccountOptional);

        when(expchageServiceProvider.getUSDEURRate()).thenReturn(0.8);

        transactionService.transferTransaction(fromAccount.getId(), toAccount.getId(), amount);

        verify(expchageServiceProvider, times(1)).getUSDEURRate();
    }

    @Test
    public void transfer_will_call_getEURUSDRate_of_exchange_rate_provider_if_from_account_and_to_account_currencies_are_different_and_from_account_currency_is_EUR()
            throws AccountBalanceException, AccountNotFoundException {
        BigDecimal amount = BigDecimal.valueOf(20);

        Account fromAccount = new Account();
        fromAccount.setCurrency(Currencies.EUR);

        Account toAccount = new Account();
        toAccount.setCurrency(Currencies.USD);

        when(transactionDetailRepository.accountBalance(fromAccount.getId())).thenReturn(BigDecimal.valueOf(20));
        Optional<Account> fromAccountOptional = Optional.of(fromAccount);
        when(accountRepository.findById(fromAccount.getId())).thenReturn(fromAccountOptional);
        Optional<Account> toAccountOptional = Optional.of(toAccount);
        when(accountRepository.findById(toAccount.getId())).thenReturn(toAccountOptional);

        when(expchageServiceProvider.getEURUSDRate()).thenReturn(1.7);

        transactionService.transferTransaction(fromAccount.getId(), toAccount.getId(), amount);

        verify(expchageServiceProvider, times(1)).getEURUSDRate();
    }

    @Test
    public void transfer_set_transaction_type_to_TRANSFER() throws AccountBalanceException, AccountNotFoundException {
        BigDecimal amount = BigDecimal.valueOf(20);

        Account fromAccount = new Account();
        fromAccount.setCurrency(Currencies.USD);

        Account toAccount = new Account();
        toAccount.setCurrency(Currencies.USD);

        when(transactionDetailRepository.accountBalance(fromAccount.getId())).thenReturn(BigDecimal.valueOf(20));
        Optional<Account> fromAccountOptional = Optional.of(fromAccount);
        when(accountRepository.findById(fromAccount.getId())).thenReturn(fromAccountOptional);
        Optional<Account> toAccountOptional = Optional.of(toAccount);
        when(accountRepository.findById(toAccount.getId())).thenReturn(toAccountOptional);

        ArgumentCaptor<Transaction> argument = ArgumentCaptor.forClass(Transaction.class);
        transactionService.transferTransaction(fromAccount.getId(), toAccount.getId(), amount);

        verify(transactionRepository).save(argument.capture());
        assertEquals(TransactionType.TRANSFER, argument.getValue().getType());
    }

    @Test
    public void transfer_saves_two_transaction_detail_instances_which_are_related_to_two_accounts()
            throws AccountBalanceException, AccountNotFoundException {
        BigDecimal amount = BigDecimal.valueOf(20);

        Account fromAccount = new Account();
        fromAccount.setCurrency(Currencies.USD);

        Account toAccount = new Account();
        toAccount.setCurrency(Currencies.USD);

        when(transactionDetailRepository.accountBalance(fromAccount.getId())).thenReturn(BigDecimal.valueOf(20));
        Optional<Account> fromAccountOptional = Optional.of(fromAccount);
        when(accountRepository.findById(fromAccount.getId())).thenReturn(fromAccountOptional);
        Optional<Account> toAccountOptional = Optional.of(toAccount);
        when(accountRepository.findById(toAccount.getId())).thenReturn(toAccountOptional);

        ArgumentCaptor<TransactionDetail> argument = ArgumentCaptor.forClass(TransactionDetail.class);
        transactionService.transferTransaction(fromAccount.getId(), toAccount.getId(), amount);

        verify(transactionDetailRepository, times(2)).save(argument.capture());
        List<TransactionDetail> capturedTransactionDetails = argument.getAllValues();

        assertEquals(fromAccount.getId().toString(), capturedTransactionDetails.get(0).getAccount().getId().toString());
        assertEquals(toAccount.getId().toString(), capturedTransactionDetails.get(1).getAccount().getId().toString());
    }

    @Test
    public void from_account_transaction_detail_amount_must_be_negative_and_to_account_transaction_detail_must_be_positive()
            throws AccountBalanceException, AccountNotFoundException {
        BigDecimal amount = BigDecimal.valueOf(20);

        Account fromAccount = new Account();
        fromAccount.setCurrency(Currencies.USD);

        Account toAccount = new Account();
        toAccount.setCurrency(Currencies.USD);

        when(transactionDetailRepository.accountBalance(fromAccount.getId())).thenReturn(BigDecimal.valueOf(20));
        Optional<Account> fromAccountOptional = Optional.of(fromAccount);
        when(accountRepository.findById(fromAccount.getId())).thenReturn(fromAccountOptional);
        Optional<Account> toAccountOptional = Optional.of(toAccount);
        when(accountRepository.findById(toAccount.getId())).thenReturn(toAccountOptional);
        when(accountRepository.findById(toAccount.getId())).thenReturn(toAccountOptional);

        ArgumentCaptor<TransactionDetail> argument = ArgumentCaptor.forClass(TransactionDetail.class);
        transactionService.transferTransaction(fromAccount.getId(), toAccount.getId(), amount);

        verify(transactionDetailRepository, times(2)).save(argument.capture());
        List<TransactionDetail> capturedTransactionDetails = argument.getAllValues();

        // from account
        assertTrue(capturedTransactionDetails.get(0).getAmount().equals(amount.negate()));
        // to account
        assertTrue(capturedTransactionDetails.get(1).getAmount().equals(amount.abs()));
    }

    @Test
    public void transfer_will_apply_exchange_rate_on_to_account_transaction_detail_amount_if_from_account_and_to_account_currencies_are_different()
            throws AccountBalanceException, AccountNotFoundException {
        BigDecimal amount = BigDecimal.valueOf(20);

        Account fromAccount = new Account();
        fromAccount.setCurrency(Currencies.USD);

        Account toAccount = new Account();
        toAccount.setCurrency(Currencies.EUR);

        when(transactionDetailRepository.accountBalance(fromAccount.getId())).thenReturn(BigDecimal.valueOf(20));
        Optional<Account> fromAccountOptional = Optional.of(fromAccount);
        when(accountRepository.findById(fromAccount.getId())).thenReturn(fromAccountOptional);
        Optional<Account> toAccountOptional = Optional.of(toAccount);
        when(accountRepository.findById(toAccount.getId())).thenReturn(toAccountOptional);
        when(accountRepository.findById(toAccount.getId())).thenReturn(toAccountOptional);

        double exchangeRate = 0.8;
        when(expchageServiceProvider.getUSDEURRate()).thenReturn(exchangeRate);

        ArgumentCaptor<TransactionDetail> argument = ArgumentCaptor.forClass(TransactionDetail.class);
        transactionService.transferTransaction(fromAccount.getId(), toAccount.getId(), amount);

        verify(transactionDetailRepository, times(2)).save(argument.capture());
        List<TransactionDetail> capturedTransactionDetails = argument.getAllValues();

        // from account
        assertTrue(capturedTransactionDetails.get(0).getAmount().equals(amount.negate()));
        // to account
        assertTrue(capturedTransactionDetails.get(1).getAmount()
                .equals(amount.multiply(BigDecimal.valueOf(exchangeRate)).abs()));
    }
}

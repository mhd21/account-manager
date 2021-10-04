package com.mpakbaz.accountManager.unit.controllers.transactionController;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import com.mpakbaz.accountManager.Services.TransactionService;
import com.mpakbaz.accountManager.http.controllers.TransactionController;
import com.mpakbaz.accountManager.infrastructure.database.models.Account;
import com.mpakbaz.accountManager.infrastructure.database.models.Transaction;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class TransferTransactionTest {

    private MockMvc mockMvc;

    private AutoCloseable closeable;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    public void create_customer_will_return_200_status_code_and_created_transaction_data_if_everythin_is_ok()
            throws Exception {

        Transaction transaction = new Transaction();
        transaction.setAccount(new Account());
        transaction.setAmount(BigDecimal.valueOf(10));
        transaction.setCreatedAt(new Date(System.currentTimeMillis()));

        when(transactionService.transferTransaction(isA(UUID.class), isA(UUID.class), isA(BigDecimal.class)))
                .thenReturn(transaction);

        this.mockMvc
                .perform(post("/transaction/transfer").contentType(APPLICATION_JSON)
                        .content(String.format("{\"fromAccountId\":\"%s\",\"toAccountId\":\"%s\", \"amount\": \"%s\"}",
                                UUID.randomUUID().toString(), UUID.randomUUID().toString(), 10)))
                .andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("id", is(transaction.getId().toString())))
                .andExpect(jsonPath("type", is(transaction.getType())))
                .andExpect(jsonPath("accountId", is(transaction.getAccount().getId().toString())));

    }
}

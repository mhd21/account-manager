package com.mpakbaz.accountManager.unit.controllers.accountController;

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

import com.mpakbaz.accountManager.Services.AccountService;
import com.mpakbaz.accountManager.constants.Currencies;
import com.mpakbaz.accountManager.http.controllers.AccountController;
import com.mpakbaz.accountManager.infrastructure.database.models.Account;
import com.mpakbaz.accountManager.infrastructure.database.models.Customer;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CreateAccountTest {

        private MockMvc mockMvc;

        private AutoCloseable closeable;

        @Mock
        private AccountService accountService;

        @InjectMocks
        private AccountController accountController;

        @BeforeEach
        public void setup() {
                closeable = MockitoAnnotations.openMocks(this);
                this.mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
        }

        @AfterEach
        void closeService() throws Exception {
                closeable.close();
        }

        @Test
        public void create_account_will_return_200_status_code_and_created_account_data_if_everythin_is_ok()
                        throws Exception {

                Customer customer = new Customer();

                Account account = new Account();
                account.setCustomerId(customer.getId());
                account.setCurrency(Currencies.USD);

                when(accountService.createAccount(isA(Account.class), isA(BigDecimal.class))).thenReturn(account);

                this.mockMvc.perform(post("/account/create").contentType(APPLICATION_JSON)
                                .content(String.format("{\"customerId\":\"%s\",\"currency\": \"%s\"}",
                                                customer.getId().toString(), Currencies.USD)))
                                .andDo(print()).andExpect(status().isOk())
                                .andExpect(jsonPath("id", is(account.getId().toString())))
                                .andExpect(jsonPath("customerId", is(customer.getId().toString())))
                                .andExpect(jsonPath("currency", is(account.getCurrency())));

        }

        @Test
        public void create_account_will_return_400_status_code_if_customer_id_is_not_in_the_request_body()
                        throws Exception {

                Customer customer = new Customer();

                Account account = new Account();
                account.setCustomerId(customer.getId());
                account.setCurrency(Currencies.USD);

                when(accountService.createAccount(isA(Account.class), isA(BigDecimal.class))).thenReturn(account);

                this.mockMvc.perform(post("/account/create").contentType(APPLICATION_JSON)
                                .content(String.format("{\"currency\": \"%s\"}", Currencies.USD))).andDo(print())
                                .andExpect(status().isBadRequest());

        }

        @Test
        public void create_account_will_return_400_status_code_if_customer_id_is_not_valid() throws Exception {

                Customer customer = new Customer();

                Account account = new Account();
                account.setCustomerId(customer.getId());
                account.setCurrency(Currencies.USD);

                when(accountService.createAccount(isA(Account.class), isA(BigDecimal.class))).thenReturn(account);

                this.mockMvc.perform(
                                post("/account/create").contentType(APPLICATION_JSON)
                                                .content(String.format("{\"customerId\":\"%s\",\"currency\": \"%s\"}",
                                                                "id-uuid-test", Currencies.USD)))
                                .andDo(print()).andExpect(status().isBadRequest());

        }
}

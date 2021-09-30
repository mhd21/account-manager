package com.mpakbaz.accountManager.unit.controllers.customerController;

import com.mpakbaz.accountManager.Services.CustomerService;
import com.mpakbaz.accountManager.http.controllers.CustomerController;
import com.mpakbaz.accountManager.infrastructure.database.models.Customer;

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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CreateCustomerTest {

    private MockMvc mockMvc;

    private AutoCloseable closeable;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    public void setup() {
        closeable = MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    public void create_customer_will_return_200_status_code_and_created_customer_data_if_everythin_is_ok()
            throws Exception {
        Customer customer = new Customer();
        customer.setName("Mehdi");

        when(customerService.createCustomer(isA(Customer.class))).thenReturn(customer);

        this.mockMvc.perform(post("/customer/create").contentType(APPLICATION_JSON).content("{\"name\":\"Mehdi\"}"))
                .andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("id", is(customer.getId().toString())))
                .andExpect(jsonPath("name", is(customer.getName())));

    }

    // TODO it needs to check error message in test. it's different from successful
    // request
    @Test
    public void create_customer_will_return_400_status_code_if_name_is_not_in_the_request_body() throws Exception {

        this.mockMvc.perform(post("/customer/create").contentType(APPLICATION_JSON).content("{}")).andDo(print())
                .andExpect(status().isBadRequest());

    }

    // TODO it needs to check error message in test. it's different from successful
    // request
    @Test
    public void create_customer_will_return_400_status_code_if_name_length_is_less_than_4() throws Exception {

        this.mockMvc.perform(post("/customer/create").contentType(APPLICATION_JSON).content("{\"name\":\"Meh\"}"))
                .andDo(print()).andExpect(status().isBadRequest());

    }
}

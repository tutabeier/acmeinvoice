package controller;

import acmeinvoice.controller.CustomerController;
import acmeinvoice.model.Customer;
import acmeinvoice.repository.CustomerRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static acmeinvoice.common.JsonUtil.toJson;
import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class CustomerControllerTest {
    private MockMvc mockMvc;
    @Mock
    private CustomerRepository repository;
    @InjectMocks
    private CustomerController controller;
    private Customer customerOne;
    private Customer customerTwo;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(controller).build();
        setCustomers();
    }

    @Test
    public void shouldSaveCustomer() throws Exception {
        when(repository.save(customerOne)).thenReturn(customerOne);

        mockMvc.perform(post("/v1.0/customers").content(toJson(customerOne)).contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name", is("Person One")));
    }

    @Test
    public void shouldFindAllCustomers() throws Exception {
        when(repository.findAll()).thenReturn(newArrayList(customerOne, customerTwo));

        mockMvc.perform(get("/v1.0/customers")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is("Person One")))
                .andExpect(jsonPath("$[1].name", is("Person Two")));
    }

    private void setCustomers() {
        customerOne = Customer.builder().name("Person One").build();
        customerTwo = Customer.builder().name("Person Two").build();
    }
}
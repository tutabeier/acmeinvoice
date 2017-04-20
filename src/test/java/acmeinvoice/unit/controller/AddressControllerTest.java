package acmeinvoice.unit.controller;

import acmeinvoice.controller.AddressController;
import acmeinvoice.controller.InvoiceController;
import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import acmeinvoice.model.InvoiceResponse;
import acmeinvoice.repository.AddressRepository;
import acmeinvoice.service.InvoiceService;
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
import static acmeinvoice.model.Address.builder;
import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
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
public class AddressControllerTest {
    private MockMvc mockMvc;
    @Mock
    private AddressRepository repository;
    @InjectMocks
    private AddressController controller;
    private Address addressOne;
    private Address addressTwo;
    private Customer customer;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(controller).build();
        setAddressesResponses();
    }

    @Test
    public void shouldSaveAddress() throws Exception {
        when(repository.save(addressOne)).thenReturn(addressOne);
        mockMvc.perform(post("/v1.0/address").content(toJson(addressOne)).contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("city", is("Porto Alegre")));
    }

    @Test
    public void shouldFindAllAddresses() throws Exception {
        when(repository.findAll()).thenReturn(newArrayList(addressOne, addressTwo));

        mockMvc.perform(get("/v1.0/address")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].city", is("Porto Alegre")))
                .andExpect(jsonPath("$[0].state", is("Rio Grande do Sul")))
                .andExpect(jsonPath("$[0].country", is("Brazil")))
                .andExpect(jsonPath("$[1].city", is("São Paulo")))
                .andExpect(jsonPath("$[1].state", is("São Paulo")))
                .andExpect(jsonPath("$[1].country", is("Brazil")));
    }

    private void setAddressesResponses() {
        customer = Customer.builder().name("Lucas Falk Beier").build();

        addressOne = builder()
                .city("Porto Alegre")
                .state("Rio Grande do Sul")
                .country("Brazil")
                .customer(customer)
                .build();

        addressTwo = builder()
                .city("São Paulo")
                .state("São Paulo")
                .country("Brazil")
                .customer(customer)
                .build();
    }
}
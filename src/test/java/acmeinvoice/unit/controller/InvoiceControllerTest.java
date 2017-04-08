package acmeinvoice.unit.controller;

import acmeinvoice.controller.InvoiceController;
import acmeinvoice.model.InvoiceResponse;
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
import org.springframework.test.web.servlet.ResultMatcher;

import static com.google.common.collect.Lists.newArrayList;
import static java.time.LocalDate.parse;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class InvoiceControllerTest {

    private MockMvc mockMvc;
    @Mock
    private InvoiceService service;
    @InjectMocks
    private InvoiceController controller;
    private InvoiceResponse invoiceResponseOne;
    private InvoiceResponse invoiceResponseTwo;

    @Before
    public void setUp() {
        mockMvc = standaloneSetup(controller).build();
        setInvoiceResponses();
    }

    @Test
    public void shouldFindAllInvoicesByCustomer() throws Exception {
        when(service.findBy(01L)).thenReturn(newArrayList(invoiceResponseOne, invoiceResponseTwo));

        mockMvc.perform(get("/v1.0/invoices/?customerId=01")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId", is("01")))
                .andExpect(jsonPath("$[0].addressId", is("02")))
                .andExpect(jsonPath("$[0].invoiceId", is("03")))
                .andExpect(jsonPath("$[0].invoiceType", is("AdvancePayment")))
                .andExpect(jsonPath("$[0].invoiceTypeLocalized", is("Voorschot")))
                .andExpect(jsonPath("$[0].invoiceNumber", is("157005888")))
                .andExpect(jsonPath("$[0].periodDescription", is("March 2015")))
                .andExpect(jsonPath("$[0].totalAmount", is(200.0)))
                .andExpect(jsonPath("$[1].customerId", is("01")))
                .andExpect(jsonPath("$[1].addressId", is("02")))
                .andExpect(jsonPath("$[1].invoiceId", is("04")))
                .andExpect(jsonPath("$[1].invoiceType", is("ShopPurchase")))
                .andExpect(jsonPath("$[1].invoiceTypeLocalized", is("Winkel aankoop")))
                .andExpect(jsonPath("$[1].invoiceNumber", is("1429564")))
                .andExpect(jsonPath("$[1].periodDescription", is("December 2014")))
                .andExpect(jsonPath("$[1].totalAmount", is(200.0)));
    }

    private void setInvoiceResponses() {
        invoiceResponseOne = new InvoiceResponse("01", "02", "03",
                "AdvancePayment", "Voorschot", parse("2015-02-13"), parse("2015-02-20"),
                "157005888", parse("2015-03-01"), parse("2015-04-01"),  165.29F, 34.71F);
        invoiceResponseTwo = new InvoiceResponse("01", "02", "04",
                "ShopPurchase", "Winkel aankoop", parse("2015-02-13"), parse("2015-02-20"),
                "1429564", parse("2014-12-01"), parse("2015-01-01"),  165.29F, 34.71F);
    }

    private ResultMatcher contains(String string) {
        return content().string(containsString(string));
    }
}
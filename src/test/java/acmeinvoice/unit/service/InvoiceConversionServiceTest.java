package service;

import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import acmeinvoice.model.Invoice;
import acmeinvoice.model.InvoiceResponse;
import acmeinvoice.service.InvoiceConversionService;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import static acmeinvoice.service.InvoiceConversionService.convert;
import static java.time.LocalDate.parse;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;

public class InvoiceConversionServiceTest {
    private Invoice invoice;
    private InvoiceResponse invoiceResponse;
    private Customer customer;
    private Address address;

    @Before
    public void setUp() throws Exception {
        customer = Customer.builder().id(01L).name("Customer One").build();

        address = Address.builder()
                .id(02L)
                .city("Porto Alegre")
                .state("Rio Grande do Sul")
                .country("Brazil")
                .customer(customer)
                .build();

        invoice = new Invoice(03L, customer, address, "AdvancePayment", "Voorschot", parse("2015-02-13"), parse("2015-02-20"), "010101",
                parse("2015-03-01"), parse("2015-04-01"), 165.29f,34.71f);

        invoiceResponse = new InvoiceResponse(customer.getId(), address.getId(), 03L, "AdvancePayment",
                "Voorschot", parse("2015-02-13"), parse("2015-02-20"), "010101",
                parse("2015-03-01"), parse("2015-04-01"), 165.29f,34.71f);
    }

    @Test
    public void shouldConvertFromInvoiceResponseToInvoice() {
        Invoice invoiceConverted = convert(invoiceResponse);
        invoiceConverted.setCustomer(customer);
        invoiceConverted.setAddress(address);
        invoiceConverted.setId(03L);

        assertThat(invoiceConverted, equalTo(invoice));
    }

    @Test
    public void shouldConvertFromInvoiceToInvoiceResponse() throws Exception {
        InvoiceResponse invoiceResponseConverted = convert(invoice);

        assertThat(invoiceResponseConverted, equalTo(invoiceResponse));
    }
}
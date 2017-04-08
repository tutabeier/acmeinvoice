package acmeinvoice.unit.service;

import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import acmeinvoice.model.Invoice;
import acmeinvoice.model.InvoiceResponse;
import acmeinvoice.repository.AddressRepository;
import acmeinvoice.repository.CustomerRepository;
import acmeinvoice.repository.InvoiceRepository;
import acmeinvoice.service.InvoiceConversionService;
import acmeinvoice.service.InvoiceService;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static acmeinvoice.service.InvoiceConversionService.convert;
import static java.time.LocalDate.parse;
import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class InvoiceServiceTest {
    @Mock
    private InvoiceRepository repository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AddressRepository addressRepository;
    private InvoiceService service;
    private Invoice invoiceOne;
    private Invoice invoiceTwo;
    private Customer customer;
    private Address addressOne;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        service = new InvoiceService(repository, customerRepository, addressRepository);

        customer = new Customer();
        customer.setId(01L);
        customer.setName("Lucas Falk Beier");
        addressOne = new Address();
        addressOne.setId(01L);
        addressOne.setCity("Porto Alegre");
        addressOne.setState("Rio Grande do Sul");
        addressOne.setCountry("Brazil");
        addressOne.setCustomer(customer);

        invoiceOne = new Invoice("AdvancePayment", "Voorschot", parse("2015-02-13"), parse("2015-02-20"),
                "157005888", parse("2015-03-01"), parse("2015-04-01"),  165.29F, 34.71F);
        invoiceOne.setId(01L);
        invoiceOne.setCustomer(customer);
        invoiceOne.setAddress(addressOne);

        invoiceTwo = new Invoice("ShopPurchase", "Voorschot", parse("2015-02-13"), parse("2015-02-20"),
                "157005888", parse("2015-03-01"), parse("2015-04-01"),  165.29F, 34.71F);
        invoiceTwo.setId(02L);
        invoiceTwo.setCustomer(customer);
        invoiceTwo.setAddress(addressOne);
    }

    @Test
    public void shouldFindByCustomerIdAndMonth() throws Exception {
        // Invoices per month
        when(repository.findByCustomerIdAndMonth(01L, "MARCH")).thenReturn(newArrayList(invoiceTwo));

        List<InvoiceResponse> responses = service.findBy(01L, null, null, 03);
        InvoiceResponse expectedInvoice = convert(invoiceTwo);

        verify(repository).findByCustomerIdAndMonth(01L, "MARCH");
        assertThat(responses, containsInAnyOrder(expectedInvoice));
    }

    @Test
    public void shouldFindByCustomerIdAndInvoiceTypeAndMonth() throws Exception {
        // Shop Invoices
        when(repository.findByCustomerIdAndInvoiceTypeAndMonth(01L, "ShopPurchase", "MARCH")).thenReturn(newArrayList(invoiceTwo));

        List<InvoiceResponse> responses = service.findBy(01L, null, "shop", 03);
        InvoiceResponse expectedInvoice = convert(invoiceTwo);

        verify(repository).findByCustomerIdAndInvoiceTypeAndMonth(01L, "ShopPurchase", "MARCH");
        assertThat(responses, containsInAnyOrder(expectedInvoice));
    }

    @Test
    public void shouldFindByCustomerId() throws Exception {
        // Invoices history full
        service.findBy(01L, null, null, null);
        verify(repository).findByCustomerId(01L);
    }

    @Test
    public void shouldFindByCustomerIdAndAddressId() throws Exception {
        // Invoices history per address
        service.findBy(01L, 0L, null, null);
        verify(repository).findByCustomerIdAndAddressId(01, 0);
    }
}
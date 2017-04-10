package acmeinvoice.unit.service;

import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import acmeinvoice.model.Invoice;
import acmeinvoice.model.InvoiceResponse;
import acmeinvoice.repository.AddressRepository;
import acmeinvoice.repository.CustomerRepository;
import acmeinvoice.repository.InvoiceRepository;
import acmeinvoice.repository.InvoiceRepositoryImpl;
import acmeinvoice.service.InvoiceService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;

import static acmeinvoice.model.Address.builder;
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
        addressOne = builder()
                .id(01L)
                .city("Porto Alegre")
                .state("Rio Grande do Sul")
                .country("Brazil")
                .customer(customer)
                .build();

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
        when(repository.findBy(01L, null, null, 03)).thenReturn(newArrayList(invoiceTwo));

        List<InvoiceResponse> responses = service.findBy(01L, null, null, 03);
        InvoiceResponse expectedInvoice = convert(invoiceTwo);

        verify(repository).findBy(01L, null, null, 03);
        assertThat(responses, containsInAnyOrder(expectedInvoice));
    }

    @Test
    public void shouldFindByCustomerIdAndInvoiceTypeAndMonth() throws Exception {
        when(repository.findBy(01L, null, "shop", 03)).thenReturn(newArrayList(invoiceTwo));

        List<InvoiceResponse> responses = service.findBy(01L, null, "shop", 03);
        InvoiceResponse expectedInvoice = convert(invoiceTwo);

        verify(repository).findBy(01L, null, "shop", 03);
        assertThat(responses, containsInAnyOrder(expectedInvoice));
    }

    @Test
    public void shouldFindByCustomerId() throws Exception {
        when(repository.findBy(01L, null, null, null)).thenReturn(newArrayList(invoiceOne));

        List<InvoiceResponse> responses = service.findBy(01L, null, null, null);
        InvoiceResponse expectedInvoice = convert(invoiceOne);

        verify(repository).findBy(01L, null, null, null);
        assertThat(responses, containsInAnyOrder(expectedInvoice));
    }

    @Test
    public void shouldFindByCustomerIdAndAddressId() throws Exception {
        when(repository.findBy(01L, 01L, null, null)).thenReturn(newArrayList(invoiceOne));

        List<InvoiceResponse> responses = service.findBy(01L, 01L, null, null);
        InvoiceResponse expectedInvoice = convert(invoiceOne);

        verify(repository).findBy(01L, 01L, null, null);
        assertThat(responses, containsInAnyOrder(expectedInvoice));
    }
}
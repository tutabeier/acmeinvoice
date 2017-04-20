package acmeinvoice.unit.service;

import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import acmeinvoice.model.Invoice;
import acmeinvoice.model.InvoiceResponse;
import acmeinvoice.repository.AddressRepository;
import acmeinvoice.repository.CustomerRepository;
import acmeinvoice.repository.InvoiceRepository;
import acmeinvoice.service.InvoiceService;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Example;

import java.util.List;
import java.util.Map;

import static acmeinvoice.model.Address.builder;
import static acmeinvoice.service.InvoiceConversionService.convert;
import static java.time.LocalDate.parse;
import static java.time.Month.of;
import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
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
    private ArgumentCaptor<Example> argument;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        argument = ArgumentCaptor.forClass(Example.class);
        service = new InvoiceService(repository, customerRepository, addressRepository);

        customer = Customer.builder()
                .id(01L)
                .name("Lucas Falk Beier")
                .build();
        addressOne = builder()
                .id(01L)
                .city("Porto Alegre")
                .state("Rio Grande do Sul")
                .country("Brazil")
                .customer(customer)
                .build();

        invoiceOne = new Invoice(01L, customer, addressOne, "AdvancePayment", "Voorschot", parse("2015-02-13"), parse("2015-02-20"),
                "157005888", parse("2015-03-01"), parse("2015-04-01"),  165.29F, 34.71F);

        invoiceTwo = new Invoice(02L, customer, addressOne, "ShopPurchase", "Voorschot", parse("2015-02-13"), parse("2015-02-20"),
                "157005888", parse("2015-03-01"), parse("2015-04-01"),  165.29F, 34.71F);
    }

    @Test
    public void shouldFindByCustomerIdAndMonth() throws Exception {
        when(repository.findAll(org.mockito.Matchers.<Example>any())).thenReturn(newArrayList(invoiceTwo));

        List<InvoiceResponse> responses = service.findBy(01L, null, null, 03);
        InvoiceResponse expectedInvoice = convert(invoiceTwo);

        argument = ArgumentCaptor.forClass(Example.class);
        Invoice invoiceMatcher = createInvoiceMatcher(01L, null, null, 03);

        verify(repository).findAll(argument.capture());
        assertThat(invoiceMatcher, is(argument.getValue().getProbe()));
        assertThat(responses, containsInAnyOrder(expectedInvoice));
    }

    @Test
    public void shouldFindByCustomerIdAndInvoiceTypeAndMonth() throws Exception {
        when(repository.findAll(org.mockito.Matchers.<Example>any())).thenReturn(newArrayList(invoiceTwo));

        List<InvoiceResponse> responses = service.findBy(01L, null, "shop", 03);
        InvoiceResponse expectedInvoice = convert(invoiceTwo);

        Invoice invoiceMatcher = createInvoiceMatcher(01L, null, "shop", 03);

        verify(repository).findAll(argument.capture());
        assertThat(invoiceMatcher, is(argument.getValue().getProbe()));
        assertThat(responses, containsInAnyOrder(expectedInvoice));
    }


    @Test
    public void shouldFindByCustomerId() throws Exception {
        when(repository.findAll(org.mockito.Matchers.<Example>any())).thenReturn(newArrayList(invoiceOne));

        List<InvoiceResponse> responses = service.findBy(01L, null, null, null);
        InvoiceResponse expectedInvoice = convert(invoiceOne);

        Invoice invoiceMatcher = createInvoiceMatcher(01L, null, null, null);

        verify(repository).findAll(argument.capture());
        assertThat(invoiceMatcher, is(argument.getValue().getProbe()));
        assertThat(responses, containsInAnyOrder(expectedInvoice));
    }

    @Test
    public void shouldFindByCustomerIdAndAddressId() throws Exception {
        when(repository.findAll(org.mockito.Matchers.<Example>any())).thenReturn(newArrayList(invoiceOne));

        List<InvoiceResponse> responses = service.findBy(01L, 01L, null, null);
        InvoiceResponse expectedInvoice = convert(invoiceOne);

        Invoice invoiceMatcher = createInvoiceMatcher(01L, 01L, null, null);

        verify(repository).findAll(argument.capture());
        assertThat(invoiceMatcher, is(argument.getValue().getProbe()));
        assertThat(responses, containsInAnyOrder(expectedInvoice));
    }

    private Invoice createInvoiceMatcher(Long customerId, Long addressId, String invoiceType, Integer month) {
        Customer customer = Customer.builder().id(customerId).build();
        Address address = Address.builder().id(addressId).build();
        Map<String, String> invoiceTypes = ImmutableMap.of("shop", "ShopPurchase", "payment", "AdvancePayment");
        String type = invoiceTypes.get(invoiceType);
        String monthDescription = null;
        if (month != null) {
            monthDescription = of(month).name().toLowerCase();
        }

        Invoice invoiceMatcher = Invoice.builder()
                .customer(customer)
                .address(address)
                .periodDescription(monthDescription)
                .invoiceType(type)
                .build();
        invoiceMatcher.setCustomer(customer);
        invoiceMatcher.setAddress(address);
        return invoiceMatcher;
    }
}
package acmeinvoice.integration;

import acmeinvoice.Application;
import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import acmeinvoice.model.Invoice;
import acmeinvoice.repository.AddressRepository;
import acmeinvoice.repository.CustomerRepository;
import acmeinvoice.repository.InvoiceRepository;
import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static acmeinvoice.model.Address.builder;
import static java.time.LocalDate.parse;
import static java.time.Month.of;
import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

@TestPropertySource("/test.properties")
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = Application.class)
public class InvoiceRepositoryTest {

    @Autowired
    private InvoiceRepository repository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AddressRepository addressRepository;
    private Customer customerOne;
    private Customer customerTwo;
    private Address addressOne;
    private Address addressTwo;
    private Address addressThree;
    private Invoice invoiceOne;
    private Invoice invoiceTwo;
    private Invoice invoiceThree;

    @Before
    public void setUp() throws Exception {
        setupCustomerAndAddress();
        customerRepository.save(customerOne);
        customerRepository.save(customerTwo);
        addressRepository.save(addressOne);
        addressRepository.save(addressTwo);
        addressRepository.save(addressThree);

        invoiceOne = new Invoice("AdvancePayment", "Voorschot", parse("2015-02-13"), parse("2015-02-20"),
                "157005888", parse("2015-03-01"), parse("2015-04-01"),  165.29F, 34.71F);
        invoiceOne.setCustomer(customerOne);
        invoiceOne.setAddress(addressOne);

        invoiceTwo = new Invoice("ShopPurchase", "Voorschot", parse("2015-02-13"), parse("2015-02-20"),
                "157005888", parse("2015-03-01"), parse("2015-04-01"),  165.29F, 34.71F);
        invoiceTwo.setCustomer(customerOne);
        invoiceTwo.setAddress(addressTwo);

        invoiceThree = new Invoice("AdvancePayment", "Voorschot", parse("2015-02-13"), parse("2015-02-20"),
                "157005888", parse("2015-03-01"), parse("2015-04-01"),  165.29F, 34.71F);
        invoiceThree.setCustomer(customerTwo);
        invoiceThree.setAddress(addressThree);
    }

    @Test
    public void findByCustomerId() throws Exception {
        Invoice savedInvoiceOne = repository.save(invoiceOne);
        Invoice savedInvoiceTwo = repository.save(invoiceTwo);
        Example<Invoice> invoiceExample = createExampleInvoice(savedInvoiceOne.getCustomer().getId(), null, null, null);

        Iterable<Invoice> invoices = repository.findAll(invoiceExample);

        assertThat(invoices, containsInAnyOrder(savedInvoiceOne, savedInvoiceTwo));
    }

    @Test
    public void findByCustomerIdAndAddressId() throws Exception {
        Invoice savedInvoice = repository.save(invoiceOne);
        Example<Invoice> invoiceExample = createExampleInvoice(savedInvoice.getCustomer().getId(), savedInvoice.getAddress().getId(), null, null);

        Iterable<Invoice> invoices = repository.findAll(invoiceExample);

        assertThat(invoices, contains(savedInvoice));
    }

    @Test
    public void shouldFindByCustomerIdAndMonth() throws Exception {
        Invoice savedInvoiceOne = repository.save(invoiceOne);
        Invoice savedInvoiceTwo = repository.save(invoiceTwo);
        Example<Invoice> invoiceExample = createExampleInvoice(customerOne.getId(), null, null, 03);

        Iterable<Invoice> invoices = repository.findAll(invoiceExample);

        assertThat(invoices, containsInAnyOrder(savedInvoiceOne, savedInvoiceTwo));
    }

    @Test
    public void shouldFindByCustomerIdAndInvoiceTypeAndMonth() throws Exception {
        Invoice savedInvoiceTwo = repository.save(invoiceTwo);
        Example<Invoice> invoiceExample = createExampleInvoice(customerOne.getId(), null, "shop", 03);

        Iterable<Invoice> invoices = repository.findAll(invoiceExample);

        assertThat(invoices, contains(savedInvoiceTwo));
//        int size = Iterators.size(invoices);
//        assertThat(size, is(1));
    }

    private void setupCustomerAndAddress() {
        customerOne = Customer.builder().name("Person One").build();
        customerTwo = Customer.builder().name("Person Two").build();

        addressOne = builder()
                .city("Porto Alegre")
                .state("Rio Grande do Sul")
                .country("Brazil")
                .customer(customerOne)
                .build();

        addressTwo = builder()
                .city("São Paulo")
                .state("São Paulo")
                .country("Brazil")
                .customer(customerOne)
                .build();

        addressThree = builder()
                .city("São Paulo")
                .state("São Paulo")
                .country("Brazil")
                .customer(customerTwo)
                .build();
    }

    private Example<Invoice> createExampleInvoice(Long customerId, Long addressId, String invoiceType, Integer month) {
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
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("periodDescription", ExampleMatcher.GenericPropertyMatcher.of(ExampleMatcher.StringMatcher.CONTAINING).ignoreCase());
        return Example.of(invoiceMatcher, exampleMatcher);
    }


}
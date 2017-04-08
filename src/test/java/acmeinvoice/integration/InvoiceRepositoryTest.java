package acmeinvoice.integration;

import acmeinvoice.Application;
import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import acmeinvoice.model.Invoice;
import acmeinvoice.repository.AddressRepository;
import acmeinvoice.repository.CustomerRepository;
import acmeinvoice.repository.InvoiceRepository;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.time.LocalDate.parse;
import static org.assertj.core.util.Lists.newArrayList;
import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

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
    private Customer customer;
    private Address address;
    private Invoice invoice;

    @Before
    public void setUp() throws Exception {
        setupCustomerAndAddress();
        invoice = new Invoice("AdvancePayment", "Voorschot", parse("2015-02-13"), parse("2015-02-20"),
                "157005888", parse("2015-03-01"), parse("2015-04-01"),  165.29F, 34.71F);
        invoice.setCustomer(customer);
        invoice.setAddress(address);
        customerRepository.save(customer);
        addressRepository.save(address);
    }

    @Test
    public void findByCustomerId() throws Exception {
        Invoice savedInvoice = repository.save(invoice);
        List<Invoice> invoices = repository.findByCustomerIdAndAddressId(savedInvoice.getCustomer().getId(), savedInvoice.getAddress().getId());
        assertThat(invoices, contains(savedInvoice));
    }

    private void setupCustomerAndAddress() {
        customer = new Customer();
        customer.setName("Person One");
        address = new Address();
        address.setCity("Porto Alegre");
        address.setCountry("Brazil");
        address.setState("Rio Grande do Sul");
        address.setCustomer(customer);
    }

}
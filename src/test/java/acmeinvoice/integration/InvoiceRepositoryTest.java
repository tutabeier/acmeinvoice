package acmeinvoice.integration;

import acmeinvoice.Application;
import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import acmeinvoice.model.Invoice;
import acmeinvoice.repository.AddressRepository;
import acmeinvoice.repository.CustomerRepository;
import acmeinvoice.repository.InvoiceRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.time.LocalDate.parse;
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
        List<Invoice> invoices = repository.findByCustomerId(savedInvoiceOne.getCustomer().getId());
        assertThat(invoices, containsInAnyOrder(savedInvoiceOne, savedInvoiceTwo));
    }

    @Test
    public void findByCustomerIdAndAddressId() throws Exception {
        Invoice savedInvoice = repository.save(invoiceOne);
        List<Invoice> invoices = repository.findByCustomerIdAndAddressId(savedInvoice.getCustomer().getId(), savedInvoice.getAddress().getId());
        assertThat(invoices, contains(savedInvoice));
    }

    @Test
    public void shouldFindByCustomerIdAndMonth() throws Exception {
        Invoice savedInvoiceOne = repository.save(invoiceOne);
        Invoice savedInvoiceTwo = repository.save(invoiceTwo);

        List<Invoice> invoices = repository.findByCustomerIdAndMonth(customerOne.getId(), "MARCH");

        assertThat(invoices, containsInAnyOrder(savedInvoiceOne, savedInvoiceTwo));
    }

    @Test
    public void shouldFindByCustomerIdAndInvoiceTypeAndMonth() throws Exception {
        Invoice savedInvoiceTwo = repository.save(invoiceTwo);

        List<Invoice> invoices = repository.findByCustomerIdAndInvoiceTypeAndMonth(customerOne.getId(), "ShopPurchase", "MARCH");

        assertThat(invoices, containsInAnyOrder(savedInvoiceTwo));
        assertThat(invoices.size(), is(1));
    }

    private void setupCustomerAndAddress() {
        customerOne = new Customer();
        customerOne.setName("Person One");

        customerTwo = new Customer();
        customerTwo.setName("Person Two");

        addressOne = new Address();
        addressOne.setCity("Porto Alegre");
        addressOne.setCountry("Brazil");
        addressOne.setState("Rio Grande do Sul");
        addressOne.setCustomer(customerOne);

        addressTwo = new Address();
        addressTwo.setCity("São Paulo");
        addressTwo.setCountry("São Paulo");
        addressTwo.setState("Rio Grande do Sul");
        addressTwo.setCustomer(customerOne);

        addressThree = new Address();
        addressThree.setCity("São Paulo");
        addressThree.setCountry("São Paulo");
        addressThree.setState("Rio Grande do Sul");
        addressThree.setCustomer(customerTwo);
    }

}
package acmeinvoice.service;

import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import acmeinvoice.model.Invoice;
import acmeinvoice.model.InvoiceResponse;
import acmeinvoice.repository.AddressRepository;
import acmeinvoice.repository.CustomerRepository;
import acmeinvoice.repository.InvoiceRepository;
import com.google.common.collect.ImmutableMap;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static acmeinvoice.service.InvoiceConversionService.convert;
import static com.google.common.collect.Lists.newArrayList;
import static java.time.Month.of;
import static java.util.stream.Collectors.toList;
import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher.of;
import static org.springframework.data.domain.ExampleMatcher.StringMatcher.CONTAINING;

@AllArgsConstructor
@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository repository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AddressRepository addressRepository;

    public List<InvoiceResponse> findBy(Long customerId, Long addressId, String invoiceType, Integer month) {
        Customer customer = Customer.builder().id(customerId).build();
        Address address = Address.builder().id(addressId).build();
        Map<String, String> invoiceTypes = ImmutableMap.of("shop", "ShopPurchase", "payment", "AdvancePayment");
        String type = invoiceTypes.get(invoiceType);
        String monthDescription = null;
        if (month != null) {
            monthDescription = of(month).name().toLowerCase();
        }
        Invoice invoice = Invoice.builder()
                .customer(customer)
                .address(address)
                .invoiceType(type)
                .periodDescription(monthDescription)
                .build();

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("periodDescription", of(CONTAINING).ignoreCase());

        Example<Invoice> invoiceExample = Example.of(invoice, exampleMatcher);

        Iterable<Invoice> invoices = repository.findAll(invoiceExample);

        List<InvoiceResponse> invoiceResponses = newArrayList(invoices).stream()
            .map(InvoiceConversionService::convert)
            .collect(toList());

        return invoiceResponses;
    }

    public InvoiceResponse save(InvoiceResponse invoiceResponse) {
        Customer customer = customerRepository.findOne(invoiceResponse.getCustomerId());
        Address address = addressRepository.findOne(invoiceResponse.getAddressId());
        Invoice invoiceToBeSaved = convert(invoiceResponse);
        invoiceToBeSaved.setCustomer(customer);
        invoiceToBeSaved.setAddress(address);
        Invoice savedInvoice = repository.save(invoiceToBeSaved);
        return convert(savedInvoice);
    }
}

package acmeinvoice.service;

import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import acmeinvoice.model.Invoice;
import acmeinvoice.model.InvoiceResponse;
import acmeinvoice.repository.AddressRepository;
import acmeinvoice.repository.CustomerRepository;
import acmeinvoice.repository.InvoiceRepository;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static acmeinvoice.service.InvoiceConversionService.convert;
import static com.google.common.collect.ImmutableMap.of;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.time.Month.of;
import static java.util.stream.Collectors.toList;

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
        List<Invoice> invoices = newArrayList();

        if (customerId != null && invoiceType != null && month != null) {
            Map<String, String> invoiceTypeS = of("shop", "ShopPurchase", "payment", "AdvancePayment");
            String value = invoiceTypeS.get(invoiceType);
            String monthName = of(month).name();
            invoices = repository.findByCustomerIdAndInvoiceTypeAndMonth(customerId, value, monthName);
        } else if (customerId != null && month != null) {
            String monthName = of(month).name();
            invoices = repository.findByCustomerIdAndMonth(customerId, monthName);
        } else if (customerId != null && addressId != null) {
            invoices = repository.findByCustomerIdAndAddressId(customerId, addressId);
        } else if (customerId != null) {
            invoices = repository.findByCustomerId(customerId);
        }

        List<InvoiceResponse> invoiceResponses = invoices.stream()
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

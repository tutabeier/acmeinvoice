package acmeinvoice.service;

import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import acmeinvoice.model.Invoice;
import acmeinvoice.model.InvoiceResponse;
import acmeinvoice.repository.AddressRepository;
import acmeinvoice.repository.CustomerRepository;
import acmeinvoice.repository.InvoiceRepository;
import acmeinvoice.repository.InvoiceRepositoryImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static acmeinvoice.service.InvoiceConversionService.convert;
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
        List<Invoice> invoices = repository.findBy(customerId, addressId, invoiceType, month);

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

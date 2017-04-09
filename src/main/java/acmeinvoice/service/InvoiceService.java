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

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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
    @Autowired
    private EntityManager entityManager;

    public List<InvoiceResponse> findBy(Long customerId, Long addressId, String invoiceType, Integer month) {
        List<Invoice> invoices = newArrayList();
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Invoice.class);
        Root invoice = query.from(Invoice.class);
        CriteriaQuery select = query.select(invoice)
                .where(builder.equal(invoice.get("customer.id"), customerId))
                .where(builder.equal(invoice.get("address.id"), addressId))
                .where(builder.equal(invoice.get("invoiceType"), invoiceType))
                .where(builder.like(invoice.get("periodDescription"), of(month).name()));
        TypedQuery typedQuery = entityManager.createQuery(select);
        List<Invoice> results = typedQuery.getResultList();


        /*
            TODO: I'm not happy with the logic below. I've tried to use Query by Examples with pure JPA with no luck.
            Still need to look further and refactor the code.
         */
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

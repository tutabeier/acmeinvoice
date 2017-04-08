package acmeinvoice.service;

import acmeinvoice.model.Invoice;
import acmeinvoice.model.InvoiceResponse;
import acmeinvoice.repository.InvoiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

@AllArgsConstructor
@Service
public class InvoiceService {

    @Autowired
    private InvoiceRepository repository;

    public List<InvoiceResponse> findBy(Long customerId) {
        List<Invoice> byCustomerId = repository.findByCustomerId(customerId);

        return null;
    }

    public Invoice save(Invoice invoice) {
        return repository.save(invoice);
    }
}

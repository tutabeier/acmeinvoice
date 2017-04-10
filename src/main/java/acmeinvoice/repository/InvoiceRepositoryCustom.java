package acmeinvoice.repository;

import acmeinvoice.model.Invoice;

import java.util.List;

public interface InvoiceRepositoryCustom {
    List<Invoice> findBy(Long customerId, Long addressId, String invoiceType, Integer month);
}

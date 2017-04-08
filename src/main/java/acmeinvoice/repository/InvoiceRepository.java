package acmeinvoice.repository;

import acmeinvoice.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, String> {
    List<Invoice> findByCustomerIdAndAddressId(long customerId, long addressId);
    List<Invoice> findByCustomerId(long customerId);
    List<Invoice> findByCustomerIdAndInvoiceType(long customerId, String invoiceType);
    @Query("Select c from Invoice c where c.customer.id = ?1 and c.invoiceType = ?2 and lower(c.periodDescription) like lower(concat('%', ?3,'%'))")
    List<Invoice> findByCustomerIdAndInvoiceTypeAndMonth(long customerId, String invoiceType, String monthName);
    @Query("Select c from Invoice c where c.customer.id = ?1 and lower(c.periodDescription) like lower(concat('%', ?2,'%'))")
    List<Invoice> findByCustomerIdAndMonth(long customerId, String monthName);
}

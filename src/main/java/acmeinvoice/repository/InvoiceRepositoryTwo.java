package acmeinvoice.repository;

import acmeinvoice.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static java.time.Month.of;

@Repository
public class InvoiceRepositoryTwo {
    @Autowired
    EntityManager entityManager;

    public List<Invoice> findBy(Long customerId, Long addressId, String invoiceType, Integer month) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Invoice.class);
        Root invoice = query.from(Invoice.class);
        CriteriaQuery select = query.select(invoice);
        if (customerId != null) select.where(builder.equal(invoice.get("customer").get("id"), customerId));
        if (addressId != null) select.where(builder.equal(invoice.get("address").get("id"), addressId));
        if (invoiceType != null) select.where(builder.equal(invoice.get("invoiceType"), invoiceType));
        if (month != null)  {
            select.where(
                builder.like(
                        builder.lower(invoice.get("periodDescription")), "%" + of(month).name().toLowerCase() + "%")
            );
        }
        TypedQuery typedQuery = entityManager.createQuery(select);
        return typedQuery.getResultList();
    }
}

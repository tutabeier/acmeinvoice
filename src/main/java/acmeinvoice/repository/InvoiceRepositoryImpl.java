package acmeinvoice.repository;

import acmeinvoice.model.Invoice;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static java.time.Month.of;

public class InvoiceRepositoryImpl implements InvoiceRepositoryCustom{
    @Autowired
    EntityManager entityManager;

    public List<Invoice> findBy(Long customerId, Long addressId, String invoiceType, Integer month) {
        Map<String, String> invoiceTypes = ImmutableMap.of("shop", "ShopPurchase", "payment", "AdvancePayment");
        String type = invoiceTypes.get(invoiceType);

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery query = builder.createQuery(Invoice.class);
        Root invoice = query.from(Invoice.class);

        List<Predicate> predicates = newArrayList();

        if (customerId != null) {
            predicates.add(builder.equal(invoice.get("customer").get("id"), customerId));
        }

        if (addressId != null) {
            predicates.add(builder.equal(invoice.get("address").get("id"), addressId));
        }

        if (invoiceType != null) {
            predicates.add(builder.equal(invoice.get("invoiceType"), type));
        }

        if (month != null)  {
            predicates.add(
                builder.like(
                        builder.lower(invoice.get("periodDescription")), "%" + of(month).name().toLowerCase() + "%")
            );
        }

        CriteriaQuery select = query.select(invoice).
                where(predicates.toArray(new Predicate[]{}));

        TypedQuery typedQuery = entityManager.createQuery(select);
        return typedQuery.getResultList();
    }
}

package acmeinvoice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDate;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy= AUTO)
    private Long id;

    @OneToOne(targetEntity=Customer.class, fetch= LAZY)
    @JoinColumn(name="customer_id")
    private Customer customer;

    private LocalDate invoiceDate;

    private LocalDate paymentDueDate;

    private String invoiceNumber;

    private LocalDate startDate;

    private LocalDate endDate;

    private Double amount;

    private Double vatAmount;

    private Double totalAmount;
}

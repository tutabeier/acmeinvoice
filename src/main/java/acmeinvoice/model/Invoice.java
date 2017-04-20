package acmeinvoice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Locale.getDefault;
import static javax.persistence.GenerationType.AUTO;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class Invoice {
    @Id
    @GeneratedValue(strategy = AUTO)
    @Setter
    private Long id;

    @JoinColumn(name = "customer_id")
    @JsonIgnore
    @OneToOne(targetEntity = Customer.class)
    @Setter
    private Customer customer;

    @JoinColumn(name = "address_id")
    @JsonIgnore
    @OneToOne(targetEntity = Address.class)
    @Setter
    private Address address;

    protected String invoiceType;

    protected String invoiceTypeLocalized;

    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    protected LocalDate invoiceDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    protected LocalDate paymentDueDate;

    protected String invoiceNumber;

    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    protected LocalDate startDate;

    @JsonFormat(pattern="yyyy-MM-dd")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    protected LocalDate endDate;

    protected String periodDescription;

    protected Float amount;

    protected Float vatAmount;

    protected Float totalAmount;

    public Invoice(Long id, Customer customer, Address address, String invoiceType, String invoiceTypeLocalized, LocalDate invoiceDate, LocalDate paymentDueDate, String invoiceNumber, LocalDate startDate, LocalDate endDate, Float amount, Float vatAmount) {
        this(invoiceType, invoiceTypeLocalized, invoiceDate, paymentDueDate, invoiceNumber, startDate, endDate, amount, vatAmount);
        this.id = id;
        this.customer = customer;
        this.address = address;
    }

    public Invoice(String invoiceType, String invoiceTypeLocalized, LocalDate invoiceDate, LocalDate paymentDueDate, String invoiceNumber, LocalDate startDate, LocalDate endDate, Float amount, Float vatAmount) {
        this.invoiceType = invoiceType;
        this.invoiceTypeLocalized = invoiceTypeLocalized;
        this.invoiceDate = invoiceDate;
        this.paymentDueDate = paymentDueDate;
        this.invoiceNumber = invoiceNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.periodDescription = formatPeriodDescription(startDate);
        this.amount = amount;
        this.vatAmount = vatAmount;
        this.totalAmount = totalAmount(amount, vatAmount);
    }

    private float totalAmount(float amount, float vatAmount) {
        return amount + vatAmount;
    }

    private String formatPeriodDescription(LocalDate startDate) {
        DateTimeFormatter formatter = ofPattern("MMMM yyyy", getDefault());
        return startDate.format(formatter);
    }
}

package acmeinvoice.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@JsonAutoDetect(fieldVisibility = ANY)
@Data
@AllArgsConstructor
public class InvoiceResponse extends Invoice {
    private long customerId;
    private long addressId;
    private long invoiceId;

    public InvoiceResponse(long customerId, long addressId, long invoiceId, String invoiceType, String invoiceTypeLocalized, LocalDate invoiceDate, LocalDate paymentDueDate, String invoiceNumber, LocalDate startDate, LocalDate endDate, float amount, float vatAmount) {
        super(invoiceType, invoiceTypeLocalized, invoiceDate, paymentDueDate, invoiceNumber, startDate, endDate, amount, vatAmount);
        this.customerId = customerId;
        this.addressId = addressId;
        this.invoiceId = invoiceId;
    }
}

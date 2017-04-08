package acmeinvoice.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static java.time.format.DateTimeFormatter.ofPattern;
import static java.util.Locale.getDefault;

@JsonAutoDetect(fieldVisibility = ANY)
public class InvoiceResponse {
    private String customerId;
    private String addressId;
    private String invoiceId;
    private String invoiceType;
    private String invoiceTypeLocalized;
    private LocalDate invoiceDate;
    private LocalDate paymentDueDate;
    private String invoiceNumber;
    private LocalDate startDate;
    private LocalDate endDate;
    private String periodDescription;
    private float amount;
    private float vatAmount;
    private float totalAmount;

    public InvoiceResponse(String customerId, String addressId, String invoiceId, String invoiceType, String invoiceTypeLocalized, LocalDate invoiceDate, LocalDate paymentDueDate, String invoiceNumber, LocalDate startDate, LocalDate endDate, float amount, float vatAmount) {
        this.customerId = customerId;
        this.addressId = addressId;
        this.invoiceId = invoiceId;
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

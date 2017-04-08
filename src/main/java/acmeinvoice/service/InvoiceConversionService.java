package acmeinvoice.service;

import acmeinvoice.model.Invoice;
import acmeinvoice.model.InvoiceResponse;

public class InvoiceConversionService {

    public static Invoice convert(InvoiceResponse invoiceResponse) {
        return new Invoice(invoiceResponse.getInvoiceType(), invoiceResponse.getInvoiceTypeLocalized(), invoiceResponse.getInvoiceDate(),
                invoiceResponse.getPaymentDueDate(), invoiceResponse.getInvoiceNumber(), invoiceResponse.getStartDate(),
                invoiceResponse.getEndDate(), invoiceResponse.getAmount(), invoiceResponse.getVatAmount());
    }

    public static InvoiceResponse convert(Invoice invoice) {
        return new InvoiceResponse(invoice.getCustomer().getId(), invoice.getAddress().getId(), invoice.getId(),
                invoice.getInvoiceType(), invoice.getInvoiceTypeLocalized(), invoice.getInvoiceDate(), invoice.getPaymentDueDate(),
                invoice.getInvoiceNumber(), invoice.getStartDate(), invoice.getEndDate(), invoice.getAmount(), invoice.getVatAmount());
    }
}

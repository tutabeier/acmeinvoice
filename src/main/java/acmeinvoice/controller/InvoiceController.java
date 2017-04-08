package acmeinvoice.controller;

import acmeinvoice.model.InvoiceResponse;
import acmeinvoice.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping(value = "/v1.0/invoices/")
public class InvoiceController {

    @Autowired
    private InvoiceService service;

    @RequestMapping(method = GET)
    public ResponseEntity findBy(@RequestParam(required = false) Long customerId,
                                 @RequestParam(required = false) Long addressId) {
        List<InvoiceResponse> invoices = service.findBy(customerId, addressId);
        return new ResponseEntity(invoices, OK);
    }

    @RequestMapping(method = POST)
    public ResponseEntity create(@RequestBody InvoiceResponse invoiceResponse) {
        InvoiceResponse invoiceSaved = service.save(invoiceResponse);
        return new ResponseEntity(invoiceSaved, CREATED);
    }
}
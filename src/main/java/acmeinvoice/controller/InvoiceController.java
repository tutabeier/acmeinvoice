package acmeinvoice.controller;

import acmeinvoice.model.Invoice;
import acmeinvoice.repository.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/v1.0/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceRepository repository;

    @RequestMapping(method = GET)
    public ResponseEntity findAll() {
        List<Invoice> invoices = repository.findAll();
        return new ResponseEntity(invoices, OK);
    }

    @RequestMapping(method = POST)
    public ResponseEntity create(@RequestBody Invoice invoice) {
        Invoice invoiceSaved = repository.save(invoice);
        return new ResponseEntity(invoiceSaved, CREATED);
    }
}

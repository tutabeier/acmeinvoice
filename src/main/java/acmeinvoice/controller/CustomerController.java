package acmeinvoice.controller;

import acmeinvoice.model.Customer;
import acmeinvoice.repository.CustomerRepository;
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
@RequestMapping("/v1.0/customers")
public class CustomerController {
    @Autowired
    private CustomerRepository repository;

    @RequestMapping(method = GET)
    public ResponseEntity findAll() {
        List<Customer> customers = repository.findAll();
        return new ResponseEntity(customers, OK);
    }

    @RequestMapping(method = POST)
    public ResponseEntity create(@RequestBody Customer customer) {
        Customer customerSaved = repository.save(customer);
        return new ResponseEntity(customerSaved, CREATED);
    }
}

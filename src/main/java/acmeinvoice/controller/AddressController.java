package acmeinvoice.controller;

import acmeinvoice.model.Address;
import acmeinvoice.model.Customer;
import acmeinvoice.repository.AddressRepository;
import acmeinvoice.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
@RequestMapping("/v1.0/address")
public class AddressController {
    @Autowired
    private AddressRepository repository;

    @RequestMapping(method = GET)
    public ResponseEntity findAll() {
        List<Address> addresses = repository.findAll();
        return new ResponseEntity(addresses, OK);
    }

    @RequestMapping(method = POST)
    public ResponseEntity create(@RequestBody Address address) {
        Address addressSaved = repository.save(address);
        return new ResponseEntity(addressSaved, CREATED);
    }
}
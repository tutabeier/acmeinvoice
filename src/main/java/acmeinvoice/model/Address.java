package acmeinvoice.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

public class Address {
    @Id
    @GeneratedValue(strategy= AUTO)
    private Long id;

    private String country;

    private String city;

    private String number;
}

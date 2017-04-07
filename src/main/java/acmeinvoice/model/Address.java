package acmeinvoice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.GenerationType.AUTO;

@Data
@Entity
public class Address {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String city;
    private String state;
    private String country;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonBackReference
    private Customer customer;
}
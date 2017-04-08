package acmeinvoice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import static javax.persistence.CascadeType.ALL;
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
    private Customer customer;
}
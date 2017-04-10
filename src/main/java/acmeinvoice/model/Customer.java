package acmeinvoice.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.AUTO;

@Data
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String name;
}
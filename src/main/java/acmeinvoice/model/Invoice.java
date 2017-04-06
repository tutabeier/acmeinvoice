package acmeinvoice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Data
@Entity
public class Invoice {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;

    @JoinColumn(name = "customer_id")
    @OneToOne(targetEntity = Customer.class, cascade = ALL)
    private Customer customer;


    @JoinColumn(name = "address_id")
    @OneToOne(targetEntity = Address.class, cascade = ALL)
    private Address address;

//    @Column(name = "id_customer", insertable = false, updatable = false)
//    private Long customerId;
//    @Column(name = "id_address", insertable = false, updatable = false)
//    private Long addressId;
}

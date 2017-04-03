package acmeinvoice.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.util.List;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.AUTO;

@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy= AUTO)
    private Long id;

    @OneToMany(targetEntity=Address.class, fetch= LAZY)
    @JoinColumn(name="address_id")
    private List<Address> address;

    private String name;
}

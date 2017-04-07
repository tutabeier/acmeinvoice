package acmeinvoice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.GenerationType.AUTO;

@Data
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "customer", cascade = ALL)
    @JsonManagedReference
    private List<Address> addresses;

    public void setAddresses(List<Address> addresses) {
        for (Address child : addresses) {
            child.setCustomer(this);
        }
        this.addresses = addresses;
    }
}
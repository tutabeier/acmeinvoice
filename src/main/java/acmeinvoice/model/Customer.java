package acmeinvoice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.PERSIST;
import static javax.persistence.GenerationType.AUTO;

@Data
@Entity
public class Customer {
    @Id
    @GeneratedValue(strategy = AUTO)
    private Long id;
    private String name;
    @OneToMany(mappedBy = "customer", cascade = ALL)
    private List<Address> address;

    public void setAddress(List<Address> address) {
        for (Address child : address) {
            // initializing the TestObj instance in Children class (Owner side)
            // so that it is not a null and PK can be created
            child.setCustomer(this);
        }
        this.address = address;
    }


}
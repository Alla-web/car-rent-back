package de.aittr.car_rent.domain.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "is_18")
    private boolean is18;

    @Column(name = "password")
    private String password;

    @Column(name = "email")
    private String email;

    @Column(name = "active")
    boolean isActive;

//    @OneToMany(mappedBy = "customer")
//    private Booking booking;
//
//    @ManyToOne
//    @JoinColumn(name = "role_id")
//    private Role role;

    @Override
    public String toString() {
        return String.format("Customer: id - %d, name - %s, last name - %s, email - %s, " +
                "is 18 - %s, active - %s.", id, firstName, lastName, email, is18 ? "yes" : "no", isActive ? "yes" : "no");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

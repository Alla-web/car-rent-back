package de.aittr.car_rent.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "customer")
public class Customer implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    @Column(name = "id")
    private Long id;

    @NotNull(message = "{customer.firstName.notBlank}")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull(message = "customer.lastName.notBlank")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank(message = "{customer.password.notBlank}")
    @Column(name = "password", nullable = false)
    private String password;

    @NotNull(message = "{customer.email.notBlank}")
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "active")
    boolean isActive;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<Booking> bookings = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    public Customer(String firstName, String lastName, String password, String email, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.email = email;
        this.role = role;
        this.isActive = false;
    }

    @Override
    public String toString() {
        return String.format("Customer: id - %d, name - %s, last name - %s, email - %s, active - %s.", id, firstName, lastName, email, isActive ? "yes" : "no");
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

    /**
     * Метод получения списка ролей покупателя Spring Security
     *
     * @return список ролей, которыми обладает покупатель
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.getTitle()));
    }

    /**
     * Метод получения полного имени (имя + фамилия) покупателя Spring Security
     *
     * @return имя покупателя
     */
    @Override
    public String getUsername() {
        return email;
    }

    // временный метод для создания тестового зашифрованного пароля
     public static void main(String[] args) {
     System.out.println(new BCryptPasswordEncoder().encode("User-pass#007"));
     }
}

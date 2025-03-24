package de.aittr.car_rent.security;

import de.aittr.car_rent.domain.entity.Role;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Collections;

@EqualsAndHashCode
public class AuthInfo implements Authentication {

    private boolean authenticated;
    private String userEmail;
    private Role role;

    public AuthInfo(String userEmail, Role role) {
        this.userEmail = userEmail;
        this.role = role;
    }

    @Override
    public String toString() {
        return String.format("Auth info: authorized - %s; email - %s; role - %s",
                authenticated, userEmail, role);
    }

    @Override
    public String getName() {
        return userEmail;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userEmail;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }
}

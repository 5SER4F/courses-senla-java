package org.uhanov.model.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "\"user\"")
@Inheritance(strategy = InheritanceType.JOINED)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements UserDetails {
    @Id
    @GeneratedValue(generator = "uuid-generator")
    @Column(name = "id", columnDefinition = "UUID")
    protected UUID id;
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    protected Role role;
    @Column(name = "username", nullable = false, unique = true, updatable = false)
    protected String username;
    @Column(name = "password", nullable = false)
    protected String password;
    @Column(name = "email", nullable = false, unique = true)
    protected String email;
    @Column(name = "registration_date", updatable = false)
    @EqualsAndHashCode.Exclude
    protected LocalDateTime registrationDate;
    @Column(name = "account_status")
    @Enumerated(EnumType.STRING)
    protected AccountStatus accountStatus;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return accountStatus.equals(AccountStatus.CREATED);
    }
}

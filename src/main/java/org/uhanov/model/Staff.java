package org.uhanov.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.uhanov.model.user.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "staff")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Staff extends User implements UserDetails {
    @Column(name = "firstname", nullable = false, unique = true)
    private String firstname;
    @Column(name = "surname", nullable = false)
    private String surname;
    @Column(name = "nickname", nullable = false)
    private String nickname;
    @Column(name = "birth_date", nullable = false, updatable = false)
    private LocalDate birthDate;

}

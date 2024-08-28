package org.uhanov.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User implements EntityWithUUID {
    private UUID id;
    private String password;
    private String firstname;
    private String surname;
    private String nickname;
    private LocalDate birthDate;
    private LocalDateTime registrationDate;
    private String country;

}

package org.uhanov.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Staff implements EntityWithUUID {
    private UUID id;
    private String password;
    private String firstname;
    private String surname;
    private Timestamp birthDate;
    private Timestamp registrationDate;
}

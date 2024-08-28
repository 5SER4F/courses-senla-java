package org.uhanov.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Creator implements EntityWithUUID {
    private UUID id;
    private String password;
    private String name;
    private LocalDateTime registrationDate;
}

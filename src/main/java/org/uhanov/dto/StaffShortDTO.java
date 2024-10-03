package org.uhanov.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uhanov.dto.serializer.LocalDateDeserializer;
import org.uhanov.dto.serializer.LocalDateSerializer;
import org.uhanov.dto.serializer.LocalDateTimeDeserializer;
import org.uhanov.dto.serializer.LocalDateTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffShortDTO {
    private UUID id;
    private String firstname;
    private String surname;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate birthDate;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime registrationDate;
}

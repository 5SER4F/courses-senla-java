package org.uhanov.dto.creator;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uhanov.dto.serializer.LocalDateTimeDeserializer;
import org.uhanov.dto.serializer.LocalDateTimeSerializer;
import org.uhanov.model.user.Role;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonInclude(
        JsonInclude.Include.NON_NULL
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatorPostDto {
    private UUID id;
    @NotBlank
    private String password;
    @NotBlank
    private String username;
    @NotBlank
    private String email;
    private Role role;
    @NotBlank
    private String name;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime registrationDate = LocalDateTime.now();
}

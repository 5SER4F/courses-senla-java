package org.uhanov.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.uhanov.security.Role;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInDto {
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;
    @NotBlank(message = "Пароль не может быть пустыми")
    private String password;
    @NotBlank
    private String role;
}

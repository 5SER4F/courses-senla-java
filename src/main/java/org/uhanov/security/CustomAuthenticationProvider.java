package org.uhanov.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.uhanov.exception.InvalidLoginException;
import org.uhanov.model.user.User;
import org.uhanov.repository.api.UserRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String passwordBeforeDecode = authentication.getCredentials().toString();
        return authUser(userName, passwordBeforeDecode);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }

    private UsernamePasswordAuthenticationToken authUser(String name, String passwordBeforeDecode) {
        Optional<User> requester = userRepository.findByUsername(name);
        if (requester.isEmpty() ||
                !encoder.matches(passwordBeforeDecode, requester.get().getPassword())) {
            throw new InvalidLoginException("Неверная комбинация логина и пароля:" + name);
        }

        if (!requester.get().isEnabled()) {
            throw new InvalidLoginException("Попытка войти в удаленный аккаунт");
        }

        User user = requester.get();
        return new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                List.of(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }

}

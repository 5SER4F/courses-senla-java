package org.uhanov.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.uhanov.exception.NoRoleException;
import org.uhanov.exception.InvalidLoginException;
import org.uhanov.model.Creator;
import org.uhanov.model.Staff;
import org.uhanov.model.User;
import org.uhanov.repository.api.CreatorRepository;
import org.uhanov.repository.api.StaffRepository;
import org.uhanov.repository.api.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserRepository userRepository;
    private final StaffRepository staffRepository;
    private final CreatorRepository creatorRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("MY PROVIDER WORK MY PROVIDER WORK MY PROVIDER WORK MY PROVIDER WORK MY PROVIDER WORK MY PROVIDER WORK MY PROVIDER WORK MY PROVIDER WORK MY PROVIDER WORK MY PROVIDER WORK MY PROVIDER WORK MY PROVIDER WORK MY PROVIDER WORK MY PROVIDER WORK ");
        Role role = extractRole(authentication);
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();
        switch (role) {
            case USER:
                authUser(userName, password);
                break;
            case STAFF:
                authStaff(userName, password);
                break;
            case CREATOR:
                authCreator(userName, password);
                break;
        }

//        var auth = new UsernamePasswordAuthenticationToken(
//                userName,
//                password,
//                List.of(new SimpleGrantedAuthority(role.name()))
//        );
//
//        var context = SecurityContextHolder.getContext();
//
//        System.out.println("CONTEXT=" + context);
//
//        context.setAuthentication(auth);
//
//        System.out.println("CONTEXTTTT=" + context);

        return new UsernamePasswordAuthenticationToken(
                userName,
                password,
                List.of(new SimpleGrantedAuthority(role.name()))
        );
    }

    @Override
    public boolean supports(Class<?> authentication) {
        var b = authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);

        System.out.println(b+"KKKKKKKKKKKKKKKK\n" + authentication.getName());
        return b;
    }

    private void authUser(String name, String password) {
        Optional<User> user = userRepository.findByNickname(name);
        if (user.isEmpty() ||
                !user.get().getPassword().equals(password)) {
            throw new InvalidLoginException("Неверная комбинация логина и пароля для пользователя:" + name);
        }

    }

    private void authStaff(String name, String password) {
        Optional<Staff> staff = staffRepository.findByUsername(name);
        if (staff.isEmpty() ||
                !password.equals(staff.get().getPassword())) {
            throw new InvalidLoginException("Неверная комбинация логина и пароля для сотрудника:" + name);
        }
    }

    private void authCreator(String name, String password) {
        Optional<Creator> creator = creatorRepository.findByName(name);
        if (creator.isEmpty() ||
                !password.equals(creator.get().getPassword())) {
            throw new InvalidLoginException("Неверная комбинация логина и пароля для автора:" + name);
        }
    }

    private Role extractRole(Authentication authentication) {
        try {
            String roleName = authentication.getAuthorities()
                    .stream()
                    .findFirst()
                    .get()
                    .getAuthority();
            return Role.valueOf(roleName);
        } catch (NoSuchElementException | IllegalArgumentException e) {
            e.printStackTrace();
            throw new NoRoleException();
        }

    }
}

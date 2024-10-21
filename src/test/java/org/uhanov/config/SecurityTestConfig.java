package org.uhanov.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.uhanov.repository.api.CreatorRepository;
import org.uhanov.repository.api.StaffRepository;
import org.uhanov.repository.api.UserRepository;
import org.uhanov.security.CustomAuthenticationProvider;
import org.uhanov.security.JwtAuthenticationFilter;
import org.uhanov.security.Role;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@ComponentScan("org.uhanov")
@Profile("test")
public class SecurityTestConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomAuthenticationProvider customAuthenticationProvider;

//    @Bean
//    public AuthenticationProvider authenticationProvider(
//            UserRepository userRepository,
//            StaffRepository staffRepository,
//            CreatorRepository creatorRepository
//    ) {
//        AuthenticationProvider authProvider =
//                new CustomAuthenticationProvider(userRepository, staffRepository, creatorRepository);
//        return authProvider;
//    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(customAuthenticationProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {
        System.out.println("GGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG" + jwtAuthenticationFilter);


        http
                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(request -> request
//                        .requestMatchers("/users", "/users/login").permitAll()
//                        .requestMatchers("/users/**").authenticated()
//                        )
                .authorizeHttpRequests(r -> r.anyRequest().permitAll())
                .formLogin(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtAuthenticationFilter, AuthorizationFilter.class)
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(customAuthenticationProvider);
        var filterChain = http.build();

        System.out.println("FILTERS\n" + filterChain.getFilters());
        return filterChain;
//        return new SecurityFilterChain() {
//            private JwtAuthenticationFilter jwtAuthenticationFilter;
//            @Override
//            public boolean matches(HttpServletRequest request) {
//                return false;
//            }
//
//            @Override
//            public List<Filter> getFilters() {
//                return List.of(jwtAuthenticationFilter);
//            }
//        };
    }


}

package org.uhanov.security;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.uhanov.exception.InvalidLoginException;
import org.uhanov.repository.api.CreatorRepository;
import org.uhanov.repository.api.StaffRepository;
import org.uhanov.repository.api.UserRepository;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    private final JwtUtil jwtUtil;
//    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final CreatorRepository creatorRepository;
    private final StaffRepository staffRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    )
            throws ServletException, IOException {
        System.out.println("FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! ");

        String authHeader = request.getHeader(HEADER_NAME);

        if (StringUtils.hasText(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(BEARER_PREFIX.length());
        String username = jwtUtil.extractUserName(jwt);
        Role role = jwtUtil.extractRole(jwt);
        System.out.println("FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! ");

        if (StringUtils.hasText(username)) {
            //ПЕРЕДАТЬ РОЛЬ В getAuthorities
            UserDetails userDetails;
            switch (role) {
                case CREATOR:
                    userDetails = creatorRepository.findCreatorDetailsByName(username);
                    break;
                case STAFF:
                    userDetails = staffRepository.findStaffDetailsByName(username);
                    break;
                case USER:
                    userDetails = userRepository.findUserDetailsByNickname(username);
                    break;
                default:
                    throw new InvalidLoginException();
            }
            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                SecurityContext context = SecurityContextHolder.getContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        userDetails.getPassword(),//???? правильно ли это
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
//                SecurityContextHolder.setContext(context);//?????
            }
        }
        System.out.println("FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! ");

        filterChain.doFilter(request, response);

    }
}

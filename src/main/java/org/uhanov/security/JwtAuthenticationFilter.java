package org.uhanov.security;

import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;
import org.uhanov.exception.InvalidLoginException;
import org.uhanov.repository.api.CreatorRepository;
import org.uhanov.repository.api.StaffRepository;
import org.uhanov.repository.api.UserRepository;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements Filter
//        OncePerRequestFilter
{
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    private final JwtUtil jwtUtil;
    //    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final CreatorRepository creatorRepository;
    private final StaffRepository staffRepository;

    @Override
    public void doFilter(
             ServletRequest httpRequest,
             ServletResponse httpResponse,
             FilterChain filterChain)
            throws IOException, ServletException {
        System.out.println("FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! ");

        if (!(httpRequest instanceof HttpServletRequest) || !(httpResponse instanceof HttpServletResponse)) {
            System.out.println("1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
            throw new ServletException("OncePerRequestFilter just supports HTTP requests");
        }
        HttpServletRequest request = (HttpServletRequest) httpRequest;
        HttpServletResponse response = (HttpServletResponse) httpResponse;

        String authHeader = request.getHeader(HEADER_NAME);

        System.out.println("OOOOOOOOOOOOOOOOOOOO=" + authHeader);

        if (!StringUtils.hasText(authHeader)
                || !StringUtils.startsWithIgnoreCase(authHeader, BEARER_PREFIX)
        ) {
            System.out.println("222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222222");

            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(BEARER_PREFIX.length());
        String username = jwtUtil.extractUserName(jwt);
        Role role = jwtUtil.extractRole(jwt);

        System.out.println(username + "\n\n\n" + role);

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
            System.out.println("3333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333333 ");

            if (jwtUtil.isTokenValid(jwt, userDetails)) {
                System.out.println("444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444444 ");

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

//        @Override
//    public void doFilterInternal(
//            @NonNull ServletRequest request,
//            @NonNull ServletResponse response,
//            @NonNull FilterChain filterChain
//    )
//            throws ServletException, IOException {
//        System.out.println("FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! ");
//
//        String authHeader = request.getHeader(HEADER_NAME);
//
//        if (StringUtils.hasText(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader, BEARER_PREFIX)) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String jwt = authHeader.substring(BEARER_PREFIX.length());
//        String username = jwtUtil.extractUserName(jwt);
//        Role role = jwtUtil.extractRole(jwt);
//        System.out.println("FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! ");
//
//        if (StringUtils.hasText(username)) {
//            //ПЕРЕДАТЬ РОЛЬ В getAuthorities
//            UserDetails userDetails;
//            switch (role) {
//                case CREATOR:
//                    userDetails = creatorRepository.findCreatorDetailsByName(username);
//                    break;
//                case STAFF:
//                    userDetails = staffRepository.findStaffDetailsByName(username);
//                    break;
//                case USER:
//                    userDetails = userRepository.findUserDetailsByNickname(username);
//                    break;
//                default:
//                    throw new InvalidLoginException();
//            }
//            if (jwtUtil.isTokenValid(jwt, userDetails)) {
//                SecurityContext context = SecurityContextHolder.getContext();
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        userDetails.getUsername(),
//                        userDetails.getPassword(),//???? правильно ли это
//                        userDetails.getAuthorities()
//                );
//
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                context.setAuthentication(authToken);
////                SecurityContextHolder.setContext(context);//?????
//            }
//        }
//        System.out.println("FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! FILTER WORK!!! ");
//
//        filterChain.doFilter(request, response);
//
//    }
}

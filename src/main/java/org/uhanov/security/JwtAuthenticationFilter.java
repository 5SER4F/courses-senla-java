package org.uhanov.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.uhanov.model.user.Role;
import org.uhanov.repository.api.UserRepository;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements Filter {
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public void doFilter(
            ServletRequest httpRequest,
            ServletResponse httpResponse,
            FilterChain filterChain)
            throws IOException, ServletException {

        if (!(httpRequest instanceof HttpServletRequest) || !(httpResponse instanceof HttpServletResponse)) {
            throw new ServletException("OncePerRequestFilter just supports HTTP requests");
        }
        HttpServletRequest request = (HttpServletRequest) httpRequest;
        HttpServletResponse response = (HttpServletResponse) httpResponse;

        String authHeader = request.getHeader(HEADER_NAME);

        if (!StringUtils.hasText(authHeader)
                || !StringUtils.startsWithIgnoreCase(authHeader, BEARER_PREFIX)
        ) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwt = authHeader.substring(BEARER_PREFIX.length());
        String username = jwtUtil.extractUserName(jwt);
        Role role = jwtUtil.extractRole(jwt);

        if (StringUtils.hasText(username)) {
            UserDetails userDetails;

            if (!userRepository.existsByUsername(username)) {
                filterChain.doFilter(request, response);
                return;
            }
            userDetails = userRepository.findUserDetailsByUsername(username);

            if (jwtUtil.isTokenValid(jwt, userDetails)) {

                SecurityContext context = SecurityContextHolder.getContext();
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(),
                        null,
                        userDetails.getAuthorities()
                );

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                context.setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}

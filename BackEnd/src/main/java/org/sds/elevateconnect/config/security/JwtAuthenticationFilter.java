package org.sds.elevateconnect.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static org.sds.elevateconnect.utils.Constants.UNAUTHORISED_ENDPOINTS;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // If the requested endpoint is supposed to be unauthorised, allow the request to pass through
        String requestURI = request.getRequestURI();
        if (Arrays.asList(UNAUTHORISED_ENDPOINTS).contains(requestURI)) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String email;

        // Check if auth header is present
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        email = jwtService.getEmail(jwt);

        boolean isEmailValid = email != null;
        boolean isUserNotAuthenticated = SecurityContextHolder.getContext().getAuthentication() == null;

        if (isEmailValid && isUserNotAuthenticated) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtService.isTokenValid(jwt, userDetails)) {
                log.info("Authenticating user with email: {}", email);

                // Create a new authentication token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Store details from the request
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // And update the security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}

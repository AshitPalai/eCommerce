package com.example.user.security;

import com.example.user.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {


    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // ✅ SKIP filter for login
        if (path.contains("/api/v1/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        System.out.println("JWT FILTER HIT");

        String authHeader = request.getHeader("Authorization");
        System.out.println("Auth Header: " + authHeader);


        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);
            System.out.println("Token: " + token);

            try {
                String username = jwtUtil.extractUsername(token);
                System.out.println("Username: " + username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("AUTH SET: " + SecurityContextHolder.getContext().getAuthentication());
                    System.out.println("Authorities: " + userDetails.getAuthorities());
                }

            } catch (Exception e) {
                e.printStackTrace(); // don't block request
            }
        }

        filterChain.doFilter(request, response);
    }

}

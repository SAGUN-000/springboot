package com.example.Nap.Buyzen.security;

import com.example.Nap.Buyzen.entities.User;
import com.example.Nap.Buyzen.repository.UserRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final AuthUtil authUtil;
    private final UserRepo userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        log.info("=== JWT FILTER HIT ===");
        log.info("Request URI: {}", request.getRequestURI());
        log.info("Auth Header: {}", request.getHeader("Authorization"));

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            log.warn("NO TOKEN FOUND → skipping auth");
            filterChain.doFilter(request, response);
            return;
        }


        try {
            String token = header.substring(7).trim();
            log.info("Token extracted: {}", token);

            String username = authUtil.getUsernameFromToken(token);
            log.info("Username from token: {}", username);

            if (username == null) {
                log.error("USERNAME NULL FROM TOKEN");
                filterChain.doFilter(request, response);
                return;
            }

            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                log.warn("Already authenticated → skipping");
                filterChain.doFilter(request, response);
                return;
            }

            User user = userRepo.findByEmail(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            if (user == null) {
                log.error("USER NOT FOUND IN DB: {}", username);
                filterChain.doFilter(request, response);
                return;
            }

            SecurityPrinciple principle = new SecurityPrinciple(
                    user.getId(),
                    user.getEmail(),
                    user.getPassword(),
                    List.of(
                            new SimpleGrantedAuthority(
                                    "ROLE_" + user.getRole().name()
                            )
                    )
            );



            log.info("User found: {}", user.getEmail());
            log.info("Authorities: {}", principle.getAuthorities());

            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            principle,
                            null,
                            principle.getAuthorities()
                    );
            auth.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            SecurityContextHolder.getContext().setAuthentication(auth);

            log.info("AUTH SET SUCCESSFULLY");
            log.info("SecurityContext: {}", SecurityContextHolder.getContext().getAuthentication());

        }  catch (Exception e) {
            log.error("JWT FILTER ERROR", e);
            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }
}
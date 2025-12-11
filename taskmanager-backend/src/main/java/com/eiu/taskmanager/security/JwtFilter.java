package com.eiu.taskmanager.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7).trim();

            // ✅ FIX: Validate token and reject if invalid
            if (jwtUtil.validateToken(token)) {
                try {
                    String username = jwtUtil.extractUsername(token);
                    List<String> roles = jwtUtil.extractRoles(token);

                    if (username != null) {
                        List<SimpleGrantedAuthority> authorities;
                        if (roles != null && !roles.isEmpty()) {
                            authorities = roles.stream()
                                    .map(role -> {
                                        if (!role.startsWith("ROLE_")) {
                                            return new SimpleGrantedAuthority("ROLE_" + role);
                                        }
                                        return new SimpleGrantedAuthority(role);
                                    })
                                    .collect(Collectors.toList());
                        } else {
                            authorities = List.of();
                        }

                        UsernamePasswordAuthenticationToken authentication = 
                            new UsernamePasswordAuthenticationToken(username, null, authorities);

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                        logger.debug("✅ Authenticated: {} with roles: {}", username, roles);
                    }
                } catch (Exception e) {
                    logger.error("❌ Token processing failed: {}", e.getMessage());
                    SecurityContextHolder.clearContext();
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\": \"Invalid or malformed JWT token\"}");
                    response.setContentType("application/json");
                    return; // ✅ STOP HERE - don't continue filter chain
                }
            } else {
                // ✅ FIX: Invalid/expired token = REJECT
                logger.warn("❌ Invalid JWT token");
                SecurityContextHolder.clearContext();
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\": \"Invalid or expired JWT token\"}");
                response.setContentType("application/json");
                return; // ✅ STOP HERE - don't continue filter chain
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/auth/") ||
               path.equals("/v3/api-docs") ||
               path.startsWith("/v3/api-docs/") ||
               path.startsWith("/swagger-ui/") ||
               path.equals("/swagger-ui.html");
    }
}
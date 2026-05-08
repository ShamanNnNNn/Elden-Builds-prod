package com.example.demo.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String uri = request.getRequestURI();
        String method = request.getMethod();

        System.out.println("JwtAuthFilter - Method: " + method + ", URI: " + uri);

        // Public API: allow only list and guide endpoints without token.
        boolean isPublicBuildsList = method.equals("GET") && uri.equals("/api/builds");
        boolean isPublicBuildById = method.equals("GET") && uri.matches("^/api/builds/\\d+$");
        if (isPublicBuildsList || isPublicBuildById) {
            System.out.println("Public GET endpoint, skipping auth");
            filterChain.doFilter(request, response);
            return;
        }

        // Пропускаем страницы билда (HTML)
        if (uri.startsWith("/build/") || uri.startsWith("/build-editor/")) {
            System.out.println("Build page, skipping auth");
            filterChain.doFilter(request, response);
            return;
        }

        // Пропускаем публичные страницы и статику
        if (uri.equals("/") || uri.equals("/index") || uri.equals("/builds") ||
                uri.equals("/builds-page") || uri.equals("/my-builds") || uri.equals("/moderation") ||
                uri.startsWith("/webjars/") || uri.startsWith("/uploads/") ||
                uri.startsWith("/api/auth/") || uri.startsWith("/h2-console")) {
            System.out.println("Public path, skipping auth");
            filterChain.doFilter(request, response);
            return;
        }

        // Для всех остальных запросов (POST, PUT, DELETE) проверяем токен
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("No valid Bearer token found - returning 401");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Missing or invalid token");
            return;
        }

        final String jwt = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("Authentication set for user: " + userEmail);
            }
        }

        filterChain.doFilter(request, response);
    }
}
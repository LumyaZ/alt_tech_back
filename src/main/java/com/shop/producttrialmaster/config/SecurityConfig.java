package com.shop.producttrialmaster.config;

import com.shop.producttrialmaster.security.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    // Email en dur plutôt qu'un rôle : le sujet demande explicitement une solution
    // simple, sans gestion des accès basée sur les rôles.
    private static final String ADMIN_EMAIL = "admin@admin.com";

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Réutilisable sur n'importe quelle route à réserver à l'admin : compare juste
    // l'email posé comme principal par JwtAuthenticationFilter.
    private static AuthorizationManager<RequestAuthorizationContext> isAdmin() {
        return (authentication, context) ->
                new AuthorizationDecision(ADMIN_EMAIL.equals(authentication.get().getName()));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint((request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                        // Non-admin sur une route d'écriture : 403 avec un message clair plutôt
                        // que la page HTML générique par défaut de Spring Security
                        .accessDeniedHandler((request, response, accessDeniedException) ->
                                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé à l'administrateur")))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/account", "/token").permitAll()
                        // Sans ça, le forward interne de Spring Boot vers /error (pour construire une réponse
                        // d'erreur, ex. 409/500) est lui-même bloqué par la sécurité et masqué en 401
                        .requestMatchers("/error").permitAll()
                        // Écriture sur /products réservée à l'admin ; la lecture (GET) reste ouverte
                        // à tout utilisateur authentifié, couverte par la règle .anyRequest() ci-dessous
                        .requestMatchers(HttpMethod.POST, "/products").access(isAdmin())
                        .requestMatchers(HttpMethod.PUT, "/products/**").access(isAdmin())
                        .requestMatchers(HttpMethod.DELETE, "/products/**").access(isAdmin())
                        .anyRequest().authenticated())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

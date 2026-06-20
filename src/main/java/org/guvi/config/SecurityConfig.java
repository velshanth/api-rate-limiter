package org.guvi.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.LocalDateTime;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final RateLimitingFilter rateLimitingFilter;
    private final LoggingFilter loggingFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, RateLimitingFilter rateLimitingFilter, LoggingFilter loggingFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.rateLimitingFilter = rateLimitingFilter;
        this.loggingFilter = loggingFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(csrf -> csrf.disable());

        http.authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/api/auth/signup",
                        "/api/auth/login",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html"
                ).permitAll()
                .requestMatchers("/api/gateway/**")
                .authenticated()
                .anyRequest()
                .authenticated()
        );

        AuthenticationEntryPoint entryPoint = (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                  "status": 401,
                  "message": "Unauthorized - Invalid or missing token"
                }
            """);
        };

        AccessDeniedHandler deniedHandler = (request, response, ex) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                  "status": 403,
                  "message": "Forbidden"
                }
            """);
        };

        http.exceptionHandling(ex -> ex
                .authenticationEntryPoint(entryPoint)
                .accessDeniedHandler(deniedHandler)
        );

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterAfter(
                rateLimitingFilter,
                JwtAuthFilter.class
        );
        http.addFilterAfter(
                loggingFilter,
                RateLimitingFilter.class
        );

        return http.build();
    }
}
package com.regalo_libre.config;

import com.regalo_libre.auth.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j

public class SecurityConfig {
    private final Environment environment;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {


        log.info("Running on environment" + environment);
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    http.requestMatchers(HttpMethod.GET, "/api/v1/meli/code").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/v1/login/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/v1/token/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/v1/lists/public/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/v1/lists/user/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/v1/profile/public/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/v1/lists/*/public/*").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/v1/follow/followers").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/v1/follow/followees").permitAll();
                    http.requestMatchers("/actuator/health", "/actuator/info", "/actuator/caches", "/actuator/metrics").permitAll();
                    http.anyRequest().authenticated();
                })
                .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.accessDeniedPage("/api/v1/login"))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://localhost:4200", "https://192.168.0.37:4200", "https://regalolibre.app", "https://www.regalolibre.app"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

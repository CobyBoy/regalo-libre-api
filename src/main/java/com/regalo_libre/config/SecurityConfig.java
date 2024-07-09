package com.regalo_libre.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(http -> {
                    http.requestMatchers(HttpMethod.POST, "/api/user/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/token/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/lists/user/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/api/lists/public/**").permitAll();
                    http.requestMatchers(new RegexRequestMatcher("/api/profile/[^/]+", "GET")).permitAll();
                    http.anyRequest().authenticated();
                })
                .cors((cors) -> cors.configurationSource(corsConfigurationSource()))
                .oauth2ResourceServer(oauth2ResourceServerCustomizer ->
                        oauth2ResourceServerCustomizer.jwt(jwtCustomizer -> {
                        }))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("https://localhost:4200"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

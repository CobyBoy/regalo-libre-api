package com.regalo_libre.auth.jwt;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.regalo_libre.auth.config.Auth0PropertiesConfig;
import com.regalo_libre.common.dtos.ApiErrorDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Collections;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final Auth0PropertiesConfig auth0PropertiesConfig;
    private static final Duration CLOCK_SKEW = Duration.ofMinutes(5);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = extractAccessToken(request);

        if (accessToken != null) {
            try {
                DecodedJWT jwt = verifyAndDecodeToken(accessToken);
                var subject = jwt.getSubject().split("\\|");
                Long userId = Long.parseLong(subject[subject.length - 1]);
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    SecurityContext context = SecurityContextHolder.createEmptyContext();

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authentication);
                    SecurityContextHolder.setContext(context);
                    log.info("Setting authentication context {}", SecurityContextHolder.getContext());
                }
            } catch (TokenExpiredException e) {
                log.error("Token expired: {}", e.getMessage());
                request.setAttribute("expired", e.getMessage());
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                var r = new ObjectMapper().writeValueAsString(ApiErrorDto.builder()
                        .httpStatus(HttpStatus.FORBIDDEN)
                        .statusCode(HttpStatus.FORBIDDEN.value())
                        .message(e.getMessage())
                        .build());
                log.info("message {}", r);
                response.getWriter().write(r);
                return;
            } catch (JwkException e) {
                log.error("Token validation failed: {}", e.getMessage());
                return;
            }

        }

        filterChain.doFilter(request, response);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean validateAccessToken(String accessToken) {
        // Implement token validation logic here
        // verifying the token with Auth0 or checking its signature
        return true;
    }

    public DecodedJWT verifyAndDecodeToken(String token) throws JWTVerificationException, MalformedURLException, JwkException {
        DecodedJWT jwt = JWT.decode(token);
        JwkProvider provider = new UrlJwkProvider(new URL(auth0PropertiesConfig.getJwksUrl()));
        Jwk jwk = provider.get(jwt.getKeyId());
        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(auth0PropertiesConfig.getIssuer())
                .withAudience(auth0PropertiesConfig.getAudience())
                .acceptLeeway(CLOCK_SKEW.getSeconds())
                .build();

        return verifier.verify(token);
    }

}
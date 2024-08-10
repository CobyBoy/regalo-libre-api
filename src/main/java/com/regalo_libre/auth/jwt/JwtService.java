package com.regalo_libre.auth.jwt;

import com.auth0.jwk.Jwk;
import com.auth0.jwk.JwkException;
import com.auth0.jwk.JwkProvider;
import com.auth0.jwk.UrlJwkProvider;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.regalo_libre.auth.config.OauthPropertiesConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final OauthPropertiesConfig oauthPropertiesConfig;
    private static final Duration CLOCK_SKEW = Duration.ofMinutes(5);

    public OAuthAccessToken setAuthentication(OAuthAccessToken accessToken) throws MalformedURLException, JwkException {

        if (accessToken.getIdToken() != null && accessToken.getAccessToken() != null) {
            DecodedJWT jwt = verifyAndDecodeToken(accessToken.getAccessToken());

            String userId = jwt.getSubject();

            // Create Authentication object
            Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, List.of());

            // Set the authentication in the SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // You can now use the decoded JWT to access claims
            String name = jwt.getClaim("name").asString();
            String email = jwt.getClaim("email").asString();

            return accessToken;
        }
        return accessToken;
    }

    public DecodedJWT verifyAndDecodeToken(String token) throws JWTVerificationException, MalformedURLException, JwkException {
        DecodedJWT jwt = JWT.decode(token);
        JwkProvider provider = new UrlJwkProvider(new URL(oauthPropertiesConfig.getJwksUrl()));
        Jwk jwk = provider.get(jwt.getKeyId());
        Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) jwk.getPublicKey(), null);

        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(oauthPropertiesConfig.getIssuer())
                .withAudience(oauthPropertiesConfig.getAudience())
                .acceptLeeway(CLOCK_SKEW.getSeconds())
                .build();

        return verifier.verify(token);
    }
}

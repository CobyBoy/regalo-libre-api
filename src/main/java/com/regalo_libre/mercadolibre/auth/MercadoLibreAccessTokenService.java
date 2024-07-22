package com.regalo_libre.mercadolibre.auth;

import com.regalo_libre.mercadolibre.auth.model.MercadoLibreAccessToken;
import com.regalo_libre.mercadolibre.auth.repository.MercadoLibreAccessTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MercadoLibreAccessTokenService {
    private final MercadoLibreAccessTokenRepository mercadoLibreAccessTokenRepository;

    public MercadoLibreAccessToken saveAccessToken(MercadoLibreAccessToken accessToken) {
        return mercadoLibreAccessTokenRepository.save(accessToken);
    }
}

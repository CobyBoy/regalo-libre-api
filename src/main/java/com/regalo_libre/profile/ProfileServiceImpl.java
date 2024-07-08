package com.regalo_libre.profile;

import com.regalo_libre.mercadolibre.auth.repository.MercadoLibreUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProfileServiceImpl {
    private final MercadoLibreUserRepository mercadoLibreUserRepository;
    private final ProfileRepository profileRepository;

    Profile find(Long userId) {
        return profileRepository.findById(userId).orElseThrow();
    }
}

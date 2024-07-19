package com.regalo_libre.auth;

import com.regalo_libre.auth.model.OAuthUser;
import com.regalo_libre.auth.repository.OAuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthUserServiceImpl {
    private final OAuthUserRepository repository;

    public OAuthUser createOauthUser(OAuthUserInfo response) {
        return repository.save(OAuthUser.builder()
                .fullStringId(response.sub)
                .name(response.name)
                .nickname(response.nickname)
                .picture(response.picture)
                .updatedAt(response.updatedAt)
                .build());
    }

}

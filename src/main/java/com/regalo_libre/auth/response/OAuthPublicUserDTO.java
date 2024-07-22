package com.regalo_libre.auth.response;

import com.regalo_libre.auth.model.OAuthUser;
import com.regalo_libre.utils.DtoConverter;
import lombok.Builder;

@Builder
public record OAuthPublicUserDTO(
        String nickname
) implements DtoConverter<OAuthUser, OAuthPublicUserDTO> {

    @Override
    public OAuthPublicUserDTO toDto(OAuthUser entity) {
        return OAuthPublicUserDTO.builder()
                .nickname(entity.getNickname())
                .build();
    }
}

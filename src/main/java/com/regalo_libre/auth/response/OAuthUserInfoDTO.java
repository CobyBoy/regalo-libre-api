package com.regalo_libre.auth.response;

import com.regalo_libre.auth.model.OAuthUser;
import com.regalo_libre.utils.DtoConverter;
import lombok.Builder;

@Builder
public record OAuthUserInfoDTO(
        Long id,
        String name,
        String nickname,
        String picture
) implements DtoConverter<OAuthUser, OAuthUserInfoDTO> {
    @Override
    public OAuthUserInfoDTO toDto(OAuthUser entity) {
        return OAuthUserInfoDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .nickname(entity.getNickname())
                .picture(entity.getPictureUrl())
                .build();
    }
}

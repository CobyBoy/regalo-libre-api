package com.regalo_libre.auth.response;

import com.regalo_libre.auth.model.Auth0User;
import com.regalo_libre.utils.DtoConverter;
import lombok.Builder;

@Builder
public record Auth0PublicUserDTO(
        String nickname
) implements DtoConverter<Auth0User, Auth0PublicUserDTO> {

    @Override
    public Auth0PublicUserDTO toDto(Auth0User entity) {
        return Auth0PublicUserDTO.builder()
                .nickname(entity.getNickname())
                .build();
    }
}

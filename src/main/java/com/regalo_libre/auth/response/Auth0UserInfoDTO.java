package com.regalo_libre.auth.response;

import com.regalo_libre.auth.model.Auth0User;
import com.regalo_libre.utils.DtoConverter;
import lombok.Builder;

@Builder
public record Auth0UserInfoDTO(
        Long id,
        String name,
        String nickname,
        String picture
) implements DtoConverter<Auth0User, Auth0UserInfoDTO> {
    @Override
    public Auth0UserInfoDTO toDto(Auth0User entity) {
        return Auth0UserInfoDTO.builder()
                .id(entity.getId())
                .name(entity.getName())
                .nickname(entity.getNickname())
                .picture(entity.getPictureUrl())
                .build();
    }
}

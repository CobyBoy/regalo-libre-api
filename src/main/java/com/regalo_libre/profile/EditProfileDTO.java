package com.regalo_libre.profile;

import com.regalo_libre.utils.DtoConverter;
import lombok.Builder;

@Builder
public record EditProfileDTO(
        Long id,
        String biography,
        String appNickname,
        Boolean isPrivate
) implements DtoConverter<Profile, EditProfileDTO> {
    @Override
    public EditProfileDTO toDto(Profile entity) {
        return EditProfileDTO.builder()
                .id(entity.getUserId())
                .biography(entity.getBiography())
                .appNickname(entity.getAppNickname())
                .isPrivate(entity.getIsPrivate())
                .build();
    }
}

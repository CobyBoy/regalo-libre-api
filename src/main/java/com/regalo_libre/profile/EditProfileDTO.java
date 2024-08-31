package com.regalo_libre.profile;

import com.regalo_libre.utils.DtoConverter;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record EditProfileDTO(
        Long id,
        String biography,
        @Pattern(regexp = "^[a-z0-9_.]+$", message = "Tu nombre de usuario solo puede contener letras, números, guiones bajos y puntos")
        String appNickname,
        Boolean isPrivate
) implements DtoConverter<Profile, EditProfileDTO> {
    @Override
    public EditProfileDTO toDto(Profile entity) {
        return EditProfileDTO.builder()
                .id(entity.getProfileId())
                .biography(entity.getBiography())
                .appNickname(entity.getAppNickname())
                .isPrivate(entity.getIsPrivate())
                .build();
    }
}

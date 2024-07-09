package com.regalo_libre.mercadolibre.auth.model;

import com.regalo_libre.utils.DtoConverter;
import lombok.Builder;

@Builder
public record MercadoLibreUserDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        String nickname
) implements DtoConverter<MercadoLibreUser, MercadoLibreUserDTO> {
    public MercadoLibreUserDTO toDto(MercadoLibreUser user) {
        return MercadoLibreUserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .nickname(user.getNickname())
                .build();
    }
}

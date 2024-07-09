package com.regalo_libre.mercadolibre.auth.model;

import lombok.Builder;

@Builder
public record MercadoLibrePublicUserDTO(
        String nickname
) {
    public static MercadoLibrePublicUserDTO toDto(MercadoLibreUser user) {
        return MercadoLibrePublicUserDTO.builder()
                .nickname(user.getNickname())
                .build();
    }
}

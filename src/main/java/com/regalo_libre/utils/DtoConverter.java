package com.regalo_libre.utils;

public interface DtoConverter<U, T> {
    T toDto(U entity);
}

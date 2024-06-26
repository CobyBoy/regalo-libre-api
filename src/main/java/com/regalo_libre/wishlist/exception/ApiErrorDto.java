package com.regalo_libre.wishlist.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Value
public class ApiErrorDto {
    @NonNull
    HttpStatus httpStatus;
    @NonNull
    Integer statusCode;
    @NonNull
    String message;
    List<String> additionalInfo;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    LocalDateTime timestamp = LocalDateTime.now();
}

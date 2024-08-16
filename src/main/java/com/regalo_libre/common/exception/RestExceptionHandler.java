package com.regalo_libre.common.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.regalo_libre.common.dtos.ApiErrorDto;
import com.regalo_libre.mercadolibre.auth.exception.TokenNotFoundException;
import com.regalo_libre.profile.ProfileNotPublicException;
import com.regalo_libre.wishlist.exception.GiftAlreadyInWishlistException;
import com.regalo_libre.wishlist.exception.PublicWishListNotFoundException;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.file.AccessDeniedException;

/**
 * Handles exceptions thrown by controllers
 */
@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler {
    private static final String TOKEN_NOT_FOUND = "Token not found";

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleTokenNotFoundException(TokenNotFoundException ex) {
        log.error(TOKEN_NOT_FOUND);
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(ServletException.class)
    public ResponseEntity<ApiErrorDto> handleTokenNotFoundException(ServletException ex) {
        log.error(TOKEN_NOT_FOUND);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorDto> handleTokenNotFoundException(AccessDeniedException ex) {
        log.error(TOKEN_NOT_FOUND);
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ApiErrorDto> handleForbiddenException(HttpClientErrorException ex) {
        log.error(ex.getMessage());
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ApiErrorDto> handleTokenNotFoundException(IllegalStateException ex) {
        log.error(TOKEN_NOT_FOUND);
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(PublicWishListNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleTokenNotFoundException(PublicWishListNotFoundException ex) {
        log.error(TOKEN_NOT_FOUND);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(ProfileNotPublicException.class)
    public ResponseEntity<ApiErrorDto> handleTokenNotFoundException(ProfileNotPublicException ex) {
        log.error(TOKEN_NOT_FOUND);
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleTokenNotFoundException(UsernameNotFoundException ex) {
        log.error("User name not found");
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(GiftAlreadyInWishlistException.class)
    public ResponseEntity<ApiErrorDto> handleGiftAlreadyInWishlistException(GiftAlreadyInWishlistException ex) {
        log.error("Gift already in wishlist");
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ApiErrorDto> handleTokenExpiredException(TokenExpiredException ex) {
        log.error("Token expired");
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }
}

package com.regalo_libre.common.exception;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.regalo_libre.common.dtos.ApiErrorDto;
import com.regalo_libre.login.exception.Auth0TokenIsUndefinedException;
import com.regalo_libre.mercadolibre.auth.exception.TokenNotFoundException;
import com.regalo_libre.mercadolibre.auth.exception.UnableToSaveMercadoLibreAccessTokenException;
import com.regalo_libre.profile.exception.ProfileNotPublicException;
import com.regalo_libre.profile.exception.ProfileNicknameAlreadyExists;
import com.regalo_libre.wishlist.exception.*;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
    public ResponseEntity<ApiErrorDto> handleServletException(ServletException ex) {
        log.error(ex.getMessage());
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiErrorDto> handleAccessDeniedException(AccessDeniedException ex) {
        log.error(ex.getMessage());
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
    public ResponseEntity<ApiErrorDto> handleIllegalStateException(IllegalStateException ex) {
        log.error("Illegal exception");
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(PublicWishListNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handlePublicWishListNotFoundException(PublicWishListNotFoundException ex) {
        log.error("Lista publica no encontrada");
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(WishlistNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleWishListNotFoundException(WishlistNotFoundException ex) {
        log.error("Lista no encontrada");
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(ProfileNotPublicException.class)
    public ResponseEntity<ApiErrorDto> handleProfileNotPublicException(ProfileNotPublicException ex) {
        log.error("Perfil publico no encontrada");
        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiErrorDto> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        log.error("User name not found");
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
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

    @ExceptionHandler(UnableToDeleteWishlistException.class)
    public ResponseEntity<ApiErrorDto> handleUnableToDeleteListException(UnableToDeleteWishlistException ex) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());

    }

    @ExceptionHandler(WishlistWithSameNameAlreadyExistsException.class)
    public ResponseEntity<ApiErrorDto> handleWishlistWithSameNameAlreadyExistsException(WishlistWithSameNameAlreadyExistsException ex) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(ProfileNicknameAlreadyExists.class)
    public ResponseEntity<ApiErrorDto> handleProfileNicknameAlreadyExistsException(ProfileNicknameAlreadyExists ex) {
        HttpStatus httpStatus = HttpStatus.CONFLICT;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(Auth0TokenIsUndefinedException.class)
    public ResponseEntity<ApiErrorDto> handleAuth0TokenIsUndefinedException(Auth0TokenIsUndefinedException ex) {
        HttpStatus httpStatus = HttpStatus.FORBIDDEN;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(UnableToSaveMercadoLibreAccessTokenException.class)
    public ResponseEntity<ApiErrorDto> handleUnableToSaveMercadoLibreAccessTokenException(UnableToSaveMercadoLibreAccessTokenException ex) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getBindingResult().getFieldError().getDefaultMessage())
                        .build());
    }

    @ExceptionHandler(JwkProviderException.class)
    public ResponseEntity<ApiErrorDto> handleJwkProviderException(JwkProviderException ex) {
        HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
        return ResponseEntity.status(httpStatus)
                .body(ApiErrorDto.builder()
                        .httpStatus(httpStatus)
                        .statusCode(httpStatus.value())
                        .message(ex.getMessage())
                        .build());
    }
}

package com.honghung.chatapp.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
public class SuccessResponseEntity<T> extends ResponseEntity<SuccessResponseEntity.Payload<T>> {
    public static <T> SuccessResponseEntity<T> from(HttpStatus status, String message) {
        return new SuccessResponseEntity<>(status, message, null, null);
    }

    public static <T> SuccessResponseEntity<T> from(HttpStatus status, String message, T data) {
        return new SuccessResponseEntity<T>(status, message, data, null);
    }

    public static <T> SuccessResponseEntity<T> from(
            HttpStatus status, String message, T data, Object others) {
        return new SuccessResponseEntity<T>(status, message, data, others);
    } 

    public SuccessResponseEntity(HttpStatus status, String message, T data, Object others) {
        super(
                Payload.<T>builder()
                        .statusCode(status.value())
                        .message(message)
                        .data(data)
                        .others(others)
                        .build(),
                status);
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Payload<T> {
        private int statusCode;
        private String message;
        private T data;
        private Object others;
    }
}
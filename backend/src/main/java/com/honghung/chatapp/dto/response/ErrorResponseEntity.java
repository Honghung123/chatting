package com.honghung.chatapp.dto.response; 

import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ErrorResponseEntity extends ResponseEntity<ErrorResponseEntity.Payload>{

    public ErrorResponseEntity(Payload payload) {
        super(payload, HttpStatus.valueOf(payload.getStatusCode()));
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Payload {
        private int statusCode;
        private String message;
        private Date timestamp;
        private String path;
        private String errorCode;
    }
}

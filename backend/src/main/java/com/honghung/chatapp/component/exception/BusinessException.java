package com.honghung.chatapp.component.exception; 

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String code;
    private final String message;

    public BusinessException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
    public static BusinessException from(ExceptionType ex) {
        return new BusinessException(ex.getCode(), ex.getDefaultMessage());
    }

    public static BusinessException from(ExceptionType ex, String message) {
        return new BusinessException(ex.getCode(), message);
    }
}

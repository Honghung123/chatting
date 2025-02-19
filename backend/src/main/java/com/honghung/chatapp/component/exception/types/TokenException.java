package com.honghung.chatapp.component.exception.types;

import com.honghung.chatapp.component.exception.ExceptionType;

public enum TokenException implements ExceptionType {
    INVALID_TOKEN("invalid_token", "Invalid token"),
    EXPIRED_TOKEN("expired_token", "Expired token"),
    ERROR_PARSING_TOKEN("error_parsing_token", "An error occurred while parsing token"),
    ERROR_GENERATING_TOKEN("error_generating_token", "An error occurred while generating token");
    private final String code;
    private final String defaultMessage;

    TokenException(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getDefaultMessage() {
        return defaultMessage;
    }
}

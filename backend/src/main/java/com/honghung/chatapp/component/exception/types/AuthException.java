package com.honghung.chatapp.component.exception.types;

import com.honghung.chatapp.component.exception.ExceptionType;

public enum AuthException implements ExceptionType {
    INVALID_CREDENTIALS("invalid_credentials", "Invalid credentials"),
    INVALID_TOKEN("invalid_token", "Invalid token"),
    TOKEN_EXPIRED("at_token_expired", "Token expired"),
    TOKEN_NOT_FOUND("token_not_found", "Token not found"),
    UNAUTHORIZED("unauthorized", "Unauthorized"),
    PASSWORD_NOT_MATCH("password_not_match", "Password not match"),
    NOT_A_TOKEN("not_a_token", "Not a valid token"),
    ;

    private final String code;
    private final String defaultMessage;

    AuthException(String code, String defaultMessage) {
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

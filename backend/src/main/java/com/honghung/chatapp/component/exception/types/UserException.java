package com.honghung.chatapp.component.exception.types;

import com.honghung.chatapp.component.exception.ExceptionType;

public enum UserException implements ExceptionType {
    USER_NOT_FOUND("user_not_found", "User Not Found"),
    USER_NOT_ACTIVATED("user_not_activated", "User not activated"),
    USER_DEACTIVATED("user_deactivated", "User deactivated"),
    EMAIL_ALREADY_EXISTS("email_already_exists", "Email already exists"), EMAIL_DOES_NOT_EXISTS("email_does_not_exists", "Email does not exists"), PASSWORD_NOT_MATCH("password_not_match", "Password not match"),
    ;
    private final String code;
    private final String defaultMessage;

    UserException(String code, String defaultMessage) {
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

package com.honghung.chatapp.component.exception.types;

import com.honghung.chatapp.component.exception.ExceptionType;

public enum NotificationException implements ExceptionType{
    NOTIFICATION_NOT_FOUND("notification_not_found", "Notification not found"),
    ;

    private final String code;
    private final String defaultMessage;
    NotificationException(String code, String defaultMessage) {
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

package com.honghung.chatapp.component.exception.types;

import com.honghung.chatapp.component.exception.ExceptionType;

public enum FileException implements ExceptionType { 
    EMPTY_FILE("empty_file", "File is empty"),
    FILE_NOT_FOUND("file_not_found", "File not found"),
    FILE_TOO_LARGE("file_too_large", "File size is too large"),
    NOT_IMAGE_FILE("not_image_file", "File is not image file")
    ;
    private final String code;
    private final String defaultMessage;

    FileException(String code, String defaultMessage) {
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

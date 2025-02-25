package com.honghung.chatapp.component.exception.types;

import com.honghung.chatapp.component.exception.ExceptionType;

public enum PostException implements ExceptionType{
    POST_NOT_FOUND("post_not_found", "Post not found"),
    ;
    private final String code;
    private final String defaultMessage;
    PostException(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getDefaultMessage() {
        return defaultMessage;
    }
    
}

package com.honghung.chatapp.constant;

public enum MessageContentType {
    TEXT("TEXT"),
    IMAGE("IMAGE"),
    AUDIO("AUDIO"),
    VIDEO("VIDEO"),
    FILE("FILE");

    private final String name;

    MessageContentType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

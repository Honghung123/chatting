package com.honghung.chatapp.constant;

public enum ConversationType {
    SELF("SELF"), 
    DIRECT("DIRECT"),
    GROUP("GROUP");

    private final String name;
    ConversationType(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

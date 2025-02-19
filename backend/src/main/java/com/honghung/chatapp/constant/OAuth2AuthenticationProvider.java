package com.honghung.chatapp.constant;

public enum OAuth2AuthenticationProvider {
    LOCAL("local"),
    FACEBOOK("facebook"),
    GOOGLE("google"),
    GITHUB("github");

    private final String name;
    OAuth2AuthenticationProvider(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public static OAuth2AuthenticationProvider of(String name) {
        for (OAuth2AuthenticationProvider provider : values()) {
            if (provider.getName().equals(name)) {
                return provider;
            }
        }
        return null;
    }
}

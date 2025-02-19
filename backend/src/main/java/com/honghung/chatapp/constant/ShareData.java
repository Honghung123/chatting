package com.honghung.chatapp.constant;

import java.util.List;

public final class ShareData {
    private ShareData() {
    } 

    public static final List<String> BYPASS_URLS = List.of(
            "/h2-console/**",
            "/auth/**", "/auth",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/swagger-ui.html",
            "/api-docs/**",
            "/swagger-resources/**",
            "/file/**",
            "/ws/**",
            "/websocket/**",
            "/app/**",
            "/topic/**",
            "/notification/**",
            "/post/get-all"
            );
}

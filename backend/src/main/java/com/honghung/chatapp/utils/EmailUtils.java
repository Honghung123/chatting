package com.honghung.chatapp.utils;

public final class EmailUtils {
    public static String extractUsernameFromEmail(String email) {
        if (email != null && email.contains("@")) {
            return email.split("@")[0];
        }
        else return null;
    }

    private EmailUtils() {
    }
}

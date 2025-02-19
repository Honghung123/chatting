package com.honghung.chatapp.utils;

import java.util.Random;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public final class PasswordUtils {
    private static PasswordEncoder passwordEncoder = null;

    public PasswordUtils(PasswordEncoder passwordEncoder) {
        PasswordUtils.passwordEncoder = passwordEncoder;
    }

    public static String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public static String generateRandomPassword() {
        return generateRandomPassword(10);
    }

    public static String generateRandomPassword(int passwordLength) {
        return alphaNumericString(passwordLength);
    }

    public static String alphaNumericString(int len) {
        String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random rnd = new Random();

        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            sb.append(AB.charAt(rnd.nextInt(AB.length())));
        }
        return sb.toString();
    }
}
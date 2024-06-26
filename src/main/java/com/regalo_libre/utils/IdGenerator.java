package com.regalo_libre.utils;

import java.security.SecureRandom;
import java.util.Random;

public class IdGenerator {

    private static final String ALPHANUMERIC_CHARACTERS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int ID_LENGTH = 8;  // Total length without the dash

    private static final Random RANDOM = new SecureRandom();

    public static String generatePublicId() {
        StringBuilder sb = new StringBuilder(ID_LENGTH + 1);  // +1 for the dash
        for (int i = 0; i < ID_LENGTH; i++) {
            sb.append(ALPHANUMERIC_CHARACTERS.charAt(RANDOM.nextInt(ALPHANUMERIC_CHARACTERS.length())));
            if (i == 4) {
                sb.append('-');  // Add dash after the 5th character
            }
        }
        return sb.toString();
    }

    public static String generatePrivateId() {
        StringBuilder sb = new StringBuilder(ID_LENGTH);
        for (int i = 0; i < ID_LENGTH; i++) {
            sb.append(ALPHANUMERIC_CHARACTERS.charAt(RANDOM.nextInt(ALPHANUMERIC_CHARACTERS.length())));
        }
        return sb.toString();
    }
}

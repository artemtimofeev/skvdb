package org.skvdb.util;

import java.security.SecureRandom;

public class RandomStringGenerator {
    private final String CHARACTERS = "abcdefghijklmnopqrstuvwxyz123456789";

    public String generateRandomString(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(randomIndex));
        }
        return sb.toString();
    }
}

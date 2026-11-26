package org.scalableurlshortener.util;

import java.security.SecureRandom;

public final class Base62Generator {

    private static final String BASE62_CHARACTERS =
            "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static final int DEFAULT_LENGTH = 7;

    private static final SecureRandom RANDOM = new SecureRandom();

    private Base62Generator() {
        // Prevent object creation
    }

    public static String generate() {
        return generate(DEFAULT_LENGTH);
    }

    public static String generate(int length) {

        if (length <= 0) {
            throw new IllegalArgumentException(
                    "Length must be greater than zero"
            );
        }

        StringBuilder shortCode = new StringBuilder(length);

        for (int i = 0; i < length; i++) {

            int index = RANDOM.nextInt(BASE62_CHARACTERS.length());

            shortCode.append(BASE62_CHARACTERS.charAt(index));
        }

        return shortCode.toString();
    }
}
package com.harvey.common.support;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomStringGenerator {
    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    
    private static final String DIGITS = "0123456789";
    
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_=+[]{}|;:'\",.<>?/`~";
    
    private static final Random RANDOM = new SecureRandom();
    
    public static String gen(int length) {
        return gen(length, true, true, true, true);
    }

    public static String gen(int length, boolean isUseUpper, boolean isUseLower, boolean isUseDigits, boolean isUseSpecialCharacters) {
        if (length <= 0) {
            throw new IllegalArgumentException("Length must be greater than 0");
        }

        List<String> charCategories = new ArrayList<>();
        if (isUseUpper) {
            charCategories.add(UPPERCASE);
        }
        if (isUseLower) {
            charCategories.add(LOWERCASE);
        }
        if (isUseDigits) {
            charCategories.add(DIGITS);
        }
        if (isUseSpecialCharacters) {
            charCategories.add(SPECIAL_CHARACTERS);
        }

        if (charCategories.isEmpty()) {
            throw new IllegalArgumentException("At least one character set (upper, lower, digits, special) must be true.");
        }

        StringBuilder result = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            String charCategory = charCategories.get(RANDOM.nextInt(charCategories.size()));
            int position = RANDOM.nextInt(charCategory.length());
            result.append(charCategory.charAt(position));
        }

        return result.toString();
    }
}
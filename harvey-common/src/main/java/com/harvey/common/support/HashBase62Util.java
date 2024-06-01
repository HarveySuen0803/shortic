package com.harvey.common.support;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class HashBase62Util {
    private static final char[] BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
    
    private static final SecureRandom RANDOM = new SecureRandom();

    private static byte[] toHash(String str) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            return messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private static String toBase62(byte[] hash) {
        BigInteger num = new BigInteger(1, hash);
        StringBuilder base62Sb = new StringBuilder();

        while (num.compareTo(BigInteger.ZERO) > 0) {
            int idx = num.mod(BigInteger.valueOf(62)).intValue();
            base62Sb.append(BASE62_CHARS[idx]);
            num = num.divide(BigInteger.valueOf(62));
        }

        return base62Sb.reverse().toString();
    }

    public static String toBase62(String str, int len) {
        byte[] hash = toHash(str);
        
        String base62 = toBase62(hash);
        
        if (base62.length() < len) {
            StringBuilder base62Sb = new StringBuilder(base62);
            while (base62Sb.length() < len) {
                base62Sb.append(BASE62_CHARS[RANDOM.nextInt(BASE62_CHARS.length)]);
            }
            base62 = base62Sb.toString();
        } else {
            base62 = base62.substring(0, 6);
        }
        
        return base62;
    }
}
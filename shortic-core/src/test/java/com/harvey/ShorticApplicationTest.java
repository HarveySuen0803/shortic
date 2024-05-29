package com.harvey;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-23
 */
@SpringBootTest
public class ShorticApplicationTest {
    @Test
    public void test() throws NoSuchAlgorithmException {
        // String str = HashBase62Util.toBase62("hello world");
        // System.out.println(str);
        
        // String str = "hello world";
        // System.out.println(Arrays.toString(str.getBytes()));
        //
        // MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        // byte[] digest = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
        // System.out.println(Arrays.toString(digest));
        
        String str = "abc";
        System.out.println(Arrays.toString(str.getBytes()));
        
        System.out.println();
    }
}
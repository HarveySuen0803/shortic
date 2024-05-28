package com.harvey;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-23
 */
@SpringBootTest
public class ShorticApplicationTest {
    @Resource
    private PasswordEncoder passwordEncoder;

    @Test
    public void test() {
        String encode = passwordEncoder.encode("123456");
        System.out.println(encode);
    }
}
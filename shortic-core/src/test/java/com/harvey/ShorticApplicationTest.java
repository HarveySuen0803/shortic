package com.harvey;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-23
 */
@SpringBootTest
public class ShorticApplicationTest {
    @Test
    public void test01() {
        String str = "harveysuen";
        
        String[] splits = str.split("@");
        System.out.println(splits);
        System.out.println(splits.length);
    }
}

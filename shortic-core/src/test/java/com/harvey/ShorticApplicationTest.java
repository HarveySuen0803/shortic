package com.harvey;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-23
 */
@SpringBootTest
public class ShorticApplicationTest {
    @Test
    public void test01() {
        List<String> strList = new LinkedList<>();
        strList.add("111");
        strList.add("222");
        strList.add("333");
        strList.add("444");
        
        
        for (String str : strList) {
            if (str.equals("111")) {
                strList.remove("222");
                strList.add("2222");
            }
        }
        
        System.out.println(strList);
    }
}

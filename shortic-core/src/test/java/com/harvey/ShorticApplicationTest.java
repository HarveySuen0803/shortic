package com.harvey;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.harvey.user.domain.UserDo;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
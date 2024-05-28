package com.harvey.user.serializer;

import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.reader.ObjectReader;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-26
 */
public class GrantedAuthorityListDeserializer implements ObjectReader<List<GrantedAuthority>> {
    @Override
    public List<GrantedAuthority> readObject(JSONReader jsonReader, Type type, Object o, long l) {
        List<String> authStrList = jsonReader.readArray(String.class);
        return AuthorityUtils.createAuthorityList(authStrList);
    }
}
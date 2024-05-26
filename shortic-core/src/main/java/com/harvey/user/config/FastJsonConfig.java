package com.harvey.user.config;

import com.alibaba.fastjson2.JSONFactory;
import com.alibaba.fastjson2.TypeReference;
import com.alibaba.fastjson2.reader.ObjectReaderProvider;
import com.harvey.user.serializer.GrantedAuthorityListDeserializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-26
 */
@Configuration
@ConditionalOnClass({JSONFactory.class})
public class FastJsonConfig {
    @Bean
    @ConditionalOnMissingBean
    public ObjectReaderProvider objectReaderProvider() {
        ObjectReaderProvider readerProvider = JSONFactory.getDefaultObjectReaderProvider();
        readerProvider.register(new TypeReference<List<GrantedAuthority>>() {}.getType(), new GrantedAuthorityListDeserializer());
        return readerProvider;
    }
}

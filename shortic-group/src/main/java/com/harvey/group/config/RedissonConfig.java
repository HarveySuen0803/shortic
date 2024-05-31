package com.harvey.group.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-24
 */
@Configuration
public class RedissonConfig {
    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        config.useSingleServer()
            .setAddress("redis://127.0.0.1:6379")
            .setPassword("111");
        return Redisson.create(config);
    }
}

package com.harvey.user.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-27
 */
@Configuration
public class UserBloomFilterConfig {
    @Bean
    public RBloomFilter userBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("user:bloomfilter:use_id");
        bloomFilter.tryInit(100000, 0.01);
        return bloomFilter;
    }
}

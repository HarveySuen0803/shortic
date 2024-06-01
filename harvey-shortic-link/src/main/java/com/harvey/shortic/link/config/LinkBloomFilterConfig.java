package com.harvey.shortic.link.config;

import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author harvey
 * @Email harveysuen0803@gmail.com
 * @Date 2024-05-29
 */
@Configuration
public class LinkBloomFilterConfig {
    @Bean
    public RBloomFilter shortUriBloomFilter(RedissonClient redissonClient) {
        RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("shortic:link:bloomfilter:short_uri");
        bloomFilter.tryInit(10000, 0.01);
        return bloomFilter;
    }
}

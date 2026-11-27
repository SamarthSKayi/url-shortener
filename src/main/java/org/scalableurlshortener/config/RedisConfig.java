package org.scalableurlshortener.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class RedisConfig {

    @Bean
    CommandLineRunner redisConnectionTest(StringRedisTemplate redisTemplate) {
        return args -> {
            redisTemplate.opsForValue().set("url-shortener:test", "connected");

            String value = redisTemplate.opsForValue()
                    .get("url-shortener:test");

            System.out.println("Redis connection test: " + value);
        };
    }
}
package org.scalableurlshortener.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.scalableurlshortener.dto.CachedUrl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public RedisService(
            RedisTemplate<String, String> redisTemplate,
            ObjectMapper objectMapper
    ) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public void set(String key, CachedUrl value, long ttl) {
        try {
            String json = objectMapper.writeValueAsString(value);

            redisTemplate.opsForValue().set(
                    key,
                    json,
                    Duration.ofSeconds(ttl)
            );
        } catch (Exception exception) {
            throw new RuntimeException("Unable to cache URL", exception);
        }
    }

    public CachedUrl get(String key) {
        try {
            String json = redisTemplate.opsForValue().get(key);

            if (json == null) {
                return null;
            }

            return objectMapper.readValue(json, CachedUrl.class);

        } catch (Exception exception) {
            throw new RuntimeException("Unable to retrieve cached URL", exception);
        }
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }
}
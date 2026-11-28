package org.scalableurlshortener.service;

import org.scalableurlshortener.dto.CachedUrl;
import org.scalableurlshortener.dto.UrlAnalyticsResponse;
import org.scalableurlshortener.entity.Url;
import org.scalableurlshortener.exception.ShortCodeGenerationException;
import org.scalableurlshortener.exception.UrlNotFoundException;
import org.scalableurlshortener.repository.UrlRepository;
import org.scalableurlshortener.util.Base62Generator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;


@Service
public class UrlService {

    private final RedisService redisService;
    private final long defaultTtl;
    private final UrlRepository urlRepository;

    public UrlService(
            UrlRepository urlRepository,
            RedisService redisService,
            @Value("${app.redis.default-ttl:3600}") long defaultTtl
    ) {
        this.urlRepository = urlRepository;
        this.redisService = redisService;
        this.defaultTtl = defaultTtl;
    }

    private static final int MAX_GENERATION_ATTEMPTS = 5;

    public Url createShortUrl(String originalUrl) {

        for (int attempt = 1; attempt <= MAX_GENERATION_ATTEMPTS; attempt++) {

            String shortCode = Base62Generator.generate();

            if (urlRepository.existsByShortCode(shortCode)) {
                continue;
            }

            Url url = new Url();
            url.setOriginalUrl(originalUrl);
            url.setShortCode(shortCode);

            return urlRepository.save(url);
        }

        throw new ShortCodeGenerationException(
                "Unable to generate a unique short code after multiple attempts"
        );
    }

    public Url getUrlByShortCode(String shortCode) {

        String cacheKey = "url:" + shortCode;

        // 1. Check Redis
        CachedUrl cachedUrl = redisService.get(cacheKey);

        if (cachedUrl != null) {

            validateCachedUrl(cachedUrl, cacheKey);

            // Reconstruct a lightweight Url object for redirecting
            Url url = new Url();
            url.setShortCode(shortCode);
            url.setOriginalUrl(cachedUrl.getOriginalUrl());
            url.setExpiresAt(cachedUrl.getExpiresAt());
            url.setIsActive(cachedUrl.getIsActive());

            return url;
        }

        // 2. Cache miss: query PostgreSQL
        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new UrlNotFoundException("Short URL not found")
                );

        // 3. Validate lifecycle
        if (!Boolean.TRUE.equals(url.getIsActive())) {
            throw new UrlNotFoundException("This short URL is inactive");
        }

        if (url.getExpiresAt() != null &&
                !url.getExpiresAt().isAfter(Instant.now())) {
            throw new UrlNotFoundException("This short URL has expired");
        }

        // 4. Cache valid URL
        long ttl = defaultTtl;

        if (url.getExpiresAt() != null) {
            long remainingSeconds = Duration.between(
                    Instant.now(), url.getExpiresAt()
            ).getSeconds();

            ttl = Math.min(ttl, remainingSeconds);
        }

        if (ttl > 0) {
            redisService.set(
                    cacheKey,
                    new CachedUrl(
                            url.getOriginalUrl(),
                            url.getExpiresAt(),
                            url.getIsActive()
                    ),
                    ttl
            );
        }

        return url;
    }

    private void validateCachedUrl(CachedUrl cachedUrl, String cacheKey) {

        if (!Boolean.TRUE.equals(cachedUrl.getIsActive())) {
            redisService.delete(cacheKey);
            throw new UrlNotFoundException("This short URL is inactive");
        }

        if (cachedUrl.getExpiresAt() != null &&
                !cachedUrl.getExpiresAt().isAfter(Instant.now())) {
            redisService.delete(cacheKey);
            throw new UrlNotFoundException("This short URL has expired");
        }
    }


    public void incrementClickCount(String shortCode) {
        urlRepository.incrementClickCount(shortCode);
    }

    public UrlAnalyticsResponse getAnalytics(String shortCode) {

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new UrlNotFoundException("Short URL not found")
                );

        return new UrlAnalyticsResponse(
                url.getShortCode(),
                url.getOriginalUrl(),
                url.getClickCount()
        );
    }

}
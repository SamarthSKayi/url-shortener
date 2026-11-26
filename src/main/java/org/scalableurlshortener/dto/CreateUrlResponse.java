package org.scalableurlshortener.dto;

import java.time.Instant;
import java.util.UUID;

public class CreateUrlResponse {

    private UUID id;
    private String shortCode;
    private String shortUrl;
    private String originalUrl;
    private Instant createdAt;
    private Instant expiresAt;

    public CreateUrlResponse(
            UUID id,
            String shortCode,
            String shortUrl,
            String originalUrl,
            Instant createdAt,
            Instant expiresAt
    ) {
        this.id = id;
        this.shortCode = shortCode;
        this.shortUrl = shortUrl;
        this.originalUrl = originalUrl;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
    }

    public UUID getId() {
        return id;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
package org.scalableurlshortener.dto;

import java.time.Instant;

public class CachedUrl {

    private String originalUrl;
    private Instant expiresAt;
    private Boolean isActive;

    public CachedUrl() {}

    public CachedUrl(String originalUrl, Instant expiresAt, Boolean isActive) {
        this.originalUrl = originalUrl;
        this.expiresAt = expiresAt;
        this.isActive = isActive;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean active) {
        isActive = active;
    }
}
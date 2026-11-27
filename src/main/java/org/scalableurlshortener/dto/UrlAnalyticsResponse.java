package org.scalableurlshortener.dto;

public class UrlAnalyticsResponse {

    private String shortCode;
    private String originalUrl;
    private Long clickCount;

    public UrlAnalyticsResponse(
            String shortCode,
            String originalUrl,
            Long clickCount
    ) {
        this.shortCode = shortCode;
        this.originalUrl = originalUrl;
        this.clickCount = clickCount;
    }

    public String getShortCode() {
        return shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public Long getClickCount() {
        return clickCount;
    }
}
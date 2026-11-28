package org.scalableurlshortener.controller;

import jakarta.validation.Valid;
import org.scalableurlshortener.dto.CreateUrlRequest;
import org.scalableurlshortener.dto.CreateUrlResponse;
import org.scalableurlshortener.dto.UrlAnalyticsResponse;
import org.scalableurlshortener.entity.Url;
import org.scalableurlshortener.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.scalableurlshortener.repository.UrlRepository;


@RestController
@RequestMapping("/api/v1/urls")
public class UrlController {

    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateUrlResponse createShortUrl(
            @Valid @RequestBody CreateUrlRequest request
    ) {

        Url url = urlService.createShortUrl(request.getOriginalUrl());

        String shortUrl = "http://localhost:8080/" + url.getShortCode();

        return new CreateUrlResponse(
                url.getId(),
                url.getShortCode(),
                shortUrl,
                url.getOriginalUrl(),
                url.getCreatedAt(),
                url.getExpiresAt()
        );
    }

    @GetMapping("/{shortCode}/analytics")
    public UrlAnalyticsResponse getAnalytics(
            @PathVariable String shortCode
    ) {
        return urlService.getAnalytics(shortCode);
    }



}
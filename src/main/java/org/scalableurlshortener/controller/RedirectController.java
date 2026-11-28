package org.scalableurlshortener.controller;

import org.scalableurlshortener.entity.Url;
import org.scalableurlshortener.service.UrlService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class RedirectController {

    private final UrlService urlService;

    public RedirectController(UrlService urlService) {
        this.urlService = urlService;
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortCode) {

        Url url = urlService.getUrlByShortCode(shortCode);

        URI location = URI.create(url.getOriginalUrl());

        // Increment only after successfully resolving the URL
        urlService.incrementClickCount(shortCode);

        return ResponseEntity
                .status(HttpStatus.FOUND)
                .location(location)
                .build();
    }


}
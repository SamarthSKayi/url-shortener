package org.scalableurlshortener.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.scalableurlshortener.entity.Url;
import org.scalableurlshortener.repository.UrlRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @Mock
    private RedisService redisService;

    private UrlService urlService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        urlService = new UrlService(
                urlRepository,
                redisService,
                3600
        );
    }

    @Test
    void shouldCreateShortUrl() {

        String originalUrl = "https://www.google.com";

        when(urlRepository.existsByShortCode(anyString()))
                .thenReturn(false);

        when(urlRepository.save(any(Url.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Url result = urlService.createShortUrl(originalUrl);

        assertNotNull(result);
        assertEquals(originalUrl, result.getOriginalUrl());
        assertNotNull(result.getShortCode());
        assertEquals(7, result.getShortCode().length());

        verify(urlRepository).save(any(Url.class));
    }
}
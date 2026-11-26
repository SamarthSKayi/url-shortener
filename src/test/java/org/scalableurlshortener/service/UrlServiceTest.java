package org.scalableurlshortener.service;

import org.junit.jupiter.api.Test;
import org.scalableurlshortener.entity.Url;
import org.scalableurlshortener.repository.UrlRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UrlServiceTest {

    @Mock
    private UrlRepository urlRepository;

    @InjectMocks
    private UrlService urlService;

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


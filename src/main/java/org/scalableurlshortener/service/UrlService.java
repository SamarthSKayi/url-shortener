package org.scalableurlshortener.service;

import org.scalableurlshortener.entity.Url;
import org.scalableurlshortener.exception.ShortCodeGenerationException;
import org.scalableurlshortener.exception.UrlNotFoundException;
import org.scalableurlshortener.repository.UrlRepository;
import org.scalableurlshortener.util.Base62Generator;
import org.springframework.stereotype.Service;

import java.time.Instant;


@Service
public class UrlService {

    private final UrlRepository urlRepository;

    public UrlService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
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

        Url url = urlRepository.findByShortCode(shortCode)
                .orElseThrow(() ->
                        new UrlNotFoundException("Short URL not found")
                );

        if (!Boolean.TRUE.equals(url.getIsActive())) {
            throw new UrlNotFoundException("This short URL is inactive");
        }

        if (url.getExpiresAt() != null &&
                !url.getExpiresAt().isAfter(Instant.now())) {
            throw new UrlNotFoundException("This short URL has expired");
        }

        return url;
    }
}
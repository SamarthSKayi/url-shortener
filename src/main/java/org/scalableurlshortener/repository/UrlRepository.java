package org.scalableurlshortener.repository;

import org.scalableurlshortener.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

public interface UrlRepository extends JpaRepository<Url, UUID> {

    Optional<Url> findByShortCode(String shortCode);

    boolean existsByShortCode(String shortCode);

    @Modifying
    @Transactional
    @Query("""
       UPDATE Url u
       SET u.clickCount = u.clickCount + 1
       WHERE u.shortCode = :shortCode
       """)
    int incrementClickCount(@Param("shortCode") String shortCode);
}


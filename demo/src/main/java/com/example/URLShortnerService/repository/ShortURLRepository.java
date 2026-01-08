package com.example.URLShortnerService.repository;

import com.example.URLShortnerService.entity.ShortURL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShortURLRepository extends JpaRepository<ShortURL, Long> {

    Optional<ShortURL> findByShortCode(String shortCode);

    Optional<ShortURL> findByOriginalURL(String originalURL);
}

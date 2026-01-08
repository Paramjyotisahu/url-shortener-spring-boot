package com.example.URLShortnerService.service;

import com.example.URLShortnerService.entity.ShortURL;
import com.example.URLShortnerService.repository.ShortURLRepository;
import com.example.URLShortnerService.util.ShortCodeGenerator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
public class URLShortnerService {

    private final ShortURLRepository shortURLRepository;
    private final ShortCodeGenerator shortCodeGenerator;
    private static final int MAX_COLLISION_RETRIES = 10;

    public URLShortnerService(ShortURLRepository shortURLRepository, ShortCodeGenerator shortCodeGenerator) {
        this.shortURLRepository = shortURLRepository;
        this.shortCodeGenerator = shortCodeGenerator;
    }

    public ShortURL createShortURL(String originalURL) {
        // Check if URL already exists
        Optional<ShortURL> existingShortURL = shortURLRepository.findByOriginalURL(originalURL);
        if (existingShortURL.isPresent()) {
            ShortURL existing = existingShortURL.get();
            // Check if expired
            if (existing.getExpiresAt() == null || existing.getExpiresAt().isAfter(LocalDateTime.now())) {
                return existing;
            }
        }

        // Generate unique code with collision handling
        String shortCode = generateUniqueShortCode();

        // Create and save new ShortURL
        ShortURL shortURL = new ShortURL();
        shortURL.setShortCode(shortCode);
        shortURL.setOriginalURL(originalURL);

        return shortURLRepository.save(shortURL);
    }

    public String getOriginalURL(String shortCode) {
        Optional<ShortURL> shortURL = shortURLRepository.findByShortCode(shortCode);
        
        if (shortURL.isEmpty()) {
            return null;
        }

        ShortURL found = shortURL.get();
        
        // Check if expired
        if (found.getExpiresAt() != null && found.getExpiresAt().isBefore(LocalDateTime.now())) {
            return null;
        }

        return found.getOriginalURL();
    }

    private String generateUniqueShortCode() {
        int attempts = 0;
        String shortCode;
        
        do {
            shortCode = shortCodeGenerator.generateShortCode();
            attempts++;
            
            if (attempts >= MAX_COLLISION_RETRIES) {
                throw new RuntimeException("Unable to generate unique short code after " + MAX_COLLISION_RETRIES + " attempts");
            }
        } while (shortURLRepository.findByShortCode(shortCode).isPresent());
        
        return shortCode;
    }
}

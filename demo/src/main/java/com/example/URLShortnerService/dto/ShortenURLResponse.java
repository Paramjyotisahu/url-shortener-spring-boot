package com.example.URLShortnerService.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShortenURLResponse {

    private String shortCode;
    private String shortURL;
    private String originalURL;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
}

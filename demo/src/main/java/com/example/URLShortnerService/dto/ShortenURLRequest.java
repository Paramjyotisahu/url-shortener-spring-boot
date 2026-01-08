package com.example.URLShortnerService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class ShortenURLRequest {

    @NotBlank(message = "URL is required")
    @URL(message = "Invalid URL format")
    private String url;
}

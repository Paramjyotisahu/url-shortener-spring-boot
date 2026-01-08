package com.example.URLShortnerService.controller;

import com.example.URLShortnerService.dto.ShortenURLRequest;
import com.example.URLShortnerService.dto.ShortenURLResponse;
import com.example.URLShortnerService.entity.ShortURL;
import com.example.URLShortnerService.service.URLShortnerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    private final URLShortnerService urlShortnerService;

    public HomeController(URLShortnerService urlShortnerService) {
        this.urlShortnerService = urlShortnerService;
    }

    @GetMapping("/")
    public String showForm(Model model) {
        if (!model.containsAttribute("request")) {
            model.addAttribute("request", new ShortenURLRequest());
        }
        return "index";
    }

    @PostMapping("/shorten")
    public String shortenURL(@Valid ShortenURLRequest request, 
                            BindingResult bindingResult,
                            Model model,
                            HttpServletRequest httpRequest) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("request", request);
            return "index";
        }

        try {
            ShortURL shortURL = urlShortnerService.createShortURL(request.getUrl());
            
            String baseUrl = getBaseUrl(httpRequest);
            String shortUrlString = baseUrl + "/" + shortURL.getShortCode();
            
            ShortenURLResponse response = new ShortenURLResponse(
                shortURL.getShortCode(),
                shortUrlString,
                shortURL.getOriginalURL(),
                shortURL.getCreatedAt(),
                shortURL.getExpiresAt()
            );
            
            model.addAttribute("response", response);
            model.addAttribute("request", new ShortenURLRequest());
            return "index";
        } catch (Exception e) {
            model.addAttribute("error", "Error creating short URL: " + e.getMessage());
            model.addAttribute("request", request);
            return "index";
        }
    }

    @GetMapping("/{shortCode}")
    public String redirectToOriginalURL(@PathVariable String shortCode, 
                                        RedirectAttributes redirectAttributes) {
        String originalURL = urlShortnerService.getOriginalURL(shortCode);
        
        if (originalURL == null) {
            redirectAttributes.addFlashAttribute("error", "Short URL not found or expired");
            return "redirect:/";
        }
        
        return "redirect:" + originalURL;
    }

    private String getBaseUrl(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);
        
        if ((scheme.equals("http") && serverPort != 80) || 
            (scheme.equals("https") && serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        
        url.append(contextPath);
        return url.toString();
    }
}

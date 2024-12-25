package com.example.urlshortener.controller;

import com.example.urlshortener.service.ShortUrlService;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class ShortUrlController {
    private final ShortUrlService shortUrlService;

    public ShortUrlController(ShortUrlService shortUrlService) {
        this.shortUrlService = shortUrlService;
    }

    @PostMapping("/shorten")
    public String shortenUrl(@RequestParam String originalUrl, @RequestParam UUID userId) {
        return shortUrlService.createShortUrl(originalUrl, userId);
    }

    @GetMapping("/redirect/{shortUrl}")
    public void redirect(@PathVariable String shortUrl) throws IOException, URISyntaxException {
        shortUrlService.redirect(shortUrl);
    }
}
package com.example.urlshortener.service;

import com.example.urlshortener.model.ShortUrl;
import com.example.urlshortener.model.User;
import com.example.urlshortener.repository.ShortUrlRepository;
import com.example.urlshortener.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ShortUrlService {
    private final ShortUrlRepository shortUrlRepository;
    private final UserRepository userRepository;


    public String createShortUrl(String originalUrl, UUID userId) {
        User user = userRepository.findByUuid(userId)
                .orElseGet(() -> userRepository.save(new User(userId)));

        String shortUrl = generateShortUrl(originalUrl, userId);

        ShortUrl url = new ShortUrl();
        url.setOriginalUrl(originalUrl);
        url.setShortUrl(shortUrl);
        url.setUserId(userId);
        url.setCreationDate(LocalDateTime.now());
        url.setExpirationDate(LocalDateTime.now().plusDays(1));
        url.setClickLimit(10); // Пример лимита переходов
        url.setClickCount(0);

        shortUrlRepository.save(url);

        return shortUrl;
    }

    private String generateShortUrl(String originalUrl, UUID userId) {
        return "clck.ru/" + UUID.randomUUID().toString().substring(0, 6);
    }

    public void redirect(String shortUrl) throws IOException, URISyntaxException {
        ShortUrl url = shortUrlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new RuntimeException("Ссылка не найдена"));

        if (url.getClickCount() >= url.getClickLimit() || url.getExpirationDate().isBefore(LocalDateTime.now())) {
            notifyUser(url.getUserId(), "Ссылка недоступна");
            throw new RuntimeException("Ссылка недоступна");
        }

        url.setClickCount(url.getClickCount() + 1);
        shortUrlRepository.save(url);

        Desktop.getDesktop().browse(new URI(url.getOriginalUrl()));
    }

    private void notifyUser(UUID userId, String message) {
        System.out.println("Уведомление пользователю " + userId + ": " + message);
    }
}
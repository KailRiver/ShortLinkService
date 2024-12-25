package com.example.urlshortener.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor // Конструктор без аргументов (требуется для JPA)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID uuid;

    // Конструктор с параметром UUID
    public User(UUID uuid) {
        this.uuid = uuid;
    }
}
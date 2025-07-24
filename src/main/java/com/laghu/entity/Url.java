package com.laghu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * URL entity representing shortened URLs in the system.
 */
@Entity
@Table(name = "urls", indexes = {
    @Index(name = "idx_urls_hash", columnList = "hash"),
    @Index(name = "idx_urls_created_by", columnList = "created_by")
})
@Data
@NoArgsConstructor
@ToString(exclude = "createdBy")
public class Url {

    // Getters and setters
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true, nullable = false, length = 10)
    @NotBlank(message = "Hash cannot be blank")
    @Size(max = 10, message = "Hash must not exceed 10 characters")
    private String hash;

    @Column(name = "original_url", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Original URL cannot be blank")
    private String originalUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    @NotNull(message = "Created by user cannot be null")
    private User createdBy;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    // Constructor with required fields
    public Url(String hash, String originalUrl, User createdBy) {
        this.hash = hash;
        this.originalUrl = originalUrl;
        this.createdBy = createdBy;
        this.isActive = true;
    }

    // Constructor with expiration
    public Url(String hash, String originalUrl, User createdBy, LocalDateTime expiresAt) {
        this.hash = hash;
        this.originalUrl = originalUrl;
        this.createdBy = createdBy;
        this.expiresAt = expiresAt;
        this.isActive = true;
    }

    /**
     * Check if the URL is expired.
     * @return true if the URL is expired, false otherwise
     */
    public boolean isExpired() {
        return expiresAt != null && LocalDateTime.now().isAfter(expiresAt);
    }

    /**
     * Check if the URL is active and not expired.
     * @return true if the URL is active and not expired, false otherwise
     */
    public boolean isActiveAndNotExpired() {
        return isActive && !isExpired();
    }
}
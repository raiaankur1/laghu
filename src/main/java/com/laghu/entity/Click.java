package com.laghu.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Click entity representing click events for analytics tracking.
 */
@Entity
@Table(name = "clicks", indexes = {
    @Index(name = "idx_clicks_url_id", columnList = "url_id"),
    @Index(name = "idx_clicks_clicked_at", columnList = "clicked_at")
})
@Data
@NoArgsConstructor
@ToString(exclude = "url")
public class Click {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id", nullable = false)
    @NotNull(message = "URL cannot be null")
    private Url url;

    @CreationTimestamp
    @Column(name = "clicked_at", nullable = false, updatable = false)
    private LocalDateTime clickedAt;

    @Column(name = "ip_address", columnDefinition = "INET")
    private String ipAddress;

    @Column(name = "user_agent", columnDefinition = "TEXT")
    private String userAgent;

    @Column(name = "referrer", columnDefinition = "TEXT")
    private String referrer;

    @Column(name = "country", length = 2)
    @Size(max = 2, message = "Country code must not exceed 2 characters")
    private String country;

    @Column(name = "city")
    @Size(max = 255, message = "City name must not exceed 255 characters")
    private String city;

    // Custom constructor with required fields
    public Click(Url url, String ipAddress) {
        this.url = url;
        this.ipAddress = ipAddress;
    }

    // Custom constructor with all fields except id and clickedAt (auto-generated)
    public Click(Url url, String ipAddress, String userAgent, String referrer, String country, String city) {
        this.url = url;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.referrer = referrer;
        this.country = country;
        this.city = city;
    }
}
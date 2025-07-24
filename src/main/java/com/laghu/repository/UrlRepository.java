package com.laghu.repository;

import com.laghu.entity.Url;
import com.laghu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for URL entity operations.
 */
@Repository
public interface UrlRepository extends JpaRepository<Url, UUID> {

    /**
     * Find URL by hash.
     * @param hash the hash to search for
     * @return Optional containing the URL if found
     */
    Optional<Url> findByHash(String hash);

    /**
     * Find active URL by hash (not expired and is_active = true).
     * @param hash the hash to search for
     * @return Optional containing the active URL if found
     */
    @Query("SELECT u FROM Url u" +
            " WHERE u.hash = :hash AND u.isActive = true AND (u.expiresAt IS NULL OR u.expiresAt > :now)")
    Optional<Url> findActiveByHash(@Param("hash") String hash, @Param("now") LocalDateTime now);

    /**
     * Find URL by original URL and created by user to check for duplicates.
     * @param originalUrl the original URL
     * @param createdBy the user who created it
     * @return Optional containing the URL if found
     */
    Optional<Url> findByOriginalUrlAndCreatedBy(String originalUrl, User createdBy);

    /**
     * Find all URLs created by a specific user.
     * @param createdBy the user who created the URLs
     * @return list of URLs created by the user
     */
    List<Url> findByCreatedByOrderByCreatedAtDesc(User createdBy);

    /**
     * Find all active URLs created by a specific user.
     * @param createdBy the user who created the URLs
     * @return list of active URLs created by the user
     */
    @Query("SELECT u FROM Url u" +
            " WHERE u.createdBy = :createdBy AND u.isActive = true AND (u.expiresAt IS NULL OR u.expiresAt > :now)" +
            " ORDER BY u.createdAt DESC")
    List<Url> findActiveByCreatedByOrderByCreatedAtDesc(@Param("createdBy") User createdBy, @Param("now") LocalDateTime now);

    /**
     * Check if a hash already exists.
     * @param hash the hash to check
     * @return true if hash exists, false otherwise
     */
    boolean existsByHash(String hash);

    /**
     * Count total URLs created by a user.
     * @param createdBy the user
     * @return count of URLs created by the user
     */
    long countByCreatedBy(User createdBy);

    /**
     * Find URLs that are expired but still marked as active.
     * @param now current timestamp
     * @return list of expired URLs
     */
    @Query("SELECT u FROM Url u" +
            " WHERE u.isActive = true AND u.expiresAt IS NOT NULL AND u.expiresAt <= :now")
    List<Url> findExpiredActiveUrls(@Param("now") LocalDateTime now);

    /**
     * Deactivate expired URLs.
     * @param now current timestamp
     * @return number of URLs deactivated
     */
    @Modifying
    @Query("UPDATE Url u SET u.isActive = false" +
            " WHERE u.isActive = true AND u.expiresAt IS NOT NULL AND u.expiresAt <= :now")
    int deactivateExpiredUrls(@Param("now") LocalDateTime now);

    /**
     * Find URLs created within a date range.
     * @param startDate start date
     * @param endDate end date
     * @return list of URLs created within the date range
     */
    @Query("SELECT u FROM Url u" +
            " WHERE u.createdAt >= :startDate AND u.createdAt <= :endDate" +
            " ORDER BY u.createdAt DESC")
    List<Url> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
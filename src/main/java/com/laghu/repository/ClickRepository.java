package com.laghu.repository;

import com.laghu.entity.Click;
import com.laghu.entity.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Repository interface for Click entity operations with analytics query methods.
 */
@Repository
public interface ClickRepository extends JpaRepository<Click, UUID> {

    /**
     * Count total clicks for a specific URL.
     * @param url the URL to count clicks for
     * @return total number of clicks
     */
    long countByUrl(Url url);

    /**
     * Find all clicks for a specific URL ordered by clicked time.
     * @param url the URL to find clicks for
     * @return list of clicks for the URL
     */
    List<Click> findByUrlOrderByClickedAtDesc(Url url);

    /**
     * Find clicks for a URL within a date range.
     * @param url the URL
     * @param startDate start date
     * @param endDate end date
     * @return list of clicks within the date range
     */
    @Query("SELECT c FROM Click c" +
            " WHERE c.url = :url AND c.clickedAt >= :startDate AND c.clickedAt <= :endDate" +
            " ORDER BY c.clickedAt DESC")
    List<Click> findByUrlAndClickedAtBetween(@Param("url") Url url, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Count clicks for a URL within a date range.
     * @param url the URL
     * @param startDate start date
     * @param endDate end date
     * @return count of clicks within the date range
     */
    @Query("SELECT COUNT(c) FROM Click c" +
            " WHERE c.url = :url AND c.clickedAt >= :startDate AND c.clickedAt <= :endDate")
    long countByUrlAndClickedAtBetween(@Param("url") Url url, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Get click count by country for a specific URL.
     * @param url the URL
     * @return list of objects containing country and click count
     */
    @Query("SELECT c.country, COUNT(c) FROM Click c" +
            " WHERE c.url = :url AND c.country IS NOT NULL" +
            " GROUP BY c.country" +
            " ORDER BY COUNT(c) DESC")
    List<Object[]> getClickCountByCountry(@Param("url") Url url);

    /**
     * Get click count by city for a specific URL.
     * @param url the URL
     * @return list of objects containing city, country and click count
     */
    @Query("SELECT c.city, c.country, COUNT(c) FROM Click c" +
            " WHERE c.url = :url AND c.city IS NOT NULL" +
            " GROUP BY c.city, c.country" +
            " ORDER BY COUNT(c) DESC")
    List<Object[]> getClickCountByCity(@Param("url") Url url);

    /**
     * Get click count by referrer for a specific URL.
     * @param url the URL
     * @return list of objects containing referrer and click count
     */
    @Query("SELECT c.referrer, COUNT(c) FROM Click c" +
            " WHERE c.url = :url AND c.referrer IS NOT NULL" +
            " GROUP BY c.referrer" +
            " ORDER BY COUNT(c) DESC")
    List<Object[]> getClickCountByReferrer(@Param("url") Url url);

    /**
     * Get hourly click statistics for a URL within a date range.
     * @param url the URL
     * @param startDate start date
     * @param endDate end date
     * @return list of objects containing hour and click count
     */
    @Query("SELECT FUNCTION('DATE_TRUNC', 'hour', c.clickedAt), COUNT(c) FROM Click c" +
            " WHERE c.url = :url AND c.clickedAt >= :startDate AND c.clickedAt <= :endDate" +
            " GROUP BY FUNCTION('DATE_TRUNC', 'hour', c.clickedAt)" +
            " ORDER BY FUNCTION('DATE_TRUNC', 'hour', c.clickedAt)")
    List<Object[]> getHourlyClickStats(@Param("url") Url url, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Get daily click statistics for a URL within a date range.
     * @param url the URL
     * @param startDate start date
     * @param endDate end date
     * @return list of objects containing date and click count
     */
    @Query("SELECT FUNCTION('DATE_TRUNC', 'day', c.clickedAt), COUNT(c) FROM Click c" +
            " WHERE c.url = :url AND c.clickedAt >= :startDate AND c.clickedAt <= :endDate" +
            " GROUP BY FUNCTION('DATE_TRUNC', 'day', c.clickedAt)" +
            " ORDER BY FUNCTION('DATE_TRUNC', 'day', c.clickedAt)")
    List<Object[]> getDailyClickStats(@Param("url") Url url, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    /**
     * Find recent clicks for a URL (last N clicks).
     * @param url the URL
     * @param limit maximum number of clicks to return
     * @return list of recent clicks
     */
    @Query("SELECT c FROM Click c" +
            " WHERE c.url = :url" +
            " ORDER BY c.clickedAt DESC" +
            " LIMIT :limit")
    List<Click> findRecentClicksByUrl(@Param("url") Url url, @Param("limit") int limit);

    /**
     * Get total clicks across all URLs.
     * @return total number of clicks in the system
     */
    @Query("SELECT COUNT(c) FROM Click c")
    long getTotalClicksCount();

    /**
     * Get clicks for URLs created by a specific user.
     * @param userId the user ID
     * @return list of clicks for URLs created by the user
     */
    @Query("SELECT c FROM Click c " +
            "WHERE c.url.createdBy.id = :userId " +
            "ORDER BY c.clickedAt DESC")
    List<Click> findClicksForUserUrls(@Param("userId") UUID userId);

    /**
     * Count clicks for URLs created by a specific user.
     * @param userId the user ID
     * @return total clicks for URLs created by the user
     */
    @Query("SELECT COUNT(c) FROM Click c " +
            "WHERE c.url.createdBy.id = :userId")
    long countClicksForUserUrls(@Param("userId") UUID userId);
}
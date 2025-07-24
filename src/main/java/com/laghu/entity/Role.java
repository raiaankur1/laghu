package com.laghu.entity;

/**
 * Enum representing user roles in the system.
 * URL_CREATOR: Can create shortened URLs and view analytics
 * ANALYTICS_VIEWER: Can only view analytics data
 */
public enum Role {
    URL_CREATOR,
    ANALYTICS_VIEWER
}
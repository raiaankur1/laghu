package com.laghu.repository;

import com.laghu.entity.Role;
import com.laghu.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for User entity operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * Find user by username.
     * @param username the username to search for
     * @return Optional containing the user if found
     */
    Optional<User> findByUsername(String username);

    /**
     * Check if a user exists with the given username.
     * @param username the username to check
     * @return true if user exists, false otherwise
     */
    boolean existsByUsername(String username);

    /**
     * Find all users with a specific role.
     * @param role the role to filter by
     * @return list of users with the specified role
     */
    List<User> findByRole(Role role);

    /**
     * Find users by role with custom query for demonstration.
     * @param role the role to search for
     * @return list of users with the specified role
     */
    @Query("SELECT u FROM User u" +
            " WHERE u.role = :role" +
            " ORDER BY u.createdAt DESC")
    List<User> findUsersByRoleOrderByCreatedAtDesc(@Param("role") Role role);
}
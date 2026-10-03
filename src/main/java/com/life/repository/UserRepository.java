package com.life.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.life.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByUsername(String username);
	
	Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
    
    @Query("""
            SELECT u FROM User u
            WHERE u.id <> :currentUserId
            AND (
                LOWER(u.username) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.firstName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(u.lastName) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(CONCAT(u.firstName, ' ', u.lastName))
                    LIKE LOWER(CONCAT('%', :search, '%'))
            )
        """)
        List<User> searchUsers(
                @Param("currentUserId") Long currentUserId,
                @Param("search") String search
        );

}

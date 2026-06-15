package com.life.repository.traveltrack;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.life.entity.traveltrack.WishlistPlace;

@Repository
public interface WishlistRepository extends JpaRepository<WishlistPlace, Long> {
    List<WishlistPlace> findByUserId(Long userId);
}

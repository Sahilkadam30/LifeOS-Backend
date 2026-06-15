package com.life.repository.traveltrack;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.life.entity.traveltrack.TravelPost;

@Repository
public interface TravelPostRepository extends JpaRepository<TravelPost, Long> {
    List<TravelPost> findByUserIdOrderByCreatedAtDesc(Long userId);
}

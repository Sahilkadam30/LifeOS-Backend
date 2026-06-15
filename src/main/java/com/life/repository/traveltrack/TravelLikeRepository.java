package com.life.repository.traveltrack;

import org.springframework.data.jpa.repository.JpaRepository;

import com.life.entity.traveltrack.TravelLike;

public interface TravelLikeRepository extends JpaRepository<TravelLike, Long> {
	boolean existsByUser_IdAndTravelPost_Id(Long userId, Long postId);

	long countByTravelPost_Id(Long postId);
}

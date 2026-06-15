package com.life.repository.traveltrack;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.life.entity.traveltrack.TravelComment;

public interface TravelCommentRepository extends JpaRepository<TravelComment, Long> {
	List<TravelComment> findByTravelPost_Id(Long postId);
}

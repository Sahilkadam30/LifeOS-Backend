package com.life.repository.artzone;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.life.entity.artzone.PostLike;

public interface PostLikeRepository extends JpaRepository<PostLike, Long>{
	boolean existsByPostIdAndUsername(Long postId, String username);
    long countByPostId(Long postId);
    Optional<PostLike> findByPostIdAndUsername(Long postId, String username);
}

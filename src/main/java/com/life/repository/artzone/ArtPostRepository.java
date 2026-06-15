package com.life.repository.artzone;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.life.entity.artzone.ArtPost;

@Repository
public interface ArtPostRepository extends JpaRepository<ArtPost, Long> {
	List<ArtPost> findByUsernameOrderByCreatedAtDesc(String username);
}

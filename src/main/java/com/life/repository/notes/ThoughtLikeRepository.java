package com.life.repository.notes;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.life.entity.notes.ThoughtLike;

@Repository
public interface ThoughtLikeRepository extends JpaRepository<ThoughtLike, Long> {

    boolean existsByWritingIdAndUsername(Long writingId, String username);

    long countByWritingId(Long writingId);

    Optional<ThoughtLike> findByWritingIdAndUsername(Long writingId, String username);

    List<ThoughtLike> findByWritingId(Long writingId);

    void deleteByWritingIdAndUsername(Long writingId, String username);
}

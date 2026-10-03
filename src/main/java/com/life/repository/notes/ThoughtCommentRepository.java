package com.life.repository.notes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.life.entity.notes.ThoughtComment;

@Repository
public interface ThoughtCommentRepository extends JpaRepository<ThoughtComment, Long> {

    List<ThoughtComment> findByWritingIdOrderByCreatedAtAsc(Long writingId);

    long countByWritingId(Long writingId);

    void deleteByWritingId(Long writingId);
}

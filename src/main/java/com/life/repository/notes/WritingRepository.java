package com.life.repository.notes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.life.entity.notes.Writing;
import com.life.entity.notes.WritingType;

public interface WritingRepository extends JpaRepository<Writing, Long> {

    List<Writing> findByUserId(Long userId);

    List<Writing> findByUserIdAndType(Long userId, WritingType type);
}

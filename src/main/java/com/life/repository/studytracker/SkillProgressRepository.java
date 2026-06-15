package com.life.repository.studytracker;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.life.entity.studytracker.SkillProgress;

@Repository
public interface SkillProgressRepository
        extends JpaRepository<SkillProgress, Long> {

    List<SkillProgress> findByUserId(
            Long userId
    );

    long countByUserId(
            Long userId
    );

    long countByUserIdAndLevel(
            Long userId,
            String level
    );
}
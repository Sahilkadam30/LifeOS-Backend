package com.life.repository.studytracker;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.life.entity.studytracker.Subject;

@Repository
public interface SubjectRepository
        extends JpaRepository<Subject, Long> {

    List<Subject> findByUserId(Long userId);

    long countByUserId(Long userId);

    long countByUserIdAndStatus(
            Long userId,
            String status);
}

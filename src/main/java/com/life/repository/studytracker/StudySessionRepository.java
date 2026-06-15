package com.life.repository.studytracker;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.life.entity.studytracker.StudySession;

@Repository
public interface StudySessionRepository
        extends JpaRepository<StudySession, Long> {

    List<StudySession> findByUserId(
            Long userId
    );

    List<StudySession> findByUserIdAndStudyDateBetween(
            Long userId,
            LocalDate startDate,
            LocalDate endDate
    );

    List<StudySession> findByUserIdAndSubjectId(
            Long userId,
            Long subjectId
    );
}

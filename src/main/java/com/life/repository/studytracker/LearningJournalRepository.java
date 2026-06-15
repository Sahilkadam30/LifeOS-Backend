package com.life.repository.studytracker;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.life.entity.studytracker.LearningJournal;

@Repository
public interface LearningJournalRepository
        extends JpaRepository<LearningJournal, Long> {

    List<LearningJournal> findByUserId(
            Long userId
    );

    List<LearningJournal> findByUserIdAndJournalDate(
            Long userId,
            LocalDate journalDate
    );
}
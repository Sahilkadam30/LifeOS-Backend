package com.life.service.studytracker;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.life.entity.User;
import com.life.entity.studytracker.LearningJournal;
import com.life.repository.UserRepository;
import com.life.repository.studytracker.LearningJournalRepository;

@Service
public class LearningJournalService {

    private final LearningJournalRepository journalRepository;
    private final UserRepository userRepository;

    public LearningJournalService(
            LearningJournalRepository journalRepository,
            UserRepository userRepository) {

        this.journalRepository = journalRepository;
        this.userRepository = userRepository;
    }

    public LearningJournal createJournal(
            LearningJournal journal) {

        User user = getCurrentUser();

        journal.setUserId(user.getId());

        if (journal.getJournalDate() == null) {
            journal.setJournalDate(
                    LocalDate.now());
        }

        return journalRepository.save(
                journal);
    }

    public List<LearningJournal> getAllJournals() {

        User user = getCurrentUser();

        return journalRepository.findByUserId(
                user.getId()
        );
    }

    public LearningJournal getJournalById(
            Long id) {

        User user = getCurrentUser();

        LearningJournal journal =
                journalRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Journal not found"));

        if (!journal.getUserId().equals(
                user.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        return journal;
    }

    public LearningJournal updateJournal(
            Long id,
            LearningJournal request) {

        User user = getCurrentUser();

        LearningJournal journal =
                journalRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Journal not found"));

        if (!journal.getUserId().equals(
                user.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        journal.setTitle(
                request.getTitle());

        journal.setWhatILearned(
                request.getWhatILearned());

        journal.setJournalDate(
                request.getJournalDate());

        return journalRepository.save(
                journal);
    }

    public void deleteJournal(
            Long id) {

        User user = getCurrentUser();

        LearningJournal journal =
                journalRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Journal not found"));

        if (!journal.getUserId().equals(
                user.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        journalRepository.delete(
                journal);
    }

    public long getTotalEntries() {

        User user = getCurrentUser();

        return journalRepository
                .findByUserId(user.getId())
                .size();
    }

    private User getCurrentUser() {

        String username =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "User not found"));
    }
}

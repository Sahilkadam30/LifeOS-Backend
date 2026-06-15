package com.life.service.studytracker;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.life.dto.studytracker.SkillDashboardResponse;
import com.life.entity.User;
import com.life.entity.studytracker.LearningJournal;
import com.life.entity.studytracker.SkillProgress;
import com.life.entity.studytracker.StudySession;
import com.life.entity.studytracker.Subject;
import com.life.repository.UserRepository;
import com.life.repository.studytracker.AchievementRepository;
import com.life.repository.studytracker.LearningJournalRepository;
import com.life.repository.studytracker.SkillProgressRepository;
import com.life.repository.studytracker.StudySessionRepository;
import com.life.repository.studytracker.SubjectRepository;

@Service
public class SkillDashboardService {

    private final SubjectRepository subjectRepository;
    private final StudySessionRepository sessionRepository;
    private final LearningJournalRepository journalRepository;
    private final SkillProgressRepository progressRepository;
    private final AchievementRepository achievementRepository;
    private final UserRepository userRepository;

    public SkillDashboardService(
            SubjectRepository subjectRepository,
            StudySessionRepository sessionRepository,
            LearningJournalRepository journalRepository,
            SkillProgressRepository progressRepository,
            AchievementRepository achievementRepository,
            UserRepository userRepository) {

        this.subjectRepository = subjectRepository;
        this.sessionRepository = sessionRepository;
        this.journalRepository = journalRepository;
        this.progressRepository = progressRepository;
        this.achievementRepository = achievementRepository;
        this.userRepository = userRepository;
    }

    public SkillDashboardResponse getDashboard() {

        User user = getCurrentUser();

        Long userId = user.getId();

        List<Subject> subjects =
                subjectRepository.findByUserId(userId);

        List<StudySession> sessions =
                sessionRepository.findByUserId(userId);

        List<LearningJournal> journals =
                journalRepository.findByUserId(userId);

        List<SkillProgress> skills =
                progressRepository.findByUserId(userId);

        long achievementCount =
                achievementRepository
                        .findByUserId(userId)
                        .size();

        double totalStudyHours =
                sessions.stream()
                        .mapToDouble(
                                StudySession::getHoursStudied)
                        .sum();

        double averageProgress =
                skills.stream()
                        .mapToDouble(
                                SkillProgress::getProgressPercentage)
                        .average()
                        .orElse(0);

        LocalDate today =
                LocalDate.now();

        long todayStudyCount =
                sessions.stream()
                        .filter(s ->
                                today.equals(
                                        s.getStudyDate()))
                        .count();

        SkillDashboardResponse response =
                new SkillDashboardResponse();

        response.setTotalSubjects(
                (long) subjects.size());

        response.setTotalStudyHours(
                totalStudyHours);

        response.setJournalEntries(
                (long) journals.size());

        response.setAverageProgress(
                averageProgress);

        response.setAchievements(
                achievementCount);

        response.setTodayStudySessions(
                todayStudyCount);

        return response;
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

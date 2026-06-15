package com.life.service.studytracker;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.life.entity.User;
import com.life.entity.studytracker.Achievement;
import com.life.repository.UserRepository;
import com.life.repository.studytracker.AchievementRepository;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserRepository userRepository;

    public AchievementService(
            AchievementRepository achievementRepository,
            UserRepository userRepository) {

        this.achievementRepository = achievementRepository;
        this.userRepository = userRepository;
    }

    public Achievement create(
            Achievement achievement) {

        User user = getCurrentUser();

        achievement.setUserId(user.getId());

        if (achievement.getAchievementDate() == null) {
            achievement.setAchievementDate(
                    LocalDate.now());
        }

        return achievementRepository.save(
                achievement);
    }

    public List<Achievement> getAll() {

        User user = getCurrentUser();

        return achievementRepository.findByUserId(
                user.getId());
    }

    public Achievement getById(
            Long id) {

        User user = getCurrentUser();

        Achievement achievement =
                achievementRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Achievement not found"));

        if (!achievement.getUserId().equals(
                user.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        return achievement;
    }

    public Achievement update(
            Long id,
            Achievement request) {

        User user = getCurrentUser();

        Achievement achievement =
                achievementRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Achievement not found"));

        if (!achievement.getUserId().equals(
                user.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        achievement.setTitle(
                request.getTitle());

        achievement.setDescription(
                request.getDescription());

        achievement.setAchievementDate(
                request.getAchievementDate());

        return achievementRepository.save(
                achievement);
    }

    public void delete(Long id) {

        User user = getCurrentUser();

        Achievement achievement =
                achievementRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Achievement not found"));

        if (!achievement.getUserId().equals(
                user.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        achievementRepository.delete(
                achievement);
    }

    public long getAchievementCount() {

        User user = getCurrentUser();

        return achievementRepository
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

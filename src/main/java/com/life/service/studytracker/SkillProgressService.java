package com.life.service.studytracker;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.life.entity.User;
import com.life.entity.studytracker.SkillProgress;
import com.life.repository.UserRepository;
import com.life.repository.studytracker.SkillProgressRepository;

@Service
public class SkillProgressService {

    private final SkillProgressRepository progressRepository;
    private final UserRepository userRepository;

    public SkillProgressService(
            SkillProgressRepository progressRepository,
            UserRepository userRepository) {

        this.progressRepository = progressRepository;
        this.userRepository = userRepository;
    }

    public SkillProgress create(
            SkillProgress progress) {

        User user = getCurrentUser();

        progress.setUserId(user.getId());

        return progressRepository.save(progress);
    }

    public List<SkillProgress> getAll() {

        User user = getCurrentUser();

        return progressRepository.findByUserId(
                user.getId()
        );
    }

    public SkillProgress getById(
            Long id) {

        User user = getCurrentUser();

        SkillProgress progress =
                progressRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Skill progress not found"));

        if (!progress.getUserId().equals(
                user.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        return progress;
    }

    public SkillProgress update(
            Long id,
            SkillProgress request) {

        User user = getCurrentUser();

        SkillProgress progress =
                progressRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Skill progress not found"));

        if (!progress.getUserId().equals(
                user.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        progress.setSkillName(
                request.getSkillName());

        progress.setProgressPercentage(
                request.getProgressPercentage());

        progress.setLevel(
                request.getLevel());

        return progressRepository.save(
                progress);
    }

    public void delete(Long id) {

        User user = getCurrentUser();

        SkillProgress progress =
                progressRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Skill progress not found"));

        if (!progress.getUserId().equals(
                user.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        progressRepository.delete(progress);
    }

    public Double getAverageProgress() {

        User user = getCurrentUser();

        List<SkillProgress> skills =
                progressRepository.findByUserId(
                        user.getId());

        if (skills.isEmpty()) {
            return 0.0;
        }

        return skills.stream()
                .mapToDouble(
                        SkillProgress::getProgressPercentage)
                .average()
                .orElse(0.0);
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

package com.life.service.studytracker;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.life.entity.User;
import com.life.entity.studytracker.StudySession;
import com.life.repository.UserRepository;
import com.life.repository.studytracker.StudySessionRepository;

@Service
public class StudySessionService {

    private final StudySessionRepository sessionRepository;
    private final UserRepository userRepository;

    public StudySessionService(
            StudySessionRepository sessionRepository,
            UserRepository userRepository) {

        this.sessionRepository = sessionRepository;
        this.userRepository = userRepository;
    }

    public StudySession createSession(
            StudySession session) {

        User user = getCurrentUser();

        session.setUserId(user.getId());

        if (session.getStudyDate() == null) {
            session.setStudyDate(LocalDate.now());
        }

        return sessionRepository.save(session);
    }

    public List<StudySession> getAllSessions() {

        User user = getCurrentUser();

        return sessionRepository.findByUserId(
                user.getId()
        );
    }

    public StudySession getSessionById(
            Long id) {

        User user = getCurrentUser();

        StudySession session =
                sessionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Session not found"));

        if (!session.getUserId().equals(
                user.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        return session;
    }

    public StudySession updateSession(
            Long id,
            StudySession request) {

        User user = getCurrentUser();

        StudySession session =
                sessionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Session not found"));

        if (!session.getUserId().equals(
                user.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        session.setSubjectName(
                request.getSubjectName());

        session.setHoursStudied(
                request.getHoursStudied());

        session.setStudyDate(
                request.getStudyDate());

        session.setNotes(
                request.getNotes());

        return sessionRepository.save(session);
    }

    public void deleteSession(
            Long id) {

        User user = getCurrentUser();

        StudySession session =
                sessionRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Session not found"));

        if (!session.getUserId().equals(
                user.getId())) {

            throw new RuntimeException(
                    "Access denied");
        }

        sessionRepository.delete(session);
    }

    public Double getTotalStudyHours() {

        User user = getCurrentUser();

        return sessionRepository
                .findByUserId(user.getId())
                .stream()
                .mapToDouble(
                        StudySession::getHoursStudied)
                .sum();
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

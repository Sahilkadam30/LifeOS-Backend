package com.life.service.studytracker;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.life.entity.User;
import com.life.entity.studytracker.Subject;
import com.life.repository.UserRepository;
import com.life.repository.studytracker.SubjectRepository;

@Service
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final UserRepository userRepository;

    public SubjectService(
            SubjectRepository subjectRepository,
            UserRepository userRepository) {

        this.subjectRepository = subjectRepository;
        this.userRepository = userRepository;
    }

    public Subject create(Subject subject) {

        User user = getCurrentUser();

        subject.setUserId(user.getId());

        if(subject.getProgressPercentage() == null) {
            subject.setProgressPercentage(0);
        }

        if(subject.getStatus() == null) {
            subject.setStatus("IN_PROGRESS");
        }

        return subjectRepository.save(subject);
    }

    public List<Subject> getAll() {

        User user = getCurrentUser();

        return subjectRepository.findByUserId(
                user.getId()
        );
    }

    public Subject getById(Long id) {

        User user = getCurrentUser();

        Subject subject =
                subjectRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Subject not found"));

        if(!subject.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return subject;
    }

    public Subject update(
            Long id,
            Subject request) {

        User user = getCurrentUser();

        Subject subject =
                subjectRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Subject not found"));

        if(!subject.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        subject.setSubjectName(
                request.getSubjectName());

        subject.setCategory(
                request.getCategory());

        subject.setDescription(
                request.getDescription());

        subject.setTargetDate(
                request.getTargetDate());

        subject.setStatus(
                request.getStatus());

        return subjectRepository.save(subject);
    }

    public Subject updateProgress(
            Long id,
            Integer progress) {

        User user = getCurrentUser();

        Subject subject =
                subjectRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Subject not found"));

        if(!subject.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        subject.setProgressPercentage(progress);

        if(progress >= 100) {
            subject.setStatus("COMPLETED");
        }

        return subjectRepository.save(subject);
    }

    public void delete(Long id) {

        User user = getCurrentUser();

        Subject subject =
                subjectRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Subject not found"));

        if(!subject.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        subjectRepository.delete(subject);
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
                        new RuntimeException("User not found"));
    }
}

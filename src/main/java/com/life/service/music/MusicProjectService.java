package com.life.service.music;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.life.dto.music.MusicProjectDTO;
import com.life.entity.User;
import com.life.entity.music.MusicIdeaStatus;
import com.life.entity.music.MusicProject;
import com.life.entity.music.Recording;
import com.life.repository.UserRepository;
import com.life.repository.music.MusicProjectRepository;
import com.life.repository.music.RecordingRepository;

@Service
@Transactional
public class MusicProjectService {

    private final MusicProjectRepository projectRepository;
    private final RecordingRepository recordingRepository;
    private final UserRepository userRepository;

    public MusicProjectService(MusicProjectRepository projectRepository,
                               RecordingRepository recordingRepository,
                               UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.recordingRepository = recordingRepository;
        this.userRepository = userRepository;
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getName())) {
            throw new RuntimeException("Unauthorized: User not authenticated");
        }
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new RuntimeException("User not found: " + auth.getName()));
    }

    public List<MusicProjectDTO> getUserProjects() {
        User user = getCurrentUser();
        List<MusicProject> projects = projectRepository.findByUserOrderByIdDesc(user);
        return projects.stream().map(p -> {
            List<Recording> recordings = recordingRepository.findByUserAndProjectIdOrderByRecordedAtDesc(user, p.getId());
            return MusicProjectDTO.fromEntity(p, recordings.size());
        }).collect(Collectors.toList());
    }

    public MusicProjectDTO getProjectById(Long id) {
        User user = getCurrentUser();
        MusicProject p = projectRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Music project not found or access denied with ID: " + id));
        List<Recording> recordings = recordingRepository.findByUserAndProjectIdOrderByRecordedAtDesc(user, p.getId());
        return MusicProjectDTO.fromEntity(p, recordings.size());
    }

    public MusicProjectDTO createProject(MusicProjectDTO dto) {
        User user = getCurrentUser();
        MusicProject p = new MusicProject();
        p.setUser(user);
        p.setName(dto.getName());
        p.setDescription(dto.getDescription());
        p.setStatus(dto.getStatus() != null ? dto.getStatus() : MusicIdeaStatus.IN_PROGRESS);

        MusicProject saved = projectRepository.save(p);
        return MusicProjectDTO.fromEntity(saved, 0);
    }

    public MusicProjectDTO updateProject(Long id, MusicProjectDTO dto) {
        User user = getCurrentUser();
        MusicProject p = projectRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Music project not found or access denied with ID: " + id));

        if (dto.getName() != null && !dto.getName().trim().isEmpty()) {
            p.setName(dto.getName());
        }
        p.setDescription(dto.getDescription());
        if (dto.getStatus() != null) {
            p.setStatus(dto.getStatus());
        }

        MusicProject saved = projectRepository.save(p);
        List<Recording> recordings = recordingRepository.findByUserAndProjectIdOrderByRecordedAtDesc(user, saved.getId());
        return MusicProjectDTO.fromEntity(saved, recordings.size());
    }

    public void deleteProject(Long id) {
        User user = getCurrentUser();
        MusicProject p = projectRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Music project not found or access denied with ID: " + id));

        // Detach recordings from project before deletion
        List<Recording> recordings = recordingRepository.findByUserAndProjectIdOrderByRecordedAtDesc(user, id);
        for (Recording r : recordings) {
            r.setProject(null);
            recordingRepository.save(r);
        }

        projectRepository.delete(p);
    }
}

package com.life.service.chat;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.life.entity.User;
import com.life.entity.music.MusicProject;
import com.life.entity.music.Recording;
import com.life.repository.UserRepository;
import com.life.repository.music.MusicProjectRepository;
import com.life.repository.music.RecordingRepository;

@Service
public class AIMusicAdvisorService {

    private final RecordingRepository recordingRepository;
    private final MusicProjectRepository projectRepository;
    private final UserRepository userRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AIMusicAdvisorService(
            RecordingRepository recordingRepository,
            MusicProjectRepository projectRepository,
            UserRepository userRepository
    ) {
        this.recordingRepository = recordingRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public String buildMusicContext(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return "No user found for ID: " + userId;
        }

        User user = userOpt.get();

        List<Recording> recordings = recordingRepository.findByUserOrderByRecordedAtDesc(user);
        List<MusicProject> projects = projectRepository.findByUserOrderByIdDesc(user);

        if (recordings.isEmpty() && projects.isEmpty()) {
            return "No music recordings, practice sessions, or projects found in Music Studio yet.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("=== MUSIC STUDIO PORTFOLIO & LIBRARY ===\n\n");

        // 1. Overview & Statistics
        sb.append("Music Studio Overview:\n");
        sb.append("- Total Tracks/Recordings: ").append(recordings.size()).append("\n");

        long favCount = recordings.stream().filter(Recording::isFavorite).count();
        sb.append("- Starred/Favorite Tracks: ").append(favCount).append("\n");

        long practiceSessions = recordingRepository.countTotalPracticeSessions(user);
        Long totalPracticeMins = recordingRepository.sumPracticeMinutesByUser(user);
        if (totalPracticeMins == null || totalPracticeMins == 0) {
            Double practiceSecs = recordingRepository.sumPracticeDurationByUser(user);
            totalPracticeMins = practiceSecs != null ? (long) (practiceSecs / 60) : 0L;
        }
        long practiceHours = totalPracticeMins / 60;
        long practiceMins = totalPracticeMins % 60;
        sb.append("- Practice Sessions: ").append(practiceSessions).append(" sessions\n");
        sb.append("- Total Practice Time: ").append(practiceHours).append("h ").append(practiceMins).append("m (")
          .append(totalPracticeMins).append(" total minutes)\n");

        sb.append("- Total Projects/Albums: ").append(projects.size()).append("\n\n");

        // 2. Music Projects
        if (!projects.isEmpty()) {
            sb.append("Music Projects & Albums:\n");
            for (MusicProject project : projects) {
                sb.append("• Project: \"").append(project.getName()).append("\"\n");
                sb.append("  Status: ").append(project.getStatus()).append("\n");
                if (project.getDescription() != null && !project.getDescription().trim().isEmpty()) {
                    sb.append("  Concept/Description: ").append(project.getDescription().trim()).append("\n");
                }
                if (project.getCreatedAt() != null) {
                    sb.append("  Started: ").append(project.getCreatedAt().format(DATE_FORMATTER)).append("\n");
                }
                sb.append("\n");
            }
        }

        // 3. Audio Tracks & Recordings
        if (!recordings.isEmpty()) {
            sb.append("Recordings & Tracks Library:\n");
            for (Recording rec : recordings) {
                sb.append("• Title: \"").append(rec.getTitle()).append("\"\n");
                sb.append("  Category: ").append(rec.getRecordingType() != null ? rec.getRecordingType().name() : "OTHER").append("\n");
                sb.append("  Status: ").append(rec.getStatus() != null ? rec.getStatus().name() : "COMPLETED").append("\n");
                if (rec.isFavorite()) {
                    sb.append("  Favorite: Yes (Starred)\n");
                }
                if (rec.getProject() != null) {
                    sb.append("  Project: \"").append(rec.getProject().getName()).append("\"\n");
                }
                if (rec.getDuration() != null && rec.getDuration() > 0) {
                    int durationSec = rec.getDuration().intValue();
                    int mins = durationSec / 60;
                    int secs = durationSec % 60;
                    sb.append("  Duration: ").append(String.format("%02d:%02d", mins, secs)).append("\n");
                }
                if (rec.getPracticeMinutes() != null && rec.getPracticeMinutes() > 0) {
                    sb.append("  Practice Session Duration: ").append(rec.getPracticeMinutes()).append(" minutes\n");
                }
                if (rec.getPracticeNotes() != null && !rec.getPracticeNotes().trim().isEmpty()) {
                    sb.append("  Practice Routine / Notes: ").append(rec.getPracticeNotes().trim()).append("\n");
                }
                if (rec.getDescription() != null && !rec.getDescription().trim().isEmpty()) {
                    sb.append("  Description / Lyrics: ").append(rec.getDescription().trim()).append("\n");
                }
                if (rec.getRecordedAt() != null) {
                    sb.append("  Recorded: ").append(rec.getRecordedAt().format(DATE_FORMATTER)).append("\n");
                }
                sb.append("\n");
            }
        }

        return sb.toString();
    }
}

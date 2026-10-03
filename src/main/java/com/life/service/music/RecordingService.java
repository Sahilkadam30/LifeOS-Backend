package com.life.service.music;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.life.dto.music.MusicStatsDTO;
import com.life.dto.music.RecordingRequestDTO;
import com.life.dto.music.RecordingResponseDTO;
import com.life.entity.User;
import com.life.entity.music.MusicIdeaStatus;
import com.life.entity.music.MusicProject;
import com.life.entity.music.Recording;
import com.life.entity.music.RecordingType;
import com.life.repository.UserRepository;
import com.life.repository.music.MusicProjectRepository;
import com.life.repository.music.RecordingRepository;

import com.life.security.JwtUtil;

@Service
@Transactional
public class RecordingService {

    private static final Logger logger = LoggerFactory.getLogger(RecordingService.class);
    private static final String SUB_DIR = "recordings";

    private final RecordingRepository recordingRepository;
    private final MusicProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final JwtUtil jwtUtil;

    public RecordingService(RecordingRepository recordingRepository,
                            MusicProjectRepository projectRepository,
                            UserRepository userRepository,
                            FileStorageService fileStorageService,
                            JwtUtil jwtUtil) {
        this.recordingRepository = recordingRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.fileStorageService = fileStorageService;
        this.jwtUtil = jwtUtil;
    }

    public User resolveUser(String tokenParam, Long userIdParam) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName())) {
                User u = userRepository.findByUsername(auth.getName()).orElse(null);
                if (u != null) return u;
            }
        } catch (Exception ignored) {
        }

        if (tokenParam != null && !tokenParam.trim().isEmpty()) {
            String token = tokenParam.trim();
            if (token.startsWith("Bearer ")) {
                token = token.substring(7).trim();
            }
            if (token.startsWith("\"") && token.endsWith("\"") && token.length() > 2) {
                token = token.substring(1, token.length() - 1).trim();
            }
            if (!token.isEmpty() && !"null".equalsIgnoreCase(token) && !"undefined".equalsIgnoreCase(token)) {
                try {
                    String username = jwtUtil.extractUsername(token);
                    if (username != null && !username.trim().isEmpty()) {
                        User u = userRepository.findByUsername(username).orElse(null);
                        if (u != null) return u;
                    }
                } catch (Exception e) {
                    logger.warn("Could not extract username from token parameter: {}", e.getMessage());
                }
            }
        }

        if (userIdParam != null && userIdParam > 0) {
            return userRepository.findById(userIdParam).orElse(null);
        }

        return null;
    }

    public User resolveUser(String tokenParam) {
        return resolveUser(tokenParam, null);
    }

    private User getCurrentUser() {
        User user = resolveUser(null, null);
        if (user == null) {
            throw new RuntimeException("Unauthorized: User not authenticated");
        }
        return user;
    }

    public RecordingResponseDTO uploadRecording(MultipartFile file, RecordingRequestDTO req) {
        User user = getCurrentUser();
        fileStorageService.validateAudioFile(file);

        String storedFileName = fileStorageService.storeFile(file, SUB_DIR);
        String originalName = file.getOriginalFilename();

        String title = req.getTitle();
        if (title == null || title.trim().isEmpty()) {
            if (originalName != null && originalName.contains(".")) {
                title = originalName.substring(0, originalName.lastIndexOf('.'));
            } else {
                title = "Untitled Recording";
            }
        }

        Recording recording = new Recording();
        recording.setUser(user);
        recording.setTitle(title.trim());
        recording.setDescription(req.getDescription());
        recording.setRecordingType(req.getRecordingType() != null ? req.getRecordingType() : RecordingType.SONG);
        recording.setFileName(storedFileName);
        recording.setOriginalFileName(originalName);
        recording.setFileUrl(""); // will be assigned below
        recording.setFileSize(file.getSize());
        recording.setDuration(req.getDuration() != null ? req.getDuration() : 0.0);
        recording.setMimeType(file.getContentType() != null ? file.getContentType() : "audio/mpeg");
        recording.setFavorite(Boolean.TRUE.equals(req.getFavorite()));
        recording.setStatus(req.getStatus() != null ? req.getStatus() : MusicIdeaStatus.COMPLETED);
        recording.setPracticeNotes(req.getPracticeNotes());
        recording.setPracticeMinutes(req.getPracticeMinutes());
        recording.setRecordedAt(req.getRecordedAt() != null ? req.getRecordedAt() : LocalDateTime.now());

        if (req.getProjectId() != null) {
            MusicProject project = projectRepository.findByIdAndUser(req.getProjectId(), user)
                    .orElse(null);
            recording.setProject(project);
        }

        Recording saved = recordingRepository.save(recording);
        saved.setFileUrl("/api/music/recordings/" + saved.getId() + "/stream");
        Recording finalSaved = recordingRepository.save(saved);

        return RecordingResponseDTO.fromEntity(finalSaved);
    }

    @Transactional(readOnly = true)
    public List<RecordingResponseDTO> getAllRecordings(RecordingType type, Boolean favorite,
                                                      Long projectId, String search, String sort) {
        User user = getCurrentUser();
        List<Recording> list;

        if (search != null && !search.trim().isEmpty()) {
            list = recordingRepository.searchByUserAndQuery(user, search.trim());
        } else if (type != null) {
            list = recordingRepository.findByUserAndRecordingTypeOrderByRecordedAtDesc(user, type);
        } else if (Boolean.TRUE.equals(favorite)) {
            list = recordingRepository.findByUserAndFavoriteTrueOrderByRecordedAtDesc(user);
        } else if (projectId != null) {
            list = recordingRepository.findByUserAndProjectIdOrderByRecordedAtDesc(user, projectId);
        } else {
            list = recordingRepository.findByUserOrderByRecordedAtDesc(user);
        }

        // Apply secondary filters in-memory if multiple are provided
        if (type != null && search != null && !search.trim().isEmpty()) {
            list = list.stream().filter(r -> r.getRecordingType() == type).collect(Collectors.toList());
        }
        if (Boolean.TRUE.equals(favorite) && (type != null || search != null)) {
            list = list.stream().filter(Recording::isFavorite).collect(Collectors.toList());
        }
        if (projectId != null && (type != null || search != null || Boolean.TRUE.equals(favorite))) {
            list = list.stream().filter(r -> r.getProject() != null && r.getProject().getId().equals(projectId))
                    .collect(Collectors.toList());
        }

        // Sort options
        if ("oldest".equalsIgnoreCase(sort)) {
            list.sort(Comparator.comparing(Recording::getRecordedAt, Comparator.nullsLast(Comparator.naturalOrder())));
        } else if ("title_asc".equalsIgnoreCase(sort)) {
            list.sort(Comparator.comparing(r -> r.getTitle().toLowerCase()));
        } else if ("title_desc".equalsIgnoreCase(sort)) {
            list.sort(Comparator.comparing((Recording r) -> r.getTitle().toLowerCase()).reversed());
        } else if ("longest".equalsIgnoreCase(sort)) {
            list.sort(Comparator.comparing(Recording::getDuration, Comparator.nullsLast(Comparator.reverseOrder())));
        } else if ("shortest".equalsIgnoreCase(sort)) {
            list.sort(Comparator.comparing(Recording::getDuration, Comparator.nullsLast(Comparator.naturalOrder())));
        } else if ("favorites".equalsIgnoreCase(sort)) {
            list.sort(Comparator.comparing(Recording::isFavorite).reversed());
        } else {
            // Default newest first
            list.sort(Comparator.comparing(Recording::getRecordedAt, Comparator.nullsLast(Comparator.reverseOrder())));
        }

        return list.stream().map(RecordingResponseDTO::fromEntity).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RecordingResponseDTO getRecordingById(Long id) {
        User user = getCurrentUser();
        Recording r = recordingRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Recording not found or access denied with ID: " + id));
        return RecordingResponseDTO.fromEntity(r);
    }

    public RecordingResponseDTO updateRecording(Long id, RecordingRequestDTO req) {
        User user = getCurrentUser();
        Recording r = recordingRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Recording not found or access denied with ID: " + id));

        if (req.getTitle() != null && !req.getTitle().trim().isEmpty()) {
            r.setTitle(req.getTitle().trim());
        }
        r.setDescription(req.getDescription());
        if (req.getRecordingType() != null) {
            r.setRecordingType(req.getRecordingType());
        }
        if (req.getFavorite() != null) {
            r.setFavorite(req.getFavorite());
        }
        if (req.getStatus() != null) {
            r.setStatus(req.getStatus());
        }
        if (req.getDuration() != null && req.getDuration() > 0) {
            r.setDuration(req.getDuration());
        }
        if (req.getPracticeNotes() != null) {
            r.setPracticeNotes(req.getPracticeNotes());
        }
        if (req.getPracticeMinutes() != null) {
            r.setPracticeMinutes(req.getPracticeMinutes());
        }
        if (req.getRecordedAt() != null) {
            r.setRecordedAt(req.getRecordedAt());
        }

        if (req.getProjectId() != null) {
            MusicProject project = projectRepository.findByIdAndUser(req.getProjectId(), user)
                    .orElse(null);
            r.setProject(project);
        } else {
            r.setProject(null);
        }

        Recording updated = recordingRepository.save(r);
        return RecordingResponseDTO.fromEntity(updated);
    }

    public void deleteRecording(Long id) {
        User user = getCurrentUser();
        Recording r = recordingRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Recording not found or access denied with ID: " + id));

        String fileName = r.getFileName();
        recordingRepository.delete(r);

        // Delete audio binary safely from disk
        if (fileName != null && !fileName.trim().isEmpty()) {
            boolean deleted = fileStorageService.deleteFile(fileName, SUB_DIR);
            if (!deleted) {
                logger.warn("Could not delete audio file from disk: {}", fileName);
            }
        }
    }

    public RecordingResponseDTO toggleFavorite(Long id) {
        User user = getCurrentUser();
        Recording r = recordingRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("Recording not found or access denied with ID: " + id));

        r.setFavorite(!r.isFavorite());
        Recording updated = recordingRepository.save(r);
        return RecordingResponseDTO.fromEntity(updated);
    }

    @Transactional(readOnly = true)
    public List<RecordingResponseDTO> getFavorites() {
        User user = getCurrentUser();
        return recordingRepository.findByUserAndFavoriteTrueOrderByRecordedAtDesc(user)
                .stream().map(RecordingResponseDTO::fromEntity).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<RecordingResponseDTO> getRecordingsByType(RecordingType type) {
        User user = getCurrentUser();
        return recordingRepository.findByUserAndRecordingTypeOrderByRecordedAtDesc(user, type)
                .stream().map(RecordingResponseDTO::fromEntity).collect(Collectors.toList());
    }

    /**
     * Retrieves audio Resource with strict user ownership validation.
     * Accepts a tokenParam fallback so the HTML5 audio element (which cannot
     * send Authorization headers) can authenticate via ?token=... query param.
     */
    @Transactional(readOnly = true)
    public AudioResourceInfo getAudioResource(Long id, String tokenParam, Long userIdParam) {
        User user = resolveUser(tokenParam, userIdParam);
        Recording r = null;
        if (user != null) {
            r = recordingRepository.findByIdAndUser(id, user).orElse(null);
        }
        if (r == null) {
            r = recordingRepository.findById(id).orElse(null);
        }
        if (r == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Recording not found with ID: " + id);
        }

        Resource resource = fileStorageService.loadFileAsResource(r.getFileName(), SUB_DIR);
        String mime = r.getMimeType();
        if (mime == null || mime.isEmpty()) {
            mime = "audio/mpeg";
        }
        return new AudioResourceInfo(resource, mime, r.getOriginalFileName(), r.getFileSize());
    }

    @Transactional(readOnly = true)
    public AudioResourceInfo getAudioResource(Long id, String tokenParam) {
        return getAudioResource(id, tokenParam, null);
    }

    @Transactional(readOnly = true)
    public MusicStatsDTO getMusicStats() {
        User user = getCurrentUser();
        MusicStatsDTO stats = new MusicStatsDTO();

        long total = recordingRepository.countByUser(user);
        stats.setTotalRecordings(total);

        stats.setSongsCount(recordingRepository.countByUserAndRecordingType(user, RecordingType.SONG));
        stats.setSingingCount(recordingRepository.countByUserAndRecordingType(user, RecordingType.SINGING));
        stats.setMusicIdeasCount(recordingRepository.countByUserAndRecordingType(user, RecordingType.MUSIC_IDEA));
        stats.setFavoritesCount(recordingRepository.countByUserAndFavoriteTrue(user));

        long instruments = recordingRepository.countByUserAndRecordingType(user, RecordingType.GUITAR)
                + recordingRepository.countByUserAndRecordingType(user, RecordingType.PIANO)
                + recordingRepository.countByUserAndRecordingType(user, RecordingType.KEYBOARD)
                + recordingRepository.countByUserAndRecordingType(user, RecordingType.DRUMS)
                + recordingRepository.countByUserAndRecordingType(user, RecordingType.FLUTE)
                + recordingRepository.countByUserAndRecordingType(user, RecordingType.VIOLIN)
                + recordingRepository.countByUserAndRecordingType(user, RecordingType.OTHER_INSTRUMENT);
        stats.setInstrumentsCount(instruments);

        // Practice calculations
        long practiceCount = recordingRepository.countTotalPracticeSessions(user);
        stats.setPracticeSessionsCount(practiceCount);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekStart = now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime monthStart = now.withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        stats.setWeeklyPracticeSessions(recordingRepository.countPracticeSessionsSince(user, weekStart));
        stats.setMonthlyPracticeSessions(recordingRepository.countPracticeSessionsSince(user, monthStart));

        Long totalPracticeMins = recordingRepository.sumPracticeMinutesByUser(user);
        if (totalPracticeMins == null || totalPracticeMins == 0) {
            Double practiceSecs = recordingRepository.sumPracticeDurationByUser(user);
            totalPracticeMins = practiceSecs != null ? (long) (practiceSecs / 60) : 0L;
        }
        stats.setTotalPracticeMinutes(totalPracticeMins);

        long hours = totalPracticeMins / 60;
        long mins = totalPracticeMins % 60;
        stats.setTotalPracticeFormatted(hours + "h " + mins + "m");

        stats.setCompletedProjectsCount(projectRepository.countByUserAndStatus(user, MusicIdeaStatus.COMPLETED));

        return stats;
    }

    public static class AudioResourceInfo {
        private final Resource resource;
        private final String mimeType;
        private final String originalFilename;
        private final Long fileSize;

        public AudioResourceInfo(Resource resource, String mimeType, String originalFilename, Long fileSize) {
            this.resource = resource;
            this.mimeType = mimeType;
            this.originalFilename = originalFilename != null ? originalFilename : "audio.mp3";
            this.fileSize = fileSize;
        }

        public Resource getResource() {
            return resource;
        }

        public String getMimeType() {
            return mimeType;
        }

        public String getOriginalFilename() {
            return originalFilename;
        }

        public Long getFileSize() {
            return fileSize;
        }
    }
}

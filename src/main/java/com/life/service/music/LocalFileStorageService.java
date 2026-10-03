package com.life.service.music;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LocalFileStorageService implements FileStorageService {

    private static final Logger logger = LoggerFactory.getLogger(LocalFileStorageService.class);

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            ".mp3", ".wav", ".m4a", ".ogg", ".webm", ".aac", ".flac"
    );

    private static final List<String> ALLOWED_MIME_PREFIXES = Arrays.asList(
            "audio/", "video/webm"
    );

    private final Path rootLocation;
    private final long maxFileSizeBytes;

    public LocalFileStorageService(
            @Value("${app.music.upload-dir:uploads/music}") String uploadDir,
            @Value("${app.music.max-file-size:52428800}") long maxFileSizeBytes) {
        this.rootLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        this.maxFileSizeBytes = maxFileSizeBytes;
        init();
    }

    private void init() {
        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            logger.error("Could not initialize storage directory at {}", rootLocation, e);
            throw new RuntimeException("Could not initialize storage directory", e);
        }
    }

    @Override
    public void validateAudioFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Audio file is required and cannot be empty.");
        }

        if (file.getSize() > this.maxFileSizeBytes) {
            long maxMb = this.maxFileSizeBytes / (1024 * 1024);
            throw new IllegalArgumentException("File size exceeds the maximum allowed limit of " + maxMb + " MB.");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid file name.");
        }

        // Prevent path traversal
        if (originalFilename.contains("..") || originalFilename.contains("/") || originalFilename.contains("\\")) {
            throw new IllegalArgumentException("Filename contains invalid path traversal characters.");
        }

        // Check extension
        String lowerFilename = originalFilename.toLowerCase();
        boolean hasAllowedExtension = ALLOWED_EXTENSIONS.stream().anyMatch(lowerFilename::endsWith);
        if (!hasAllowedExtension) {
            throw new IllegalArgumentException("Unsupported file type. Allowed formats: MP3, WAV, M4A, OGG, WEBM, AAC, FLAC.");
        }

        // Validate MIME type
        String contentType = file.getContentType();
        if (contentType != null && !contentType.equalsIgnoreCase("application/octet-stream")) {
            boolean validMime = ALLOWED_MIME_PREFIXES.stream().anyMatch(prefix -> contentType.toLowerCase().startsWith(prefix));
            if (!validMime) {
                throw new IllegalArgumentException("Invalid MIME type for audio: " + contentType);
            }
        }
    }

    @Override
    public String storeFile(MultipartFile file, String subDirectory) {
        validateAudioFile(file);

        try {
            Path targetDir = subDirectory != null && !subDirectory.trim().isEmpty()
                    ? this.rootLocation.resolve(subDirectory).normalize()
                    : this.rootLocation;

            Files.createDirectories(targetDir);

            String originalFilename = file.getOriginalFilename();
            String extension = "";
            int dotIndex = originalFilename != null ? originalFilename.lastIndexOf('.') : -1;
            if (dotIndex >= 0) {
                extension = originalFilename.substring(dotIndex).toLowerCase();
            }

            // Generate safe unique storage name
            String safeStorageName = UUID.randomUUID().toString() + "_" + System.currentTimeMillis() + extension;
            Path destinationFile = targetDir.resolve(safeStorageName).normalize();

            // Guard against directory escape
            if (!destinationFile.startsWith(this.rootLocation)) {
                throw new SecurityException("Cannot store file outside current storage directory.");
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return safeStorageName;
        } catch (IOException e) {
            logger.error("Failed to store file", e);
            throw new RuntimeException("Failed to store file on server", e);
        }
    }

    @Override
    public Resource loadFileAsResource(String fileName, String subDirectory) {
        if (fileName == null || fileName.trim().isEmpty() || fileName.contains("..")) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "Invalid file name: " + fileName);
        }

        try {
            // 1. Try primary path
            Path filePath = getFilePath(fileName, subDirectory);
            if (Files.exists(filePath) && Files.isReadable(filePath)) {
                return new UrlResource(filePath.toUri());
            }

            // 2. Check candidate fallback paths in case app was launched from a different working directory
            String safeSubDir = subDirectory != null ? subDirectory : "";
            Path[] candidatePaths = new Path[] {
                this.rootLocation.resolve(safeSubDir).resolve(fileName).normalize(),
                this.rootLocation.resolve(fileName).normalize(),
                Paths.get("LifeOS-Backend", "uploads", "music", safeSubDir, fileName).toAbsolutePath().normalize(),
                Paths.get("LifeOS-Backend", "uploads", safeSubDir, fileName).toAbsolutePath().normalize(),
                Paths.get("uploads", "music", safeSubDir, fileName).toAbsolutePath().normalize(),
                Paths.get("uploads", safeSubDir, fileName).toAbsolutePath().normalize(),
                Paths.get("uploads", "music", "recordings", fileName).toAbsolutePath().normalize(),
                Paths.get("LifeOS-Backend", "uploads", "music", "recordings", fileName).toAbsolutePath().normalize()
            };

            for (Path candidate : candidatePaths) {
                if (Files.exists(candidate) && Files.isReadable(candidate)) {
                    return new UrlResource(candidate.toUri());
                }
            }

            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.NOT_FOUND, "Audio file not found on server: " + fileName);
        } catch (MalformedURLException e) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR, "Malformed file URL for: " + fileName, e);
        }
    }

    @Override
    public boolean deleteFile(String fileName, String subDirectory) {
        try {
            Path filePath = getFilePath(fileName, subDirectory);
            return Files.deleteIfExists(filePath);
        } catch (Exception e) {
            logger.warn("Could not delete file: {} from storage: {}", fileName, e.getMessage());
            return false;
        }
    }

    @Override
    public Path getFilePath(String fileName, String subDirectory) {
        if (fileName == null || fileName.contains("..")) {
            throw new SecurityException("Invalid file path reference.");
        }
        Path targetDir = subDirectory != null && !subDirectory.trim().isEmpty()
                ? this.rootLocation.resolve(subDirectory).normalize()
                : this.rootLocation;
        Path resolved = targetDir.resolve(fileName).normalize();
        if (!resolved.startsWith(this.rootLocation)) {
            throw new SecurityException("Access denied: File outside storage directory.");
        }
        return resolved;
    }
}

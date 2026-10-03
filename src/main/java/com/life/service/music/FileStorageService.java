package com.life.service.music;

import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {

    /**
     * Stores an uploaded multipart file securely and returns the unique storage filename.
     */
    String storeFile(MultipartFile file, String subDirectory);

    /**
     * Loads a stored file as a Spring Resource for streaming or downloading.
     */
    Resource loadFileAsResource(String fileName, String subDirectory);

    /**
     * Deletes a stored file from storage.
     */
    boolean deleteFile(String fileName, String subDirectory);

    /**
     * Returns the absolute Path to the stored file.
     */
    Path getFilePath(String fileName, String subDirectory);

    /**
     * Validates audio file extension, MIME type, and size.
     */
    void validateAudioFile(MultipartFile file);
}

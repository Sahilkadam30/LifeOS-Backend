package com.life.controller.music;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.life.dto.music.MusicStatsDTO;
import com.life.dto.music.RecordingRequestDTO;
import com.life.dto.music.RecordingResponseDTO;
import com.life.entity.music.MusicIdeaStatus;
import com.life.entity.music.RecordingType;
import com.life.service.music.RecordingService;
import com.life.service.music.RecordingService.AudioResourceInfo;

@RestController
@RequestMapping("/api/music")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class RecordingController {

    private final RecordingService recordingService;

    public RecordingController(RecordingService recordingService) {
        this.recordingService = recordingService;
    }

    @PostMapping(value = "/recordings/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RecordingResponseDTO> uploadRecording(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "recordingType", required = false) RecordingType recordingType,
            @RequestParam(value = "recordedAt", required = false) String recordedAtStr,
            @RequestParam(value = "duration", required = false) Double duration,
            @RequestParam(value = "favorite", required = false) Boolean favorite,
            @RequestParam(value = "status", required = false) MusicIdeaStatus status,
            @RequestParam(value = "practiceNotes", required = false) String practiceNotes,
            @RequestParam(value = "practiceMinutes", required = false) Integer practiceMinutes,
            @RequestParam(value = "projectId", required = false) Long projectId) {

        RecordingRequestDTO req = new RecordingRequestDTO();
        req.setTitle(title);
        req.setDescription(description);
        req.setRecordingType(recordingType != null ? recordingType : RecordingType.SONG);
        req.setDuration(duration != null ? duration : 0.0);
        req.setFavorite(favorite);
        req.setStatus(status);
        req.setPracticeNotes(practiceNotes);
        req.setPracticeMinutes(practiceMinutes);
        req.setProjectId(projectId);

        if (recordedAtStr != null && !recordedAtStr.trim().isEmpty()) {
            try {
                req.setRecordedAt(LocalDateTime.parse(recordedAtStr, DateTimeFormatter.ISO_DATE_TIME));
            } catch (Exception e) {
                try {
                    req.setRecordedAt(LocalDateTime.parse(recordedAtStr + "T00:00:00"));
                } catch (Exception ignored) {
                    req.setRecordedAt(LocalDateTime.now());
                }
            }
        } else {
            req.setRecordedAt(LocalDateTime.now());
        }

        RecordingResponseDTO created = recordingService.uploadRecording(file, req);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/recordings")
    public ResponseEntity<List<RecordingResponseDTO>> getAllRecordings(
            @RequestParam(required = false) RecordingType type,
            @RequestParam(required = false) Boolean favorite,
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false, defaultValue = "newest") String sort) {
        return ResponseEntity.ok(recordingService.getAllRecordings(type, favorite, projectId, search, sort));
    }

    @GetMapping("/recordings/{id:[0-9]+}")
    public ResponseEntity<RecordingResponseDTO> getRecordingById(@PathVariable Long id) {
        return ResponseEntity.ok(recordingService.getRecordingById(id));
    }

    @PutMapping("/recordings/{id:[0-9]+}")
    public ResponseEntity<RecordingResponseDTO> updateRecording(
            @PathVariable Long id,
            @RequestBody RecordingRequestDTO req) {
        return ResponseEntity.ok(recordingService.updateRecording(id, req));
    }

    @DeleteMapping("/recordings/{id:[0-9]+}")
    public ResponseEntity<Void> deleteRecording(@PathVariable Long id) {
        recordingService.deleteRecording(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/recordings/{id:[0-9]+}/favorite")
    public ResponseEntity<RecordingResponseDTO> toggleFavorite(@PathVariable Long id) {
        return ResponseEntity.ok(recordingService.toggleFavorite(id));
    }

    @GetMapping("/recordings/favorites")
    public ResponseEntity<List<RecordingResponseDTO>> getFavorites() {
        return ResponseEntity.ok(recordingService.getFavorites());
    }

    @GetMapping("/recordings/type/{type}")
    public ResponseEntity<List<RecordingResponseDTO>> getRecordingsByType(@PathVariable RecordingType type) {
        return ResponseEntity.ok(recordingService.getRecordingsByType(type));
    }

    @GetMapping("/stats")
    public ResponseEntity<MusicStatsDTO> getMusicStats() {
        return ResponseEntity.ok(recordingService.getMusicStats());
    }

    /**
     * Authenticated audio streaming endpoint supporting HTTP 206 Partial Content for instant seeking.
     * The optional `token` query param lets the HTML5 <audio> element authenticate
     * because browsers cannot send a custom Authorization header for media URLs.
     */
    @GetMapping("/recordings/{id:[0-9]+}/stream")
    public ResponseEntity<StreamingResponseBody> streamAudio(
            @PathVariable Long id,
            @RequestParam(value = "token", required = false) String tokenParam,
            @RequestParam(value = "userId", required = false) Long userIdParam,
            @RequestHeader(value = "userId", required = false) Long userIdHeader,
            @RequestHeader HttpHeaders headers) throws IOException {

        Long effectiveUserId = userIdParam != null ? userIdParam : userIdHeader;
        AudioResourceInfo info = recordingService.getAudioResource(id, tokenParam, effectiveUserId);
        Resource resource = info.getResource();
        long contentLength = resource.contentLength();

        MediaType mediaType;
        try {
            mediaType = (info.getMimeType() != null && !info.getMimeType().trim().isEmpty())
                    ? MediaType.parseMediaType(info.getMimeType())
                    : MediaType.valueOf("audio/mpeg");
        } catch (Exception e) {
            mediaType = MediaType.valueOf("audio/mpeg");
        }

        List<HttpRange> ranges = headers.getRange();
        long start = 0;
        long end = contentLength > 0 ? contentLength - 1 : 0;
        boolean isPartial = false;

        if (!ranges.isEmpty() && contentLength > 0) {
            HttpRange range = ranges.get(0);
            start = range.getRangeStart(contentLength);
            end = range.getRangeEnd(contentLength);
            isPartial = true;
        }

        final long finalStart = start;
        final long finalEnd = end;
        final long rangeLength = Math.max(0, finalEnd - finalStart + 1);

        StreamingResponseBody responseBody = outputStream -> {
            try (InputStream is = resource.getInputStream()) {
                if (finalStart > 0) {
                    long skipped = is.skip(finalStart);
                    while (skipped < finalStart) {
                        long additionalSkipped = is.skip(finalStart - skipped);
                        if (additionalSkipped <= 0) break;
                        skipped += additionalSkipped;
                    }
                }
                byte[] buffer = new byte[16384];
                long remaining = rangeLength;
                while (remaining > 0) {
                    int toRead = (int) Math.min(buffer.length, remaining);
                    int read = is.read(buffer, 0, toRead);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(buffer, 0, read);
                    remaining -= read;
                }
                outputStream.flush();
            }
        };

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(mediaType);
        responseHeaders.set(HttpHeaders.ACCEPT_RANGES, "bytes");
        responseHeaders.setContentLength(rangeLength);
        responseHeaders.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + info.getOriginalFilename() + "\"");

        if (isPartial) {
            responseHeaders.set(HttpHeaders.CONTENT_RANGE, String.format("bytes %d-%d/%d", finalStart, finalEnd, contentLength));
            return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.PARTIAL_CONTENT);
        } else {
            return new ResponseEntity<>(responseBody, responseHeaders, HttpStatus.OK);
        }
    }

    /**
     * Authenticated audio download endpoint.
     */
    @GetMapping("/recordings/{id:[0-9]+}/download")
    public ResponseEntity<Resource> downloadAudio(
            @PathVariable Long id,
            @RequestParam(value = "token", required = false) String tokenParam,
            @RequestParam(value = "userId", required = false) Long userIdParam,
            @RequestHeader(value = "userId", required = false) Long userIdHeader) {

        Long effectiveUserId = userIdParam != null ? userIdParam : userIdHeader;
        AudioResourceInfo info = recordingService.getAudioResource(id, tokenParam, effectiveUserId);

        MediaType mediaType;
        try {
            mediaType = (info.getMimeType() != null && !info.getMimeType().trim().isEmpty())
                    ? MediaType.parseMediaType(info.getMimeType())
                    : MediaType.APPLICATION_OCTET_STREAM;
        } catch (Exception e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + info.getOriginalFilename() + "\"")
                .header(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.CONTENT_DISPOSITION)
                .body(info.getResource());
    }
}

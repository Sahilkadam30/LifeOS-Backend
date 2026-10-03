package com.life.repository.music;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.life.entity.User;
import com.life.entity.music.Recording;
import com.life.entity.music.RecordingType;

@Repository
public interface RecordingRepository extends JpaRepository<Recording, Long> {

    List<Recording> findByUserOrderByRecordedAtDesc(User user);

    Optional<Recording> findByIdAndUser(Long id, User user);

    List<Recording> findByUserAndRecordingTypeOrderByRecordedAtDesc(User user, RecordingType recordingType);

    List<Recording> findByUserAndFavoriteTrueOrderByRecordedAtDesc(User user);

    List<Recording> findByUserAndProjectIdOrderByRecordedAtDesc(User user, Long projectId);

    long countByUser(User user);

    long countByUserAndRecordingType(User user, RecordingType recordingType);

    long countByUserAndFavoriteTrue(User user);

    @Query("SELECT r FROM Recording r WHERE r.user = :user AND " +
           "(LOWER(r.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(r.description) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(r.practiceNotes) LIKE LOWER(CONCAT('%', :query, '%'))) " +
           "ORDER BY r.recordedAt DESC")
    List<Recording> searchByUserAndQuery(@Param("user") User user, @Param("query") String query);

    @Query("SELECT COUNT(r) FROM Recording r WHERE r.user = :user AND " +
           "(r.recordingType = com.life.entity.music.RecordingType.PRACTICE OR r.practiceMinutes IS NOT NULL)")
    long countTotalPracticeSessions(@Param("user") User user);

    @Query("SELECT COUNT(r) FROM Recording r WHERE r.user = :user AND " +
           "(r.recordingType = com.life.entity.music.RecordingType.PRACTICE OR r.practiceMinutes IS NOT NULL) AND " +
           "r.recordedAt >= :since")
    long countPracticeSessionsSince(@Param("user") User user, @Param("since") LocalDateTime since);

    @Query("SELECT COALESCE(SUM(r.practiceMinutes), 0) FROM Recording r WHERE r.user = :user")
    Long sumPracticeMinutesByUser(@Param("user") User user);

    @Query("SELECT COALESCE(SUM(r.duration), 0.0) FROM Recording r WHERE r.user = :user AND " +
           "r.recordingType = com.life.entity.music.RecordingType.PRACTICE")
    Double sumPracticeDurationByUser(@Param("user") User user);
}

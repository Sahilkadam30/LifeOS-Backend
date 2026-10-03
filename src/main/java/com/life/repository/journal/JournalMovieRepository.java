package com.life.repository.journal;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.life.entity.journal.JournalMovie;
import com.life.entity.journal.MovieStatus;

@Repository
public interface JournalMovieRepository extends JpaRepository<JournalMovie, Long> {
    List<JournalMovie> findByUserIdOrderByIdDesc(Long userId);
    List<JournalMovie> findByUserIdAndStatusOrderByIdDesc(Long userId, MovieStatus status);
    long countByUserId(Long userId);
}

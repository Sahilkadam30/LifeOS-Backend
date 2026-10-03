package com.life.repository.journal;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.life.entity.journal.JournalBook;
import com.life.entity.journal.BookStatus;

@Repository
public interface JournalBookRepository extends JpaRepository<JournalBook, Long> {
    List<JournalBook> findByUserIdOrderByIdDesc(Long userId);
    List<JournalBook> findByUserIdAndStatusOrderByIdDesc(Long userId, BookStatus status);
    long countByUserId(Long userId);
}

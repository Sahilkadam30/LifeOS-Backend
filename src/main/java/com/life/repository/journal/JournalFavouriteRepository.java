package com.life.repository.journal;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.life.entity.journal.JournalFavourite;

@Repository
public interface JournalFavouriteRepository extends JpaRepository<JournalFavourite, Long> {
    List<JournalFavourite> findByUserIdOrderByIdDesc(Long userId);
    List<JournalFavourite> findByUserIdAndCategoryIdOrderByIdDesc(Long userId, Long categoryId);
    long countByUserId(Long userId);
}

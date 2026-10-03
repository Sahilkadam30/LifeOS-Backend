package com.life.repository.journal;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.life.entity.journal.JournalFavouriteCategory;

@Repository
public interface JournalFavouriteCategoryRepository extends JpaRepository<JournalFavouriteCategory, Long> {
    List<JournalFavouriteCategory> findByUserIdOrderByNameAsc(Long userId);
    Optional<JournalFavouriteCategory> findByUserIdAndNameIgnoreCase(Long userId, String name);
}

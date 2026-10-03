package com.life.repository.journal;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.life.entity.journal.JournalFood;
import com.life.entity.journal.FoodStatus;

@Repository
public interface JournalFoodRepository extends JpaRepository<JournalFood, Long> {
    List<JournalFood> findByUserIdOrderByIdDesc(Long userId);
    List<JournalFood> findByUserIdAndStatusOrderByIdDesc(Long userId, FoodStatus status);
    long countByUserId(Long userId);
}

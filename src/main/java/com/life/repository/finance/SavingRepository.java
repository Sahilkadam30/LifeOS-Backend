package com.life.repository.finance;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.life.entity.finance.Saving;

public interface SavingRepository extends JpaRepository<Saving, Long>{

	List<Saving> findByUserId(Long userId);

    List<Saving> findByUserIdAndSavingDateBetween(
            Long userId,
            LocalDate start,
            LocalDate end
    );
}

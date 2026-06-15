package com.life.repository.finance;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.life.entity.finance.Expense;

public interface ExpenseRepository extends JpaRepository<Expense, Long>{

	List<Expense> findByUserId(Long userId);

    List<Expense> findByUserIdAndExpenseDateBetween(
            Long userId,
            LocalDate start,
            LocalDate end
    );
}

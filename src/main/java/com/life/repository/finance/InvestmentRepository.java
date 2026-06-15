package com.life.repository.finance;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.life.entity.finance.Investment;

public interface InvestmentRepository extends JpaRepository<Investment, Long>{

	List<Investment> findByUserId(Long userId);
}

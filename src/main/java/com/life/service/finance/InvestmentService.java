package com.life.service.finance;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.life.entity.User;
import com.life.entity.finance.Investment;
import com.life.repository.UserRepository;
import com.life.repository.finance.InvestmentRepository;

@Service
public class InvestmentService {

    private final InvestmentRepository investmentRepository;
    private final UserRepository userRepository;

    public InvestmentService(
            InvestmentRepository investmentRepository,
            UserRepository userRepository) {

        this.investmentRepository = investmentRepository;
        this.userRepository = userRepository;
    }

    public Investment create(
            Investment investment) {

        User user = getCurrentUser();

        investment.setUserId(user.getId());

        return investmentRepository.save(
                investment
        );
    }

    public List<Investment> getAll() {

        User user = getCurrentUser();

        return investmentRepository.findByUserId(
                user.getId()
        );
    }

    public Investment update(
            Long id,
            Investment request) {

        User user = getCurrentUser();

        Investment investment =
                investmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Investment not found"));

        if (!investment.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        investment.setInvestmentName(
                request.getInvestmentName());

        investment.setInvestmentType(
                request.getInvestmentType());

        investment.setInvestedAmount(
                request.getInvestedAmount());

        investment.setCurrentValue(
                request.getCurrentValue());

        investment.setInvestmentDate(
                request.getInvestmentDate());

        investment.setNotes(
                request.getNotes());

        return investmentRepository.save(
                investment
        );
    }

    public void delete(Long id) {

        User user = getCurrentUser();

        Investment investment =
                investmentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Investment not found"));

        if (!investment.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        investmentRepository.delete(
                investment
        );
    }

    public Double getInvestmentValue() {

        return getAll()
                .stream()
                .mapToDouble(
                        Investment::getCurrentValue)
                .sum();
    }

    private User getCurrentUser() {

        String username =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
}

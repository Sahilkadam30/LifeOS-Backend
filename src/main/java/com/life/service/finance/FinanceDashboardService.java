package com.life.service.finance;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.life.dto.finance.FinanceDashboardResponse;
import com.life.dto.finance.ExpenseChartDto;
import com.life.dto.finance.InvestmentChartDto;
import com.life.dto.finance.SavingsChartDto;
import com.life.entity.User;
import com.life.entity.finance.Expense;
import com.life.entity.finance.Investment;
import com.life.entity.finance.Saving;
import com.life.repository.UserRepository;
import com.life.repository.finance.ExpenseRepository;
import com.life.repository.finance.InvestmentRepository;
import com.life.repository.finance.SavingRepository;

@Service
public class FinanceDashboardService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final SavingRepository savingRepository;
    private final InvestmentRepository investmentRepository;

    public FinanceDashboardService(
            ExpenseRepository expenseRepository,
            UserRepository userRepository,
            SavingRepository savingRepository,
            InvestmentRepository investmentRepository) {

        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
        this.savingRepository = savingRepository;
        this.investmentRepository = investmentRepository;
    }

    private List<SavingsChartDto>
    getSavingsTrend(Long userId) {

        List<Saving> savings =
                savingRepository.findByUserId(userId);

        Map<Month, Double> monthMap =
                new TreeMap<>();

        for (Saving s : savings) {

            Month month =
                    s.getSavingDate().getMonth();

            monthMap.put(
                    month,
                    monthMap.getOrDefault(
                            month,
                            0.0
                    ) + s.getAmount()
            );
        }

        List<SavingsChartDto> list =
                new ArrayList<>();

        monthMap.forEach((m, a) -> {

            list.add(
                    new SavingsChartDto(
                            m.name(),
                            a
                    )
            );
        });

        return list;
    }
    
    private List<InvestmentChartDto>
    getInvestmentTrend(Long userId) {

        List<Investment> investments =
                investmentRepository.findByUserId(userId);

        Map<Month, InvestmentChartDto> map =
                new TreeMap<>();

        for (Investment investment :
                investments) {

            Month month =
                    investment
                            .getInvestmentDate()
                            .getMonth();

            InvestmentChartDto dto =
                    map.getOrDefault(
                            month,
                            new InvestmentChartDto(
                                    month.name(),
                                    0.0,
                                    0.0
                            )
                    );

            dto.setInvestedAmount(
                    dto.getInvestedAmount()
                    + investment.getInvestedAmount()
            );

            dto.setCurrentValue(
                    dto.getCurrentValue()
                    + investment.getCurrentValue()
            );

            map.put(month, dto);
        }

        return new ArrayList<>(
                map.values()
        );
    }
    
    private List<ExpenseChartDto>
    getExpenseBreakdown(Long userId) {

        List<Expense> expenses =
                expenseRepository.findByUserId(userId);

        Map<String, Double> categoryMap =
                new HashMap<>();

        for (Expense expense :
                expenses) {

            categoryMap.put(
                    expense.getCategory(),
                    categoryMap.getOrDefault(
                            expense.getCategory(),
                            0.0
                    ) + expense.getAmount()
            );
        }

        List<ExpenseChartDto> result =
                new ArrayList<>();

        categoryMap.forEach((cat, amt) -> {

            result.add(
                    new ExpenseChartDto(
                            cat,
                            amt
                    )
            );
        });

        return result;
    }
    
    
    public FinanceDashboardResponse getDashboard() {

        User user = getCurrentUser();

        LocalDate start =
                LocalDate.now()
                        .withDayOfMonth(1);

        LocalDate end =
                LocalDate.now();

        /*
         * Monthly Expenses
         */
        List<Expense> expenses =
                expenseRepository
                        .findByUserIdAndExpenseDateBetween(
                                user.getId(),
                                start,
                                end
                        );

        double monthlyExpenses =
                expenses.stream()
                        .mapToDouble(
                                Expense::getAmount
                        )
                        .sum();

        /*
         * Total Savings
         */
        double totalSavings =
                savingRepository
                        .findByUserId(
                                user.getId()
                        )
                        .stream()
                        .mapToDouble(
                                Saving::getAmount
                        )
                        .sum();

        /*
         * Total Investment Value
         */
        double investmentValue =
                investmentRepository
                        .findByUserId(
                                user.getId()
                        )
                        .stream()
                        .mapToDouble(
                                Investment::getCurrentValue
                        )
                        .sum();

        /*
         * Net Growth
         */
        double netGrowth =
                totalSavings
                + investmentValue
                - monthlyExpenses;

        FinanceDashboardResponse dto =
                new FinanceDashboardResponse();

        dto.setCurrentSavings(
                totalSavings
        );

        dto.setMonthlyExpenses(
                monthlyExpenses
        );

        dto.setInvestmentValue(
                investmentValue
        );

        dto.setNetGrowth(
                netGrowth
        );

        /*
         * Chart Data
         */
        dto.setSavingsTrend(
                getSavingsTrend(
                        user.getId()
                )
        );

        dto.setInvestmentTrend(
                getInvestmentTrend(
                        user.getId()
                )
        );

        dto.setExpenseBreakdown(
                getExpenseBreakdown(
                        user.getId()
                )
        );

        return dto;
    }

    private User getCurrentUser() {
        String username =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository
                .findByUsername(username)
                .orElseThrow();
    }
}

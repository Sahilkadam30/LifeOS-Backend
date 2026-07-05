package com.life.service.chat;

import java.util.List;

import org.springframework.stereotype.Service;

import com.life.entity.finance.Expense;
import com.life.repository.finance.ExpenseRepository;

@Service
public class AIFinanceAdvisorService {

    private final ExpenseRepository expenseRepository;
    
    public AIFinanceAdvisorService(ExpenseRepository expenseRepository) {
    	this.expenseRepository=expenseRepository;
    }

    public String buildFinanceContext(Long userId) {

        List<Expense> expenses =
                expenseRepository.findByUserId(userId);

        StringBuilder sb = new StringBuilder();

        sb.append("Finance Summary:\n");

        for(Expense expense : expenses) {

            sb.append(
                    expense.getCategory()
                    + " : "
                    + expense.getAmount()
                    + "\n"
            );
        }

        return sb.toString();
    }
}
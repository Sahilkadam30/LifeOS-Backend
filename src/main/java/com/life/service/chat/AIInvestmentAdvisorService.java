package com.life.service.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.life.entity.finance.Investment;
import com.life.repository.finance.InvestmentRepository;

@Service
public class AIInvestmentAdvisorService {

    @Autowired
    private InvestmentRepository repository;

    public String buildInvestmentContext(Long userId) {

        List<Investment> investments =
                repository.findByUserId(userId);

        if(investments.isEmpty()) {
            return "No investment data found.";
        }

        StringBuilder context =
                new StringBuilder();

        context.append("Investment Portfolio:\n\n");

        for(Investment inv : investments) {

            double invested =
                    inv.getInvestedAmount();

            double current =
                    inv.getCurrentValue();

            double profit =
                    current - invested;

            double roi =
                    (profit / invested) * 100;

            context.append(
                    "Investment: "
                    + inv.getInvestmentName()
                    + "\nType: "
                    + inv.getInvestmentType()
                    + "\nInvested: ₹"
                    + invested
                    + "\nCurrent Value: ₹"
                    + current
                    + "\nProfit: ₹"
                    + profit
                    + "\nROI: "
                    + String.format("%.2f", roi)
                    + "%\n\n"
            );
        }

        return context.toString();
    }
}

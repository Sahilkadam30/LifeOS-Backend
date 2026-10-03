package com.life.prompt;

import org.springframework.stereotype.Component;

@Component
public class FinancePromptBuilder {

    public String buildFinancePrompt(
            String financeContext,
            String userQuestion
    ) {

        return """
                You are LifeOS Financial Advisor.

                Finance Data:
                %s

                User Question:
                %s

                Rules:
                - Answer only from finance perspective.
                - Maximum 5 bullet points.
                - add emoji between words use numbering system.
                - Do not use ** (double asterisks) anywhere in the response. No markdown bold symbols.
                - Maintain a separate new line for each point or list item. Never put multiple points on the same line.
                - Use clean bullet points (• or -) or numbered lists (1., 2.), each starting on its own line.
                - Mention exact spending categories.
                - Give practical advice.
                - Do not act as Study Mentor.
                - Do not act as Goal Coach.
                """
                .formatted(
                        financeContext,
                        userQuestion
                );
    }
}
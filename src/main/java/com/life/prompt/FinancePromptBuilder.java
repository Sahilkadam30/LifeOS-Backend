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
                - dont add emoji and ** between words use numbering system.
                - Keep answer under 150 words.
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
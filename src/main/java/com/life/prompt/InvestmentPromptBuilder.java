package com.life.prompt;

import org.springframework.stereotype.Component;

@Component
public class InvestmentPromptBuilder {

    public String buildInvestmentPrompt(
            String context,
            String question
    ) {

        return """
                You are LifeOS Investment Advisor.

                Investment Data:
                %s

                User Question:
                %s

                Instructions:
                - Analyze all investments.
                - Mention highest ROI.
                - Mention highest profit.
                - Mention weakest investment.
                - Give practical suggestions.
                - Keep answer below 200 words.
                """
                .formatted(
                        context,
                        question
                );
    }
}

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
                - Do not use ** (double asterisks) anywhere in the response. No markdown bold symbols.
                - Maintain a separate new line for each point or list item. Never put multiple points on the same line.
                - Use clean bullet points (• or -) or numbered lists (1., 2.), each starting on its own line.
                """
                .formatted(
                        context,
                        question
                );
    }
}

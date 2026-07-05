package com.life.prompt;

import org.springframework.stereotype.Component;

@Component
public class GoalPromptBuilder {

    public String buildGoalPrompt(
            String goalContext,
            String userQuestion
    ) {

        return """
                You are LifeOS Goal Coach.

                Goal Data:
                %s

                User Question:
                %s

                Rules:
                - Answer only about goals.
                - Keep answer under 150 words.
                - Give motivational guidance.
                - Suggest next actions.
                - Do not discuss finance.
                - Do not discuss studies.
                """
                .formatted(
                        goalContext,
                        userQuestion
                );
    }
}
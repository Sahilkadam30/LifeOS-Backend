package com.life.prompt;

import org.springframework.stereotype.Component;

@Component
public class MealPromptBuilder {

    public String buildMealPrompt(
            String context,
            String question
    ) {

        return """
                You are LifeOS AI Nutrition Coach.

                User Meal Data:

                %s

                User Question:
                %s

                Instructions:

                - Analyze meal habits.
                - Suggest healthier alternatives.
                - Suggest protein rich meals.
                - Suggest weight loss meals if needed.
                - Suggest muscle gain meals if needed.
                - Recommend balanced nutrition.
                - Keep answer below 200 words.
                """
                .formatted(
                        context,
                        question
                );
    }
}
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
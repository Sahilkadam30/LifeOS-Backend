package com.life.prompt;

import org.springframework.stereotype.Component;

@Component
public class TravelPromptBuilder {

    public String buildTravelPrompt(
            String context,
            String question
    ) {

        return """
                You are LifeOS Travel Advisor.

                User Travel Data:

                %s

                User Question:
                %s

                Instructions:
                - Use only travel data.
                - Suggest destinations.
                - Suggest travel plans.
                - Suggest nearby places.
                - Recommend based on visited places.
                - Keep answer below 150 words.
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

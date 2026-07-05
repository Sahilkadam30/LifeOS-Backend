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
                """
                .formatted(
                        context,
                        question
                );
    }
}

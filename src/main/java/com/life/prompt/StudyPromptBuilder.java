package com.life.prompt;

import org.springframework.stereotype.Component;

@Component
public class StudyPromptBuilder {

    public String buildStudyPrompt(
            String studyContext,
            String userQuestion
    ) {

        return """
                You are LifeOS Study Mentor.

                Study Data:
                %s

                User Question:
                %s

                Rules:
                - Answer only about learning.
                - Maximum 5 bullet points.
                - Keep answer under 150 words.
                - Mention weak skills.
                - Suggest next topics.
                - Do not discuss finance.
                - Do not discuss goals.
                """
                .formatted(
                        studyContext,
                        userQuestion
                );
    }
}
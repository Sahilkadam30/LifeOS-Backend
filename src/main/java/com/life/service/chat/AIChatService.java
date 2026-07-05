package com.life.service.chat;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.life.prompt.FinancePromptBuilder;
import com.life.prompt.GoalPromptBuilder;
import com.life.prompt.InvestmentPromptBuilder;
import com.life.prompt.MealPromptBuilder;
import com.life.prompt.StudyPromptBuilder;
import com.life.prompt.TravelPromptBuilder;

@Service
public class AIChatService {

    @Value("${gemini.api.key}")
    private String apiKey;
    
    @Autowired
    private AIFinanceAdvisorService financeService;

    @Autowired
    private AIStudyMentorService studyService;

    @Autowired
    private AIGoalCoachService goalService;

    @Autowired
    private FinancePromptBuilder financePromptBuilder;

    @Autowired
    private StudyPromptBuilder studyPromptBuilder;

    @Autowired
    private GoalPromptBuilder goalPromptBuilder;
    
    @Autowired
    private AIInvestmentAdvisorService investmentService;

    @Autowired
    private InvestmentPromptBuilder investmentPromptBuilder;
    
    @Autowired
    private AITravelAdvisorService travelService;

    @Autowired
    private TravelPromptBuilder travelPromptBuilder;
    
    @Autowired
    private AIMealPlannerService mealService;

    @Autowired
    private MealPromptBuilder mealPromptBuilder;

    public String askGemini(String userQuestion,Long userId) {

        String url =
                "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key="
                        + apiKey;
        
        RestTemplate restTemplate = new RestTemplate();

        String intent = detectIntent(userQuestion);

        String prompt;

        if("INVESTMENT".equals(intent)) {

            String investmentContext =
                    investmentService
                            .buildInvestmentContext(userId);

            prompt =
                    investmentPromptBuilder
                            .buildInvestmentPrompt(
                                    investmentContext,
                                    userQuestion
                            );
        }
        else if("MEAL".equals(intent)) {

            String mealContext =
                    mealService.buildMealContext(userId);

            prompt =
                    mealPromptBuilder.buildMealPrompt(
                            mealContext,
                            userQuestion
                    );
        }
        else if("TRAVEL".equals(intent)) {

            String travelContext =
                    travelService.buildTravelContext(userId);

            prompt =
                    travelPromptBuilder.buildTravelPrompt(
                            travelContext,
                            userQuestion
                    );
        }
        else if("FINANCE".equals(intent)) {

            String financeContext =
                    financeService.buildFinanceContext(userId);

            prompt =
                    financePromptBuilder.buildFinancePrompt(
                            financeContext,
                            userQuestion
                    );
        }
        else if("STUDY".equals(intent)) {

            String studyContext =
                    studyService.buildStudyContext(userId);

            prompt =
                    studyPromptBuilder.buildStudyPrompt(
                            studyContext,
                            userQuestion
                    );
        }
        else if("GOAL".equals(intent)) {

            String goalContext =
                    goalService.buildGoalContext(userId);

            prompt =
                    goalPromptBuilder.buildGoalPrompt(
                            goalContext,
                            userQuestion
                    );
        }
        else {

            prompt =
                    """
                    You are LifeOS AI Assistant.

                    Answer briefly and helpfully.

                    User Question:
                    """
                    + userQuestion;
        }
        
        System.out.println("\n========== INTENT ==========");
        System.out.println(intent);

        System.out.println("\n========== PROMPT ==========");
        System.out.println(prompt);

        System.out.println("\n============================\n");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = Map.of(
                "contents",
                List.of(
                        Map.of(
                                "parts",
                                List.of(
                                        Map.of(
                                                "text",
                                                prompt
                                        )
                                )
                        )
                )
        );

        HttpEntity<Map<String, Object>> entity =
                new HttpEntity<>(requestBody, headers);

        ResponseEntity<Map> response;

        try {

            response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.POST,
                            entity,
                            Map.class
                    );

        }
        catch (HttpServerErrorException.ServiceUnavailable e) {

            return """
                    Gemini is currently overloaded.
                    Please try again in a few seconds.
                    """;
        }
        catch (Exception e) {

            e.printStackTrace();

            return """
                    AI service unavailable.
                    Please try again later.
                    """;
        }

        System.out.println(response.getBody());

        List<Map<String, Object>> candidates =
                (List<Map<String, Object>>) response.getBody().get("candidates");

        if (candidates == null || candidates.isEmpty()) {
            return "No response from Gemini.";
        }

        Map<String, Object> content =
                (Map<String, Object>) candidates.get(0).get("content");

        List<Map<String, Object>> parts =
                (List<Map<String, Object>>) content.get("parts");
        
        

        return (String) parts.get(0).get("text");
    }
    
    private String detectIntent(String question) {

        String q = question.toLowerCase();

        
        if(q.contains("travel")
                || q.contains("trip")
                || q.contains("vacation")
                || q.contains("tour")
                || q.contains("destination")
                || q.contains("holiday")
                || q.contains("place")) {

            return "TRAVEL";
        }
        if(q.contains("meal")
                || q.contains("diet")
                || q.contains("food")
                || q.contains("nutrition")
                || q.contains("protein")
                || q.contains("breakfast")
                || q.contains("lunch")
                || q.contains("dinner")
                || q.contains("weight loss")) {

            return "MEAL";
        }
        if(q.contains("investment")
                || q.contains("stock")
                || q.contains("mutual fund")
                || q.contains("sip")
                || q.contains("profit")
                || q.contains("roi")
                || q.contains("portfolio")) {

            return "INVESTMENT";
        }
        if (q.contains("money")
                || q.contains("expense")
                || q.contains("save")
                || q.contains("budget")
                || q.contains("investment")) {

            return "FINANCE";
        }
        if (q.contains("study")
                || q.contains("learn")
                || q.contains("skill")
                || q.contains("java")
                || q.contains("interview")) {

            return "STUDY";
        }

        if (q.contains("goal")
                || q.contains("motivation")
                || q.contains("habit")
                || q.contains("discipline")) {

            return "GOAL";
        }

        return "GENERAL";
    }
}
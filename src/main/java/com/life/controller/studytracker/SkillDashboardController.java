package com.life.controller.studytracker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.life.dto.studytracker.SkillDashboardResponse;
import com.life.service.studytracker.SkillDashboardService;

@RestController
@RequestMapping("/api/skills/dashboard")
public class SkillDashboardController {

    private final SkillDashboardService service;

    public SkillDashboardController(
            SkillDashboardService service) {

        this.service = service;
    }

    @GetMapping
    public SkillDashboardResponse getDashboard() {

        return service.getDashboard();
    }
}

package com.life.controller.finance;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.life.dto.finance.FinanceDashboardResponse;
import com.life.service.finance.FinanceDashboardService;

@RestController
@RequestMapping("/api/finance/dashboard")
public class FinanceDashboardController {

    private final FinanceDashboardService service;
    

    public FinanceDashboardController(
            FinanceDashboardService service) {

        this.service = service;
    }

    @GetMapping
    public FinanceDashboardResponse dashboard() {

        return service.getDashboard();
    }
}

package com.life.controller.finance;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.life.entity.finance.Investment;
import com.life.service.finance.InvestmentService;

@RestController
@RequestMapping("/api/finance/investments")
public class InvestmentController {

    private final InvestmentService service;

    public InvestmentController(
            InvestmentService service) {

        this.service = service;
    }

    @GetMapping
    public List<Investment> getAll() {

        return service.getAll();
    }

    @PostMapping
    public Investment create(
            @RequestBody Investment investment) {

        return service.create(investment);
    }

    @PutMapping("/{id}")
    public Investment update(
            @PathVariable Long id,
            @RequestBody Investment investment) {

        return service.update(id, investment);
    }

    
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {
    	System.out.println("DELETE SAVING CALLED -> " + id);
        service.delete(id);
    }

    @GetMapping("/value")
    public Double totalValue() {

        return service.getInvestmentValue();
    }
}
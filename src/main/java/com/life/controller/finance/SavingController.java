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

import com.life.entity.finance.Saving;
import com.life.service.finance.SavingService;

@RestController
@RequestMapping("/api/finance/savings")
public class SavingController {

    private final SavingService service;

    public SavingController(
            SavingService service) {

        this.service = service;
    }

    @GetMapping
    public List<Saving> getAll() {

        return service.getAll();
    }

    @PostMapping
    public Saving create(
            @RequestBody Saving saving) {

        return service.create(saving);
    }

    @PutMapping("/{id}")
    public Saving update(
            @PathVariable Long id,
            @RequestBody Saving saving) {

        return service.update(id, saving);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id) {
    	System.out.println("DELETE SAVING CALLED -> " + id);
        service.delete(id);
    }

    @GetMapping("/total")
    public Double totalSavings() {

        return service.getTotalSavings();
    }
}

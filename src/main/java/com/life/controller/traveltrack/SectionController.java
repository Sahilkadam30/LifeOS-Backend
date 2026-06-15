package com.life.controller.traveltrack;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.life.entity.traveltrack.Section;
import com.life.service.traveltrack.SectionService;


@RestController
@RequestMapping("/api/sections")
@CrossOrigin
public class SectionController {

	@Autowired
    private SectionService service;

    @PostMapping
    public Section save(@RequestBody Section section) {
        return service.save(section);
    }

    @GetMapping
    public List<Section> getAll() {
        return service.getAll();
    }

    @PutMapping("/{id}")
    public Section update(
            @PathVariable Long id,
            @RequestBody Section section
    ) {
        return service.update(id, section);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}

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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.life.entity.traveltrack.VisitedPlace;
import com.life.service.traveltrack.VisitedPlaceService;

@RestController
@RequestMapping("/api/visited")
@CrossOrigin
public class VisitedPlaceController {

	@Autowired
	private VisitedPlaceService service;

    @PostMapping
    public VisitedPlace add(@RequestBody VisitedPlace place,
                            @RequestHeader("userId") Long userId) {
        return service.save(place, userId);
    }

    @GetMapping
    public List<VisitedPlace> getAll(@RequestHeader("userId") Long userId) {
        return service.getAll(userId);
    }
    
    @PutMapping("/{id}")
    public VisitedPlace updateVisited(
            @PathVariable Long id,
            @RequestBody VisitedPlace place
    ) {
        return service.updateVisited(id, place);
    }

    @DeleteMapping("/{id}")
    public void deleteVisited(@PathVariable Long id) {
        service.deleteVisited(id);
    }
}

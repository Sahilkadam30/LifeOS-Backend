package com.life.controller.traveltrack;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.life.entity.traveltrack.WishlistPlace;
import com.life.service.traveltrack.WishlistService;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin
public class WishlistController {

	@Autowired
	 private WishlistService service;

	@PostMapping
    public WishlistPlace add(@RequestBody WishlistPlace place,
                             @RequestHeader("userId") Long userId) {
        return service.save(place, userId);
    }

    @GetMapping
    public List<WishlistPlace> getAll(@RequestHeader("userId") Long userId) {
        return service.getAll(userId);
    }
}

package com.life.service.traveltrack;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.life.entity.traveltrack.WishlistPlace;
import com.life.repository.traveltrack.WishlistRepository;

@Service
public class WishlistService {

	@Autowired
	private  WishlistRepository repo;

    public WishlistPlace save(WishlistPlace place, Long userId) {
        place.setUserId(userId);
        return repo.save(place);
    }

    public List<WishlistPlace> getAll(Long userId) {
        return repo.findByUserId(userId);
    }
}

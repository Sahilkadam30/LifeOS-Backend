package com.life.service.traveltrack;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.life.entity.traveltrack.VisitedPlace;
import com.life.repository.traveltrack.VisitedPlaceRepository;

@Service
public class VisitedPlaceService {

	@Autowired
	private  VisitedPlaceRepository repo;

    public VisitedPlace save(VisitedPlace place, Long userId) {
        place.setUserId(userId);
        return repo.save(place);
    }

    public List<VisitedPlace> getAll(Long userId) {
        return repo.findByUserId(userId);
    }
    
    public VisitedPlace updateVisited(Long id, VisitedPlace updated) {

        VisitedPlace existing = repo.findById(id)
                .orElseThrow();

        existing.setPlaceName(updated.getPlaceName());
        existing.setCity(updated.getCity());
        existing.setType(updated.getType());
        existing.setVisitedOn(updated.getVisitedOn());
        existing.setLatitude(updated.getLatitude());
        existing.setLongitude(updated.getLongitude());

        return repo.save(existing);
    }

    public void deleteVisited(Long id) {
    	repo.deleteById(id);
    }
}

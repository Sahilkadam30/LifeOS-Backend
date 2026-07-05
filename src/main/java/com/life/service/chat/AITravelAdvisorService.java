package com.life.service.chat;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.life.entity.traveltrack.VisitedPlace;
import com.life.entity.traveltrack.WishlistPlace;
import com.life.repository.traveltrack.VisitedPlaceRepository;
import com.life.repository.traveltrack.WishlistRepository;

@Service
public class AITravelAdvisorService {

    @Autowired
    private VisitedPlaceRepository visitedRepo;

    @Autowired
    private WishlistRepository wishlistRepo;

    public String buildTravelContext(Long userId) {

        List<VisitedPlace> visited =
                visitedRepo.findByUserId(userId);

        List<WishlistPlace> wishlist =
                wishlistRepo.findByUserId(userId);

        StringBuilder context =
                new StringBuilder();

        context.append("Visited Places:\n");

        for(VisitedPlace place : visited) {

            context.append(
                    "- "
                    + place.getPlaceName()
                    + " ("
                    + place.getCity()
                    + ")\n"
            );
        }

        context.append("\nWishlist Places:\n");

        for(WishlistPlace place : wishlist) {

            context.append(
                    "- "
                    + place.getPlaceName()
                    + " ("
                    + place.getCity()
                    + ")\n"
            );
        }

        return context.toString();
    }
}

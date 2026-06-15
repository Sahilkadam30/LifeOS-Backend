package com.life.repository.traveltrack;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.life.entity.traveltrack.VisitedPlace;

@Repository
public interface VisitedPlaceRepository extends JpaRepository<VisitedPlace, Long> {
    List<VisitedPlace> findByUserId(Long userId);
}

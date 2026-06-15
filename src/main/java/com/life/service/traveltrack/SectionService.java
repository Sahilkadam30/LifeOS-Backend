package com.life.service.traveltrack;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.life.entity.traveltrack.Section;
import com.life.repository.traveltrack.SectionRepository;

@Service
public class SectionService {

	@Autowired
    private SectionRepository repository;

    public Section save(Section section) {
        section.getPlaces().forEach(p -> p.setSection(section));
        return repository.save(section);
    }

    public List<Section> getAll() {
        return repository.findAll();
    }

    public Section update(Long id, Section section) {

        Section existing = repository.findById(id).orElseThrow();

        existing.setTitle(section.getTitle());
        existing.setDescription(section.getDescription());

        existing.getPlaces().clear();

        section.getPlaces().forEach(place -> {
            place.setSection(existing);
            existing.getPlaces().add(place);
        });

        return repository.save(existing);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

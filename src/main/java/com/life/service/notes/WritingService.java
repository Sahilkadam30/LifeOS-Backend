package com.life.service.notes;

import java.util.List;

import com.life.dto.WritingRequest;
import com.life.entity.notes.Writing;
import com.life.entity.notes.WritingType;

public interface WritingService {

	Writing create(WritingRequest request, Long userId);

    List<Writing> getAll(Long userId);

    List<Writing> getByType(Long userId, WritingType type);

    Writing update(Long id, WritingRequest request, Long userId);

    void delete(Long id, Long userId);
    
    Writing toggleFavorite(Long id, Long userId);
}

package com.life.service.notes;

import java.util.List;

import org.springframework.stereotype.Service;

import com.life.dto.WritingRequest;
import com.life.entity.notes.Writing;
import com.life.entity.notes.WritingType;
import com.life.repository.UserRepository;
import com.life.repository.notes.WritingRepository;

@Service
public class WritingServiceImpl implements WritingService{

	private final WritingRepository writingRepository;
    private final UserRepository userRepository;

    public WritingServiceImpl(WritingRepository writingRepository, UserRepository userRepository) {
        this.writingRepository = writingRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Writing create(WritingRequest request, Long userId) {

        Writing writing = new Writing();

        writing.setTitle(request.getTitle());
        writing.setContent(request.getContent());
        writing.setType(request.getType() != null ? request.getType() : WritingType.NOTE);
        writing.setCardColor(request.getCardColor());
        writing.setFavorite(request.isFavorite());
        writing.setUserId(userId);

        if (userId != null) {
            userRepository.findById(userId).ifPresent(user -> {
                writing.setUsername(user.getUsername());
            });
        }

        return writingRepository.save(writing);
    }

    @Override
    public List<Writing> getAll(Long userId) {
        return writingRepository.findByUserId(userId);
    }

    @Override
    public List<Writing> getByType(Long userId, WritingType type) {
        return writingRepository.findByUserIdAndType(userId, type);
    }

    @Override
    public Writing update(Long id, WritingRequest request, Long userId) {

        Writing writing = writingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Writing not found"));

        if (!writing.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        writing.setTitle(request.getTitle());
        writing.setContent(request.getContent());
        writing.setType(request.getType());
        writing.setCardColor(request.getCardColor());
        writing.setFavorite(request.isFavorite());

        return writingRepository.save(writing);
    }

    @Override
    public void delete(Long id, Long userId) {

        Writing writing = writingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Writing not found"));

        if (!writing.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        writingRepository.delete(writing);
    }
    
    @Override
    public Writing toggleFavorite(Long id, Long userId) {

        Writing writing = writingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Writing not found"));

        if (!writing.getUserId().equals(userId)) {
            throw new RuntimeException("Unauthorized");
        }

        writing.setFavorite(!writing.isFavorite());

        return writingRepository.save(writing);
    }
}

package com.life.service.finance;

import java.time.LocalDate;
import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.life.entity.User;
import com.life.entity.finance.Saving;
import com.life.repository.UserRepository;
import com.life.repository.finance.SavingRepository;

@Service
public class SavingService {

    private final SavingRepository savingRepository;
    private final UserRepository userRepository;

    public SavingService(
            SavingRepository savingRepository,
            UserRepository userRepository) {

        this.savingRepository = savingRepository;
        this.userRepository = userRepository;
    }

    public Saving create(Saving saving) {

        User user = getCurrentUser();

        saving.setUserId(user.getId());

        if (saving.getSavingDate() == null) {
            saving.setSavingDate(LocalDate.now());
        }

        return savingRepository.save(saving);
    }

    public List<Saving> getAll() {

        User user = getCurrentUser();

        return savingRepository.findByUserId(
                user.getId()
        );
    }

    public Saving update(
            Long id,
            Saving request) {

        User user = getCurrentUser();

        Saving saving =
                savingRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Saving not found"));

        if (!saving.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        saving.setAmount(request.getAmount());
        saving.setSavingType(request.getSavingType());
        saving.setSavingDate(request.getSavingDate());
        saving.setNotes(request.getNotes());

        return savingRepository.save(saving);
    }

    public void delete(Long id) {

        User user = getCurrentUser();

        Saving saving =
                savingRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Saving not found"));

        if (!saving.getUserId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        savingRepository.delete(saving);
    }

    public Double getTotalSavings() {

        return getAll()
                .stream()
                .mapToDouble(Saving::getAmount)
                .sum();
    }

    private User getCurrentUser() {

        String username =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository
                .findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));
    }
}

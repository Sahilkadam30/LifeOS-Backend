package com.life.service.finance;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.life.entity.User;
import com.life.entity.finance.Expense;
import com.life.repository.UserRepository;
import com.life.repository.finance.ExpenseRepository;

@Service
public class ExpenseService {

    private final ExpenseRepository repository;
    private final UserRepository userRepository;

    public ExpenseService(
            ExpenseRepository repository,
            UserRepository userRepository) {

        this.repository = repository;
        this.userRepository = userRepository;
    }

    public Expense create(Expense expense) {

        User user = getCurrentUser();

        expense.setUserId(user.getId());

        return repository.save(expense);
    }

    public List<Expense> getAll() {

        User user = getCurrentUser();

        return repository.findByUserId(user.getId());
    }

    public Expense update(
            Long id,
            Expense request) {

        Expense expense =
                repository.findById(id)
                        .orElseThrow();

        expense.setTitle(request.getTitle());
        expense.setCategory(request.getCategory());
        expense.setAmount(request.getAmount());
        expense.setExpenseDate(
                request.getExpenseDate());
        expense.setNotes(request.getNotes());

        return repository.save(expense);
    }

    public void delete(Long id) {

        repository.deleteById(id);
    }

    private User getCurrentUser() {

        String username =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getName();

        return userRepository
                .findByUsername(username)
                .orElseThrow();
    }
}

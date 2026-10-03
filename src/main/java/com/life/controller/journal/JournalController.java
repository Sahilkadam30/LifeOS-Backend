package com.life.controller.journal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.life.dto.journal.*;
import com.life.entity.journal.*;
import com.life.service.journal.JournalService;

@RestController
@RequestMapping("/api/journal")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JournalController {

    @Autowired
    private JournalService journalService;

    // ================= STATS =================
    @GetMapping("/stats")
    public ResponseEntity<JournalStatsDTO> getStats() {
        return ResponseEntity.ok(journalService.getStats());
    }

    // ================= MOVIES =================
    @GetMapping("/movies")
    public ResponseEntity<List<JournalMovie>> getMovies(@RequestParam(required = false) MovieStatus status) {
        return ResponseEntity.ok(journalService.getMovies(status));
    }

    @PostMapping("/movies")
    public ResponseEntity<JournalMovie> createMovie(@RequestBody MovieRequest req) {
        return new ResponseEntity<>(journalService.createMovie(req), HttpStatus.CREATED);
    }

    @PutMapping("/movies/{id}")
    public ResponseEntity<JournalMovie> updateMovie(@PathVariable Long id, @RequestBody MovieRequest req) {
        return ResponseEntity.ok(journalService.updateMovie(id, req));
    }

    @DeleteMapping("/movies/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        journalService.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }

    // ================= BOOKS =================
    @GetMapping("/books")
    public ResponseEntity<List<JournalBook>> getBooks(@RequestParam(required = false) BookStatus status) {
        return ResponseEntity.ok(journalService.getBooks(status));
    }

    @PostMapping("/books")
    public ResponseEntity<JournalBook> createBook(@RequestBody BookRequest req) {
        return new ResponseEntity<>(journalService.createBook(req), HttpStatus.CREATED);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<JournalBook> updateBook(@PathVariable Long id, @RequestBody BookRequest req) {
        return ResponseEntity.ok(journalService.updateBook(id, req));
    }

    @DeleteMapping("/books/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        journalService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FOOD =================
    @GetMapping("/food")
    public ResponseEntity<List<JournalFood>> getFood(@RequestParam(required = false) FoodStatus status) {
        return ResponseEntity.ok(journalService.getFood(status));
    }

    @PostMapping("/food")
    public ResponseEntity<JournalFood> createFood(@RequestBody FoodRequest req) {
        return new ResponseEntity<>(journalService.createFood(req), HttpStatus.CREATED);
    }

    @PutMapping("/food/{id}")
    public ResponseEntity<JournalFood> updateFood(@PathVariable Long id, @RequestBody FoodRequest req) {
        return ResponseEntity.ok(journalService.updateFood(id, req));
    }

    @DeleteMapping("/food/{id}")
    public ResponseEntity<Void> deleteFood(@PathVariable Long id) {
        journalService.deleteFood(id);
        return ResponseEntity.noContent().build();
    }

    // ================= CATEGORIES =================
    @GetMapping("/categories")
    public ResponseEntity<List<JournalFavouriteCategory>> getCategories() {
        return ResponseEntity.ok(journalService.getCategories());
    }

    @PostMapping("/categories")
    public ResponseEntity<JournalFavouriteCategory> createCategory(@RequestBody CategoryRequest req) {
        return new ResponseEntity<>(journalService.createCategory(req), HttpStatus.CREATED);
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        journalService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    // ================= FAVOURITES =================
    @GetMapping("/favourites")
    public ResponseEntity<List<JournalFavourite>> getFavourites(@RequestParam(required = false) Long categoryId) {
        return ResponseEntity.ok(journalService.getFavourites(categoryId));
    }

    @PostMapping("/favourites")
    public ResponseEntity<JournalFavourite> createFavourite(@RequestBody FavouriteRequest req) {
        return new ResponseEntity<>(journalService.createFavourite(req), HttpStatus.CREATED);
    }

    @DeleteMapping("/favourites/{id}")
    public ResponseEntity<Void> deleteFavourite(@PathVariable Long id) {
        journalService.deleteFavourite(id);
        return ResponseEntity.noContent().build();
    }
}

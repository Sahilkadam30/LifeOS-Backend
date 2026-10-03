package com.life.service.journal;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.life.dto.journal.*;
import com.life.entity.User;
import com.life.entity.journal.*;
import com.life.repository.UserRepository;
import com.life.repository.journal.*;

@Service
@Transactional
public class JournalService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JournalMovieRepository movieRepository;

    @Autowired
    private JournalBookRepository bookRepository;

    @Autowired
    private JournalFoodRepository foodRepository;

    @Autowired
    private JournalFavouriteCategoryRepository categoryRepository;

    @Autowired
    private JournalFavouriteRepository favouriteRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    // ================= MOVIES =================
    public List<JournalMovie> getMovies(MovieStatus status) {
        User user = getCurrentUser();
        if (status != null) {
            return movieRepository.findByUserIdAndStatusOrderByIdDesc(user.getId(), status);
        }
        return movieRepository.findByUserIdOrderByIdDesc(user.getId());
    }

    public JournalMovie createMovie(MovieRequest req) {
        User user = getCurrentUser();
        JournalMovie movie = new JournalMovie();
        movie.setUser(user);
        movie.setTitle(req.getTitle());
        movie.setType(req.getType() != null ? req.getType() : MediaType.MOVIE);
        movie.setStatus(req.getStatus() != null ? req.getStatus() : MovieStatus.WANT_TO_WATCH);
        movie.setDescription(req.getDescription());
        return movieRepository.save(movie);
    }

    public JournalMovie updateMovie(Long id, MovieRequest req) {
        User user = getCurrentUser();
        JournalMovie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        if (!movie.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        if (req.getTitle() != null) movie.setTitle(req.getTitle());
        if (req.getType() != null) movie.setType(req.getType());
        if (req.getStatus() != null) movie.setStatus(req.getStatus());
        if (req.getDescription() != null) movie.setDescription(req.getDescription());
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id) {
        User user = getCurrentUser();
        JournalMovie movie = movieRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Movie not found"));
        if (!movie.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        movieRepository.delete(movie);
    }

    // ================= BOOKS =================
    public List<JournalBook> getBooks(BookStatus status) {
        User user = getCurrentUser();
        if (status != null) {
            return bookRepository.findByUserIdAndStatusOrderByIdDesc(user.getId(), status);
        }
        return bookRepository.findByUserIdOrderByIdDesc(user.getId());
    }

    public JournalBook createBook(BookRequest req) {
        User user = getCurrentUser();
        JournalBook book = new JournalBook();
        book.setUser(user);
        book.setTitle(req.getTitle());
        book.setAuthor(req.getAuthor());
        book.setStatus(req.getStatus() != null ? req.getStatus() : BookStatus.WANT_TO_READ);
        book.setDescription(req.getDescription());
        return bookRepository.save(book);
    }

    public JournalBook updateBook(Long id, BookRequest req) {
        User user = getCurrentUser();
        JournalBook book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        if (!book.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        if (req.getTitle() != null) book.setTitle(req.getTitle());
        if (req.getAuthor() != null) book.setAuthor(req.getAuthor());
        if (req.getStatus() != null) book.setStatus(req.getStatus());
        if (req.getDescription() != null) book.setDescription(req.getDescription());
        return bookRepository.save(book);
    }

    public void deleteBook(Long id) {
        User user = getCurrentUser();
        JournalBook book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        if (!book.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        bookRepository.delete(book);
    }

    // ================= FOOD =================
    public List<JournalFood> getFood(FoodStatus status) {
        User user = getCurrentUser();
        if (status != null) {
            return foodRepository.findByUserIdAndStatusOrderByIdDesc(user.getId(), status);
        }
        return foodRepository.findByUserIdOrderByIdDesc(user.getId());
    }

    public JournalFood createFood(FoodRequest req) {
        User user = getCurrentUser();
        JournalFood food = new JournalFood();
        food.setUser(user);
        food.setName(req.getName());
        food.setType(req.getType() != null ? req.getType() : FoodType.DISH);
        food.setStatus(req.getStatus() != null ? req.getStatus() : FoodStatus.WANT_TO_TRY);
        food.setDescription(req.getDescription());
        return foodRepository.save(food);
    }

    public JournalFood updateFood(Long id, FoodRequest req) {
        User user = getCurrentUser();
        JournalFood food = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food item not found"));
        if (!food.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        if (req.getName() != null) food.setName(req.getName());
        if (req.getType() != null) food.setType(req.getType());
        if (req.getStatus() != null) food.setStatus(req.getStatus());
        if (req.getDescription() != null) food.setDescription(req.getDescription());
        return foodRepository.save(food);
    }

    public void deleteFood(Long id) {
        User user = getCurrentUser();
        JournalFood food = foodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Food item not found"));
        if (!food.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        foodRepository.delete(food);
    }

    // ================= CATEGORIES =================
    private static class DefaultCatDef {
        final String name;
        final String icon;
        final String color;
        DefaultCatDef(String name, String icon, String color) {
            this.name = name;
            this.icon = icon;
            this.color = color;
        }
    }

    private static final DefaultCatDef[] DEFAULT_CATEGORY_DEFS = {
        // ── Music & Entertainment ─────────────────────────────────────────
        new DefaultCatDef("Favourite Song",                  "🎵", "#8B5CF6"),
        new DefaultCatDef("Favourite Singer/Artist",         "🎤", "#C026D3"),
        new DefaultCatDef("Favourite Movie",                 "🎬", "#991B1B"),
        new DefaultCatDef("Favourite Series",                "📺", "#0E7490"),
        new DefaultCatDef("Favourite Book",                  "📚", "#1E3A5F"),
        // ── Food & Drinks ─────────────────────────────────────────────────
        new DefaultCatDef("Favourite Food",                  "🍕", "#DC2626"),
        new DefaultCatDef("Favourite Dessert",               "🍰", "#DB2777"),
        new DefaultCatDef("Favourite Drink",                 "☕", "#B45309"),
        new DefaultCatDef("Favourite Recipe",                "👨‍🍳", "#15803D"),
        // ── Sports & Games ────────────────────────────────────────────────
        new DefaultCatDef("Favourite Game",                  "🎮", "#7C3AED"),
        new DefaultCatDef("Favourite Sport",                 "🏏", "#15803D"),
        new DefaultCatDef("Favourite Team",                  "⚽", "#B91C1C"),
        // ── Art & Culture ─────────────────────────────────────────────────
        new DefaultCatDef("Favourite Artwork",               "🎨", "#9D174D"),
        new DefaultCatDef("Favourite Artist",                "🧑‍🎨", "#7E22CE"),
        new DefaultCatDef("Favourite Quote",                 "💬", "#0F766E"),
        new DefaultCatDef("Favourite Story/Poem",            "📖", "#1D4ED8"),
        new DefaultCatDef("Favourite Technology/Tool",       "🧑‍💻", "#374151"),
        // ── Places & Experiences ──────────────────────────────────────────
        new DefaultCatDef("Favourite Bookstore",             "📚", "#2563EB"),
        new DefaultCatDef("Favourite Restaurant",            "🍽️", "#EF4444"),
        new DefaultCatDef("Favourite Café",                  "☕", "#92400E"),
        new DefaultCatDef("Favourite Garden/Park",           "🌳", "#16A34A"),
        new DefaultCatDef("Favourite Historical Place",      "🏛️", "#78716C"),
        new DefaultCatDef("Favourite Temple/Spiritual Place","🛕", "#D97706"),
        new DefaultCatDef("Favourite Beach",                 "🏖️", "#0284C7"),
        new DefaultCatDef("Favourite Hill/Mountain",         "🏔️", "#64748B"),
        new DefaultCatDef("Favourite City",                  "🏙️", "#4F46E5"),
        new DefaultCatDef("Favourite Travel Destination",    "🗺️", "#06B6D4"),
        new DefaultCatDef("Favourite Cinema/Theatre",        "🎬", "#7C3AED"),
        new DefaultCatDef("Favourite Hotel/Stay",            "🏨", "#1D4ED8"),
        new DefaultCatDef("Favourite Shopping Place",        "🛍️", "#EC4899"),
        new DefaultCatDef("Favourite Walking Spot",          "🚶", "#059669"),
        new DefaultCatDef("Favourite Sunset Spot",           "🌅", "#F59E0B"),
        new DefaultCatDef("Favourite Stargazing Spot",       "🌌", "#312E81"),
        new DefaultCatDef("Favourite Photography Spot",      "📸", "#1E293B"),
        new DefaultCatDef("Favourite Art Gallery/Museum",    "🎨", "#7C2D12")
    };

    public List<JournalFavouriteCategory> getCategories() {
        User user = getCurrentUser();
        List<JournalFavouriteCategory> categories = categoryRepository.findByUserIdOrderByNameAsc(user.getId());
        if (!Boolean.TRUE.equals(user.getJournalDefaultsSeeded())) {
            boolean createdAny = ensureDefaultCategories(user, categories);
            user.setJournalDefaultsSeeded(true);
            userRepository.save(user);
            if (createdAny) {
                categories = categoryRepository.findByUserIdOrderByNameAsc(user.getId());
            }
        }
        return categories;
    }

    private boolean ensureDefaultCategories(User user, List<JournalFavouriteCategory> existing) {
        java.util.Set<String> existingNames = existing.stream()
                .map(c -> c.getName() != null ? c.getName().trim().toLowerCase() : "")
                .collect(java.util.stream.Collectors.toSet());

        java.util.List<JournalFavouriteCategory> toCreate = new java.util.ArrayList<>();
        for (DefaultCatDef def : DEFAULT_CATEGORY_DEFS) {
            if (!existingNames.contains(def.name.trim().toLowerCase())) {
                toCreate.add(new JournalFavouriteCategory(user, def.name, def.icon, def.color));
            }
        }

        if (!toCreate.isEmpty()) {
            categoryRepository.saveAll(toCreate);
            return true;
        }
        return false;
    }

    public JournalFavouriteCategory createCategory(CategoryRequest req) {
        User user = getCurrentUser();
        JournalFavouriteCategory cat = new JournalFavouriteCategory(
                user,
                req.getName(),
                req.getIcon() != null && !req.getIcon().isEmpty() ? req.getIcon() : "⭐",
                req.getColor() != null && !req.getColor().isEmpty() ? req.getColor() : "#2563EB"
        );
        return categoryRepository.save(cat);
    }

    public void deleteCategory(Long id) {
        User user = getCurrentUser();
        JournalFavouriteCategory cat = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        if (!cat.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        List<JournalFavourite> items = favouriteRepository.findByUserIdAndCategoryIdOrderByIdDesc(user.getId(), id);
        if (!items.isEmpty()) {
            favouriteRepository.deleteAll(items);
        }
        categoryRepository.delete(cat);
    }

    // ================= FAVOURITES =================
    public List<JournalFavourite> getFavourites(Long categoryId) {
        User user = getCurrentUser();
        if (categoryId != null) {
            return favouriteRepository.findByUserIdAndCategoryIdOrderByIdDesc(user.getId(), categoryId);
        }
        return favouriteRepository.findByUserIdOrderByIdDesc(user.getId());
    }

    public JournalFavourite createFavourite(FavouriteRequest req) {
        User user = getCurrentUser();
        JournalFavourite fav = new JournalFavourite();
        fav.setUser(user);
        fav.setTitle(req.getTitle());

        JournalFavouriteCategory category = null;
        if (req.getCategoryId() != null) {
            category = categoryRepository.findById(req.getCategoryId())
                    .orElse(null);
        }
        if (category == null && req.getCategoryName() != null && !req.getCategoryName().trim().isEmpty()) {
            category = categoryRepository.findByUserIdAndNameIgnoreCase(user.getId(), req.getCategoryName().trim())
                    .orElseGet(() -> categoryRepository.save(
                            new JournalFavouriteCategory(user, req.getCategoryName().trim(), "⭐", "#2563EB")));
        }
        if (category == null) {
            List<JournalFavouriteCategory> all = getCategories();
            category = all.get(0);
        }

        fav.setCategory(category);
        return favouriteRepository.save(fav);
    }

    public void deleteFavourite(Long id) {
        User user = getCurrentUser();
        JournalFavourite fav = favouriteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Favourite item not found"));
        if (!fav.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }
        favouriteRepository.delete(fav);
    }

    // ================= STATS =================
    public JournalStatsDTO getStats() {
        User user = getCurrentUser();
        Long uid = user.getId();

        JournalStatsDTO stats = new JournalStatsDTO();
        stats.setTotalMovies(movieRepository.countByUserId(uid));
        stats.setWatchedMovies(movieRepository.findByUserIdAndStatusOrderByIdDesc(uid, MovieStatus.WATCHED).size());
        stats.setWantToWatchMovies(movieRepository.findByUserIdAndStatusOrderByIdDesc(uid, MovieStatus.WANT_TO_WATCH).size());

        stats.setTotalBooks(bookRepository.countByUserId(uid));
        stats.setReadBooks(bookRepository.findByUserIdAndStatusOrderByIdDesc(uid, BookStatus.READ).size());
        stats.setWantToReadBooks(bookRepository.findByUserIdAndStatusOrderByIdDesc(uid, BookStatus.WANT_TO_READ).size());

        stats.setTotalFood(foodRepository.countByUserId(uid));
        stats.setTriedFood(foodRepository.findByUserIdAndStatusOrderByIdDesc(uid, FoodStatus.TRIED).size());
        stats.setFavoriteFood(foodRepository.findByUserIdAndStatusOrderByIdDesc(uid, FoodStatus.FAVORITE).size());

        stats.setTotalFavourites(favouriteRepository.countByUserId(uid));
        stats.setTotalCategories(categoryRepository.findByUserIdOrderByNameAsc(uid).size());

        return stats;
    }
}

package com.life.dto.journal;

public class JournalStatsDTO {
    private long totalMovies;
    private long watchedMovies;
    private long wantToWatchMovies;

    private long totalBooks;
    private long readBooks;
    private long wantToReadBooks;

    private long totalFood;
    private long triedFood;
    private long favoriteFood;

    private long totalFavourites;
    private long totalCategories;

    public JournalStatsDTO() {}

    public long getTotalMovies() {
        return totalMovies;
    }

    public void setTotalMovies(long totalMovies) {
        this.totalMovies = totalMovies;
    }

    public long getWatchedMovies() {
        return watchedMovies;
    }

    public void setWatchedMovies(long watchedMovies) {
        this.watchedMovies = watchedMovies;
    }

    public long getWantToWatchMovies() {
        return wantToWatchMovies;
    }

    public void setWantToWatchMovies(long wantToWatchMovies) {
        this.wantToWatchMovies = wantToWatchMovies;
    }

    public long getTotalBooks() {
        return totalBooks;
    }

    public void setTotalBooks(long totalBooks) {
        this.totalBooks = totalBooks;
    }

    public long getReadBooks() {
        return readBooks;
    }

    public void setReadBooks(long readBooks) {
        this.readBooks = readBooks;
    }

    public long getWantToReadBooks() {
        return wantToReadBooks;
    }

    public void setWantToReadBooks(long wantToReadBooks) {
        this.wantToReadBooks = wantToReadBooks;
    }

    public long getTotalFood() {
        return totalFood;
    }

    public void setTotalFood(long totalFood) {
        this.totalFood = totalFood;
    }

    public long getTriedFood() {
        return triedFood;
    }

    public void setTriedFood(long triedFood) {
        this.triedFood = triedFood;
    }

    public long getFavoriteFood() {
        return favoriteFood;
    }

    public void setFavoriteFood(long favoriteFood) {
        this.favoriteFood = favoriteFood;
    }

    public long getTotalFavourites() {
        return totalFavourites;
    }

    public void setTotalFavourites(long totalFavourites) {
        this.totalFavourites = totalFavourites;
    }

    public long getTotalCategories() {
        return totalCategories;
    }

    public void setTotalCategories(long totalCategories) {
        this.totalCategories = totalCategories;
    }
}

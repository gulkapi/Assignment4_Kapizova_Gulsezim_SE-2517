package com.musiclibrary.service;

import com.musiclibrary.interfaces.Playable;
import com.musiclibrary.interfaces.Rateable;
import com.musiclibrary.interfaces.Searchable;
import com.musiclibrary.interfaces.Validatable;
import com.musiclibrary.model.Media;
import com.musiclibrary.repository.interfaces.SearchableRepository;
import com.musiclibrary.utils.SortingUtils;
import com.musiclibrary.exception.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MediaService implements Playable, Rateable, Searchable<Media> {

    private final SearchableRepository<Media, Integer> repository;

    private Media currentMedia;
    private boolean isPlaying = false;
    private List<Integer> ratings = new ArrayList<>();

    public MediaService(SearchableRepository<Media, Integer> repository) {
        this.repository = repository;
        System.out.println("MediaService initialized with repository (DIP)");
    }

    public MediaService() {
        this.repository = null;
        System.out.println("Warning: Using default constructor without repository");
    }

    @Override
    public void play() {
        if (currentMedia != null) {
            isPlaying = true;
            System.out.println("Playing: " + currentMedia.getTitle());
        }
    }

    @Override
    public void pause() {
        isPlaying = false;
        System.out.println("Paused: " + currentMedia.getTitle());
    }

    @Override
    public void stop() {
        isPlaying = false;
        System.out.println("Stopped: " + currentMedia.getTitle());
    }

    public String getCurrentStatus() {
        return isPlaying ? "Playing" : "Stopped";
    }

    @Override
    public void rate(int rating) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }
        ratings.add(rating);
        System.out.println("Rated " + currentMedia.getTitle() + ": " + rating + "/5");
    }

    @Override
    public double getAverageRating() {
        if (ratings.isEmpty()) return 0.0;
        double average = ratings.stream()
                .mapToInt(Integer::intValue)
                .average()
                .orElse(0.0);
        return Math.round(average * 10.0) / 10.0;
    }

    @Override
    public int getRatingCount() {
        return ratings.size();
    }

    public void setCurrentMedia(Media media) {
        this.currentMedia = media;
        System.out.println("Selected: " + media.getTitle());
    }

    public Media getCurrentMedia() {
        return currentMedia;
    }

    public void clearRatings() {
        ratings.clear();
        System.out.println("All ratings cleared");
    }

    @Override
    public List<Media> search(String keyword) {
        if (repository == null) {
            System.out.println("Warning: Repository not initialized. Using fallback.");
            return getFallbackSearchResults(keyword);
        }

        if (keyword == null || keyword.trim().isEmpty()) {
            throw new InvalidInputException("Search keyword cannot be empty");
        }

        return repository.search(keyword);
    }

    public List<Media> searchByTitle(String title) {
        return search("title:" + title);
    }

    public List<Media> getAllMediaSortedByTitle() {
        if (repository == null) {
            System.out.println("Repository not initialized. Returning empty list.");
            return new ArrayList<>();
        }

        List<Media> mediaList = repository.findAll();
        SortingUtils.sortByTitle(mediaList);
        return mediaList;
    }

    public List<Media> getAllMediaSortedByDuration() {
        if (repository == null) {
            System.out.println("Repository not initialized. Returning empty list.");
            return new ArrayList<>();
        }

        List<Media> mediaList = repository.findAll();
        SortingUtils.sortByDuration(mediaList);
        return mediaList;
    }

    public List<Media> getAllMediaSortedByArtistThenTitle() {
        if (repository == null) {
            System.out.println("Repository not initialized. Returning empty list.");
            return new ArrayList<>();
        }

        List<Media> mediaList = repository.findAll();
        SortingUtils.sortByArtistThenTitle(mediaList);
        return mediaList;
    }

    public List<Media> getLongMedia(int minDurationSeconds) {
        if (repository == null) {
            System.out.println("Repository not initialized. Returning empty list.");
            return new ArrayList<>();
        }

        List<Media> allMedia = repository.findAll();
        return allMedia.stream()
                .filter(media -> media.getDuration() >= minDurationSeconds)
                .toList();
    }

    public List<Media> getMediaByType(String type) {
        if (repository == null) {
            System.out.println("Repository not initialized. Returning empty list.");
            return new ArrayList<>();
        }

        List<Media> allMedia = repository.findAll();
        return allMedia.stream()
                .filter(media -> media.getMediaType().equalsIgnoreCase(type))
                .toList();
    }

    public List<String> getAllArtists() {
        if (repository == null) {
            System.out.println("Repository not initialized. Returning empty list.");
            return new ArrayList<>();
        }

        List<Media> allMedia = repository.findAll();
        return allMedia.stream()
                .map(Media::getArtist)
                .distinct()
                .sorted((a1, a2) -> a1.compareToIgnoreCase(a2))
                .toList();
    }

    public Media createMedia(Media media) {
        if (repository == null) {
            throw new DatabaseOperationException("Repository not initialized");
        }

        if (!media.validate()) {
            throw new InvalidInputException("Invalid media data");
        }

        List<Media> existing = repository.findByTitle(media.getTitle());
        if (!existing.isEmpty()) {
            throw new DuplicateResourceException(
                    "Media with title '" + media.getTitle() + "' already exists"
            );
        }

        return repository.save(media);
    }

    public Media getMediaById(int id) {
        if (repository == null) {
            throw new DatabaseOperationException("Repository not initialized");
        }

        Optional<Media> media = repository.findById(id);
        return media.orElseThrow(() ->
                new ResourceNotFoundException("Media with ID " + id + " not found")
        );
    }

    public List<Media> getAllMedia() {
        if (repository == null) {
            System.out.println("Repository not initialized. Returning empty list.");
            return new ArrayList<>();
        }

        return repository.findAll();
    }

    public Media updateMedia(int id, Media mediaUpdates) {
        if (repository == null) {
            throw new DatabaseOperationException("Repository not initialized");
        }

        Media existing = getMediaById(id);

        if (mediaUpdates.getTitle() != null) {
            existing.setTitle(mediaUpdates.getTitle());
        }
        if (mediaUpdates.getArtist() != null) {
            existing.setArtist(mediaUpdates.getArtist());
        }
        if (mediaUpdates.getDuration() > 0) {
            existing.setDuration(mediaUpdates.getDuration());
        }
        if (mediaUpdates.getReleaseYear() > 0) {
            existing.setReleaseYear(mediaUpdates.getReleaseYear());
        }

        if (!existing.validate()) {
            throw new InvalidInputException("Invalid data after update");
        }

        return repository.update(existing);
    }

    public boolean deleteMedia(int id) {
        if (repository == null) {
            throw new DatabaseOperationException("Repository not initialized");
        }

        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Media with ID " + id + " not found");
        }

        return repository.delete(id);
    }

    public int getTotalMediaCount() {
        if (repository == null) return 0;
        return (int) repository.count();
    }

    public boolean mediaExists(int id) {
        if (repository == null) return false;
        return repository.existsById(id);
    }

    public boolean validateMediaYear(int year) {
        return com.musiclibrary.interfaces.Validatable.isValidYear(year);
    }

    private List<Media> getFallbackSearchResults(String keyword) {
        List<Media> results = new ArrayList<>();
        return results;
    }

    public void demonstratePolymorphism() {
        System.out.println("\n=== Demonstrating Polymorphism in Service ===");

        if (repository == null) {
            System.out.println("Cannot demonstrate - repository not initialized");
            return;
        }

        List<Media> allMedia = repository.findAll();
        if (allMedia.isEmpty()) {
            System.out.println("No media to demonstrate");
            return;
        }

        for (Media media : allMedia) {
            System.out.println("Title: " + media.getTitle());
            System.out.println("Type: " + media.getMediaType());
            System.out.println("Info: " + media.getAdditionalInfo());
            System.out.println("Metadata: " + media.getMetadata());
            System.out.println("Valid: " + media.validate());
            System.out.println("---");
        }
    }
}
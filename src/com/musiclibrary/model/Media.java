package com.musiclibrary.model;
import com.musiclibrary.interfaces.Validatable;
import java.util.Map;

public abstract class Media implements Validatable {
    private int id;
    private String title;
    private String artist;
    private int duration; // in seconds
    private int releaseYear;

    public Media(int id, String title, String artist, int duration, int releaseYear) {
        this.id = id;
        this.title = title;
        this.artist = artist;
        this.duration = duration;
        this.releaseYear = releaseYear;
    }

    public abstract String getMediaType();
    public abstract String getAdditionalInfo();

    public abstract Map<String, String> getMetadata();

    public String getDurationFormatted() {
        int minutes = duration / 60;
        int seconds = duration % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title cannot be empty");
        }
        this.title = title;
    }

    public String getArtist() { return artist; }
    public void setArtist(String artist) {
        if (artist == null || artist.trim().isEmpty()) {
            throw new IllegalArgumentException("Artist cannot be empty");
        }
        this.artist = artist;
    }

    public int getDuration() { return duration; }
    public void setDuration(int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be greater than 0");
        }
        this.duration = duration;
    }

    public int getReleaseYear() { return releaseYear; }
    public void setReleaseYear(int releaseYear) {
        int currentYear = java.time.Year.now().getValue();
        if (releaseYear < 1900 || releaseYear > currentYear) {
            throw new IllegalArgumentException("Release year must be between 1900 and " + currentYear);
        }
        this.releaseYear = releaseYear;
    }
    @Override
    public boolean validate() {
        return title != null && !title.trim().isEmpty() &&
                artist != null && !artist.trim().isEmpty() &&
                duration > 0 &&
                releaseYear >= 1900 && releaseYear <= java.time.Year.now().getValue();
    }

    public String getBasicInfo() {
        return String.format("%s by %s (%d)", title, artist, releaseYear);
    }
}
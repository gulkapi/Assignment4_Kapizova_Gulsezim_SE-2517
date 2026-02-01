package com.musiclibrary.model;
import java.util.HashMap;
import java.util.Map;

public class Podcast extends Media {
    private String host;
    private String category;
    private int episodeNumber;
    private String description;

    public Podcast(int id, String title, String artist, int duration, int releaseYear,
                   String host, String category, int episodeNumber, String description) {
        super(id, title, artist, duration, releaseYear);
        this.host = host;
        this.category = category;
        this.episodeNumber = episodeNumber;
        this.description = description;
    }

    @Override
    public String getMediaType() {
        return "Podcast";
    }

    @Override
    public String getAdditionalInfo() {
        return String.format("Host: %s | Category: %s | Episode: %d",
                host, category, episodeNumber);
    }
    @Override
    public Map<String, String> getMetadata() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("type", "Podcast");
        metadata.put("host", host);
        metadata.put("category", category);
        metadata.put("episodeNumber", String.valueOf(episodeNumber));
        metadata.put("description", description != null ? description : "");
        metadata.put("duration", getDurationFormatted());
        return metadata;
    }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public int getEpisodeNumber() { return episodeNumber; }
    public void setEpisodeNumber(int episodeNumber) {
        if (episodeNumber <= 0) {
            throw new IllegalArgumentException("Episode number must be positive");
        }
        this.episodeNumber = episodeNumber;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    @Override
    public String toString() {
        return super.toString() + String.format(" | Host: %s | Episode: %d",
                host, episodeNumber);
    }

    @Override
    public boolean validate() {
        return super.validate() &&
                host != null && !host.trim().isEmpty() &&
                category != null && !category.trim().isEmpty() &&
                episodeNumber > 0;
    }
}

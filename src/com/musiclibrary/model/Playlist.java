package com.musiclibrary.model;

import java.util.ArrayList;
import java.util.List;

public class Playlist {
    private int id;
    private String name;
    private String description;
    private List<PlaylistItem> items;

    public Playlist(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.items = new ArrayList<>();
    }

    public void addMedia(Media media) {
        PlaylistItem item = new PlaylistItem(media, items.size() + 1);
        items.add(item);
    }

    public boolean removeMedia(int mediaId) {
        return items.removeIf(item -> item.getMedia().getId() == mediaId);
    }

    public int getTotalDuration() {
        return items.stream()
                .mapToInt(item -> item.getMedia().getDuration())
                .sum();
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Playlist name cannot be empty");
        }
        this.name = name;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<PlaylistItem> getItems() { return new ArrayList<>(items); }

    public static class PlaylistItem {
        private Media media;
        private int position;

        public PlaylistItem(Media media, int position) {
            this.media = media;
            this.position = position;
        }

        public Media getMedia() { return media; }
        public int getPosition() { return position; }
        public void setPosition(int position) { this.position = position; }
    }
}

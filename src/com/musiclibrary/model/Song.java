package com.musiclibrary.model;
import java.util.HashMap;
import java.util.Map;

public class Song extends Media {
    private String album;
    private String genre;
    private int trackNumber;

    public Song(int id, String title, String artist, int duration, int releaseYear,
                String album, String genre, int trackNumber) {
        super(id, title, artist, duration, releaseYear);
        this.album = album;
        this.genre = genre;
        this.trackNumber = trackNumber;
    }

    @Override
    public String getMediaType() {
        return "Song";
    }

    @Override
    public String getAdditionalInfo() {
        return String.format("Album: %s | Genre: %s | Track: %d", album, genre, trackNumber);
    }
    @Override
    public Map<String, String> getMetadata() {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("type", "Song");
        metadata.put("album", album);
        metadata.put("genre", genre);
        metadata.put("trackNumber", String.valueOf(trackNumber));
        metadata.put("duration", getDurationFormatted());
        return metadata;
    }
    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getTrackNumber() { return trackNumber; }
    public void setTrackNumber(int trackNumber) {
        if (trackNumber <= 0) {
            throw new IllegalArgumentException("Track number must be positive");
        }
        this.trackNumber = trackNumber;
    }
    @Override
    public String toString() {
        return super.toString() + String.format(" | Album: %s | Genre: %s", album, genre);
    }
}
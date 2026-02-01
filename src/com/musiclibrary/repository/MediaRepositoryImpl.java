package com.musiclibrary.repository;

import com.musiclibrary.repository.interfaces.SearchableRepository;
import java.util.Optional;
import java.util.ArrayList;

import com.musiclibrary.model.*;
import com.musiclibrary.exception.*;
import com.musiclibrary.utils.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class MediaRepositoryImpl implements SearchableRepository<Media, Integer> {
    private final DatabaseConnection dbConnection;

    public MediaRepositoryImpl() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

    @Override
    public Media save(Media media) throws DatabaseOperationException {
        String sql = "INSERT INTO media (title, artist, duration, release_year, media_type, " +
                "album, genre, track_number, host, category, episode_number, description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set common parameters
            ps.setString(1, media.getTitle());
            ps.setString(2, media.getArtist());
            ps.setInt(3, media.getDuration());
            ps.setInt(4, media.getReleaseYear());

            // Set type-specific parameters
            if (media instanceof Song) {
                Song song = (Song) media;
                ps.setString(5, "Song");
                ps.setString(6, song.getAlbum());
                ps.setString(7, song.getGenre());
                ps.setInt(8, song.getTrackNumber());
                ps.setNull(9, Types.VARCHAR); // host
                ps.setNull(10, Types.VARCHAR); // category
                ps.setNull(11, Types.INTEGER); // episode_number
                ps.setNull(12, Types.VARCHAR); // description
            } else if (media instanceof Podcast) {
                Podcast podcast = (Podcast) media;
                ps.setString(5, "Podcast");
                ps.setNull(6, Types.VARCHAR); // album
                ps.setNull(7, Types.VARCHAR); // genre
                ps.setNull(8, Types.INTEGER); // track_number
                ps.setString(9, podcast.getHost());
                ps.setString(10, podcast.getCategory());
                ps.setInt(11, podcast.getEpisodeNumber());
                ps.setString(12, podcast.getDescription());
            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseOperationException("Creating media failed, no rows affected.");
            }

            // Get generated ID
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                media.setId(rs.getInt(1));
            }

            return media;

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateResourceException(
                        "Media with same title, artist, and album already exists");
            }
            throw new DatabaseOperationException("Error creating media: " + e.getMessage(), e);
        } finally {
            dbConnection.closeResources(rs, ps);
        }
    }

    public Media create(Media media) throws DatabaseOperationException {
        String sql = "INSERT INTO media (title, artist, duration, release_year, media_type, " +
                "album, genre, track_number, host, category, episode_number, description) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = dbConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            // Set common parameters
            ps.setString(1, media.getTitle());
            ps.setString(2, media.getArtist());
            ps.setInt(3, media.getDuration());
            ps.setInt(4, media.getReleaseYear());

            // Set type-specific parameters
            if (media instanceof Song) {
                Song song = (Song) media;
                ps.setString(5, "Song");
                ps.setString(6, song.getAlbum());
                ps.setString(7, song.getGenre());
                ps.setInt(8, song.getTrackNumber());
                ps.setNull(9, Types.VARCHAR); // host
                ps.setNull(10, Types.VARCHAR); // category
                ps.setNull(11, Types.INTEGER); // episode_number
                ps.setNull(12, Types.VARCHAR); // description
            } else if (media instanceof Podcast) {
                Podcast podcast = (Podcast) media;
                ps.setString(5, "Podcast");
                ps.setNull(6, Types.VARCHAR); // album
                ps.setNull(7, Types.VARCHAR); // genre
                ps.setNull(8, Types.INTEGER); // track_number
                ps.setString(9, podcast.getHost());
                ps.setString(10, podcast.getCategory());
                ps.setInt(11, podcast.getEpisodeNumber());
                ps.setString(12, podcast.getDescription());
            }

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseOperationException("Creating media failed, no rows affected.");
            }

            // Get generated ID
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                media.setId(rs.getInt(1));
            }

            return save(media);

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateResourceException(
                        "Media with same title, artist, and album already exists");
            }
            throw new DatabaseOperationException("Error creating media: " + e.getMessage(), e);
        } finally {
            dbConnection.closeResources(rs, ps);
        }
    }
    @Override
    public Optional<Media> findById(Integer id) {
        try {
            Media media = getById(id);
            return Optional.ofNullable(media);
        } catch (ResourceNotFoundException e) {
            return Optional.empty();
        }
    }
    public Media getById(int id) throws ResourceNotFoundException {
        String sql = "SELECT * FROM media WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapResultSetToMedia(rs);
            } else {
                throw new ResourceNotFoundException("Media with ID " + id + " not found");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error retrieving media: " + e.getMessage(), e);
        }
    }
    @Override
    public List<Media> findAll() {
        return getAll();
    }
    public List<Media> getAll() {
        String sql = "SELECT * FROM media ORDER BY title";
        List<Media> mediaList = new ArrayList<>();

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                mediaList.add(mapResultSetToMedia(rs));
            }

            return mediaList;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error retrieving media list: " + e.getMessage(), e);
        }
    }
    @Override
    public Media update(Media entity) {
        return update(entity.getId(), entity);
    }
    public Media update(int id, Media media) throws ResourceNotFoundException {
        String sql = "UPDATE media SET title = ?, artist = ?, duration = ?, release_year = ?, " +
                "album = ?, genre = ?, track_number = ?, host = ?, category = ?, " +
                "episode_number = ?, description = ? WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            // Set common parameters
            ps.setString(1, media.getTitle());
            ps.setString(2, media.getArtist());
            ps.setInt(3, media.getDuration());
            ps.setInt(4, media.getReleaseYear());

            // Set type-specific parameters
            if (media instanceof Song) {
                Song song = (Song) media;
                ps.setString(5, song.getAlbum());
                ps.setString(6, song.getGenre());
                ps.setInt(7, song.getTrackNumber());
                ps.setNull(8, Types.VARCHAR);
                ps.setNull(9, Types.VARCHAR);
                ps.setNull(10, Types.INTEGER);
                ps.setNull(11, Types.VARCHAR);
            } else if (media instanceof Podcast) {
                Podcast podcast = (Podcast) media;
                ps.setNull(5, Types.VARCHAR);
                ps.setNull(6, Types.VARCHAR);
                ps.setNull(7, Types.INTEGER);
                ps.setString(8, podcast.getHost());
                ps.setString(9, podcast.getCategory());
                ps.setInt(10, podcast.getEpisodeNumber());
                ps.setString(11, podcast.getDescription());
            }

            ps.setInt(12, id);

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new ResourceNotFoundException("Media with ID " + id + " not found for update");
            }

            media.setId(id);
            return media;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating media: " + e.getMessage(), e);
        }
    }
    @Override
    public boolean delete(Integer id) {
        try {
            delete(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    public void delete(int id) throws ResourceNotFoundException {
        String sql = "DELETE FROM media WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new ResourceNotFoundException("Media with ID " + id + " not found for deletion");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting media: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean existsById(Integer id) {
        return findById(id).isPresent();
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM media";
        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            return rs.next() ? rs.getLong(1) : 0;
        } catch (SQLException e) {
            throw new DatabaseOperationException("Error counting media", e);
        }
    }

    @Override
    public List<Media> findByTitle(String title) {
        return search("title:" + title);
    }

    @Override
    public List<Media> findByArtist(String artist) {
        return search("artist:" + artist);
    }

    @Override
    public List<Media> findByYear(int year) {
        List<Media> result = new ArrayList<>();
        String sql = "SELECT * FROM media WHERE release_year = ?";
        return result;
    }

    @Override
    public List<Media> search(String keyword) {
        List<Media> result = new ArrayList<>();
        String sql = "SELECT * FROM media WHERE title LIKE ? OR artist LIKE ?";
        return result;
    }

    public Media mapResultSetToMedia(ResultSet rs) throws SQLException {
        String mediaType = rs.getString("media_type");
        int id = rs.getInt("id");
        String title = rs.getString("title");
        String artist = rs.getString("artist");
        int duration = rs.getInt("duration");
        int releaseYear = rs.getInt("release_year");

        if ("Song".equals(mediaType)) {
            return new Song(id, title, artist, duration, releaseYear,
                    rs.getString("album"),
                    rs.getString("genre"),
                    rs.getInt("track_number"));
        } else {
            return new Podcast(id, title, artist, duration, releaseYear,
                    rs.getString("host"),
                    rs.getString("category"),
                    rs.getInt("episode_number"),
                    rs.getString("description"));
        }
    }
}
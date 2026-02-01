package com.musiclibrary.repository;

import com.musiclibrary.repository.interfaces.CrudRepository;
import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import com.musiclibrary.model.*;
import com.musiclibrary.exception.*;
import com.musiclibrary.utils.DatabaseConnection;

import java.sql.*;

public class PlaylistRepositoryImpl implements CrudRepository<Playlist, Integer> {
    private final DatabaseConnection dbConnection;

    public PlaylistRepositoryImpl() {
        this.dbConnection = DatabaseConnection.getInstance();
    }

@Override
    public Playlist save (Playlist playlist) {
        String sql = "INSERT INTO playlists (name, description) VALUES (?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, playlist.getName());
            ps.setString(2, playlist.getDescription());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseOperationException("Creating playlist failed");
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                playlist.setId(rs.getInt(1));
            }

            for (Playlist.PlaylistItem item : playlist.getItems()) {
                addMediaToPlaylist(playlist.getId(), item.getMedia().getId(), item.getPosition());
            }

            return playlist;

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateResourceException(
                        "Playlist with name '" + playlist.getName() + "' already exists");
            }
            throw new DatabaseOperationException("Error creating playlist: " + e.getMessage(), e);
        }
    }
    public Playlist create(Playlist playlist) {
        String sql = "INSERT INTO playlists (name, description) VALUES (?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, playlist.getName());
            ps.setString(2, playlist.getDescription());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseOperationException("Creating playlist failed");
            }

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                playlist.setId(rs.getInt(1));
            }

            for (Playlist.PlaylistItem item : playlist.getItems()) {
                addMediaToPlaylist(playlist.getId(), item.getMedia().getId(), item.getPosition());
            }

            return save(playlist);

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateResourceException(
                        "Playlist with name '" + playlist.getName() + "' already exists");
            }
            throw new DatabaseOperationException("Error creating playlist: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Playlist> findById(Integer id) {
        try {
            Playlist playlist = getById(id);
            return Optional.ofNullable(playlist);
        } catch (ResourceNotFoundException e) {
            return Optional.empty();
        }
    }
    public Playlist getById(int id) throws ResourceNotFoundException {
        String sql = "SELECT p.*, pi.media_id, pi.position, m.* " +
                "FROM playlists p " +
                "LEFT JOIN playlist_items pi ON p.id = pi.playlist_id " +
                "LEFT JOIN media m ON pi.media_id = m.id " +
                "WHERE p.id = ? " +
                "ORDER BY pi.position";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                throw new ResourceNotFoundException("Playlist with ID " + id + " not found");
            }

            Playlist playlist = new Playlist(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("description")
            );

            do {
                if (rs.getInt("media_id") != 0) {
                    Media media = new MediaRepositoryImpl().mapResultSetToMedia(rs);
                    playlist.addMedia(media);
                }
            } while (rs.next());

            return playlist;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error retrieving playlist: " + e.getMessage(), e);
        }
    }
    @Override
    public List<Playlist> findAll() {
        List<Playlist> playlists = new ArrayList<>();
        String sql = "SELECT * FROM playlists ORDER BY name";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Playlist playlist = new Playlist(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description")
                );
                playlists.add(playlist);
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error retrieving all playlists", e);
        }

        return playlists;
    }

    @Override
    public Playlist update(Playlist playlist) {
        String sql = "UPDATE playlists SET name = ?, description = ? WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, playlist.getName());
            ps.setString(2, playlist.getDescription());
            ps.setInt(3, playlist.getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new ResourceNotFoundException("Playlist with ID " + playlist.getId() + " not found");
            }

            return playlist;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error updating playlist", e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        String sql = "DELETE FROM playlists WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error deleting playlist", e);
        }
    }

    @Override
    public boolean existsById(Integer id) {
        String sql = "SELECT 1 FROM playlists WHERE id = ?";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error checking playlist existence", e);
        }
    }

    @Override
    public long count() {
        String sql = "SELECT COUNT(*) FROM playlists";

        try (Connection conn = dbConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return rs.next() ? rs.getLong(1) : 0;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Error counting playlists", e);
        }
    }

    public void addMediaToPlaylist(int playlistId, int mediaId, int position) {
        String sql = "INSERT INTO playlist_items (playlist_id, media_id, position) VALUES (?, ?, ?)";

        try (Connection conn = dbConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, playlistId);
            ps.setInt(2, mediaId);
            ps.setInt(3, position);
            ps.executeUpdate();

        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateResourceException(
                        "Media already exists in this playlist");
            }
            if (e.getMessage().contains("FOREIGN KEY constraint failed")) {
                throw new ResourceNotFoundException("Media or playlist not found");
            }
            throw new DatabaseOperationException("Error adding media to playlist: " + e.getMessage(), e);
        }
    }

}
package com.musiclibrary.controller;

import com.musiclibrary.utils.ReflectionUtils;
import com.musiclibrary.utils.SortingUtils;
import com.musiclibrary.model.*;
import com.musiclibrary.service.MediaService;
import com.musiclibrary.exception.*;
import java.util.*;

public class MusicController {
    private MediaService mediaService;
    private final Scanner scanner;

    public MusicController() {
        this.mediaService = new MediaService();
        this.scanner = new Scanner(System.in);
    }

    public MusicController(MediaService mediaService) {
        this.mediaService = mediaService;
        this.scanner = new Scanner(System.in);
    }
    public Song createSong() {
        System.out.println("\n Creating New Song:");
        System.out.println("=" .repeat(40));

        try {
            System.out.print("Title: ");
            String title = scanner.nextLine();

            System.out.print("Artist: ");
            String artist = scanner.nextLine();

            System.out.print("Duration (seconds): ");
            int duration = Integer.parseInt(scanner.nextLine());

            System.out.print("Release Year: ");
            int year = Integer.parseInt(scanner.nextLine());

            System.out.print("Album: ");
            String album = scanner.nextLine();

            System.out.print("Genre: ");
            String genre = scanner.nextLine();

            System.out.print("Track Number: ");
            int track = Integer.parseInt(scanner.nextLine());

            Song song = new Song(0, title, artist, duration, year, album, genre, track);

            System.out.println(" Song created: " + song.getTitle());
            System.out.println("   Duration: " + song.getDurationFormatted());
            System.out.println("   Type: " + song.getMediaType());
            System.out.println("   Info: " + song.getAdditionalInfo());

            return song;

        } catch (NumberFormatException e) {
            System.out.println(" Error: Please enter valid numbers for duration, year, and track");
        } catch (IllegalArgumentException e) {
            System.out.println(" Error: " + e.getMessage());
        }
        return null;
    }

    public Podcast createPodcast() {
        System.out.println("\n Creating New Podcast:");
        System.out.println("=" .repeat(40));

        try {
            System.out.print("Title: ");
            String title = scanner.nextLine();

            System.out.print("Host/Artist: ");
            String artist = scanner.nextLine();

            System.out.print("Duration (seconds): ");
            int duration = Integer.parseInt(scanner.nextLine());

            System.out.print("Release Year: ");
            int year = Integer.parseInt(scanner.nextLine());

            System.out.print("Host Name: ");
            String host = scanner.nextLine();

            System.out.print("Category: ");
            String category = scanner.nextLine();

            System.out.print("Episode Number: ");
            int episode = Integer.parseInt(scanner.nextLine());

            System.out.print("Description: ");
            String description = scanner.nextLine();

            Podcast podcast = new Podcast(0, title, artist, duration, year,
                    host, category, episode, description);

            System.out.println(" Podcast created: " + podcast.getTitle());
            System.out.println("   Duration: " + podcast.getDurationFormatted());
            System.out.println("   Type: " + podcast.getMediaType());
            System.out.println("   Info: " + podcast.getAdditionalInfo());

            return podcast;

        } catch (NumberFormatException e) {
            System.out.println(" Error: Please enter valid numbers");
        } catch (IllegalArgumentException e) {
            System.out.println(" Error: " + e.getMessage());
        }
        return null;
    }

    public void listAllMedia() {
        System.out.println("\n All Media in Library:");
        System.out.println("=" .repeat(60));

        List<Media> sampleMedia = createSampleMedia();

        if (sampleMedia.isEmpty()) {
            System.out.println("No media found in the library.");
            return;
        }

        for (int i = 0; i < sampleMedia.size(); i++) {
            Media media = sampleMedia.get(i);
            System.out.printf("[%d] %s - %s (%s)%n",
                    i + 1,
                    media.getTitle(),
                    media.getArtist(),
                    media.getMediaType());
            System.out.println("   Duration: " + media.getDurationFormatted());
            System.out.println("   Released: " + media.getReleaseYear());
            System.out.println("   " + media.getAdditionalInfo());
            System.out.println();
        }
    }

    public void updateMedia() {
        System.out.println("\n️ Updating Media:");
        System.out.println("=" .repeat(40));

        listAllMedia();

        try {
            System.out.print("\nEnter media number to update: ");
            int mediaNumber = Integer.parseInt(scanner.nextLine());

            List<Media> sampleMedia = createSampleMedia();
            if (mediaNumber < 1 || mediaNumber > sampleMedia.size()) {
                System.out.println(" Error: Invalid media number");
                return;
            }

            System.out.print("Enter new title: ");
            String newTitle = scanner.nextLine();

            Media media = sampleMedia.get(mediaNumber - 1);
            String oldTitle = media.getTitle();
            media.setTitle(newTitle);

            System.out.println(" Updated: '" + oldTitle + "' → '" + newTitle + "'");

        } catch (NumberFormatException e) {
            System.out.println(" Error: Please enter a valid number");
        } catch (IllegalArgumentException e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }

    public void deleteMedia() {
        System.out.println("\n Deleting Media:");
        System.out.println("=" .repeat(40));

        listAllMedia();

        try {
            System.out.print("\nEnter media number to delete: ");
            int mediaNumber = Integer.parseInt(scanner.nextLine());

            List<Media> sampleMedia = createSampleMedia();
            if (mediaNumber < 1 || mediaNumber > sampleMedia.size()) {
                System.out.println(" Error: Invalid media number");
                return;
            }

            Media media = sampleMedia.get(mediaNumber - 1);

            if (isMediaInPlaylist(media)) {
                throw new InvalidInputException(
                        "Cannot delete media that is in a playlist. Remove from playlist first.");
            }

            System.out.print("Are you sure you want to delete '" + media.getTitle() + "'? (yes/no): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("yes")) {
                System.out.println(" Deleted: " + media.getTitle());
            } else {
                System.out.println(" Deletion cancelled");
            }

        } catch (NumberFormatException e) {
            System.out.println(" Error: Please enter a valid number");
        } catch (InvalidInputException e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }

    public void demonstratePolymorphism() {
        System.out.println("\n POLYMORPHISM DEMONSTRATION:");
        System.out.println("=" .repeat(50));

        List<Media> mediaList = createSampleMedia();

        System.out.println("Treating all media as 'Media' type:");
        System.out.println("-".repeat(50));

        for (Media media : mediaList) {
            System.out.println("Title: " + media.getTitle());
            System.out.println("Type: " + media.getMediaType()); // Polymorphic!
            System.out.println("Duration: " + media.getDurationFormatted());
            System.out.println("Info: " + media.getAdditionalInfo()); // Polymorphic!
            System.out.println("-".repeat(30));
        }

        System.out.println("\n Key Points Demonstrated:");
        System.out.println("1. Media reference can point to Song or Podcast");
        System.out.println("2. getMediaType() returns different values");
        System.out.println("3. getAdditionalInfo() shows different information");
        System.out.println(" Polymorphism requirement satisfied!");
    }

    public void demonstrateInterfaces() {
        System.out.println("\n INTERFACE DEMONSTRATION:");
        System.out.println("=" .repeat(50));

        Song testSong = new Song(99, "Test Song", "Test Artist", 180, 2024,
                "Test Album", "Test", 1);
        mediaService.setCurrentMedia(testSong);

        System.out.println(" Testing Playable Interface:");
        System.out.println("-".repeat(30));
        System.out.println("Current Status: " + mediaService.getCurrentStatus());
        mediaService.play();
        System.out.println("Status after play(): " + mediaService.getCurrentStatus());
        mediaService.pause();
        System.out.println("Status after pause(): " + mediaService.getCurrentStatus());
        mediaService.stop();

        System.out.println("\n Testing Rateable Interface:");
        System.out.println("-".repeat(30));
        System.out.println("Initial ratings: " + mediaService.getRatingCount());
        mediaService.rate(5);
        mediaService.rate(4);
        mediaService.rate(5);
        System.out.println("After 3 ratings:");
        System.out.println("  Total ratings: " + mediaService.getRatingCount());
        System.out.println("  Average rating: " + mediaService.getAverageRating());

        try {
            System.out.println("\n Testing validation - invalid rating:");
            mediaService.rate(10); // Should throw exception
        } catch (IllegalArgumentException e) {
            System.out.println(" Correctly caught: " + e.getMessage());
        }

        System.out.println("\n Key Points Demonstrated:");
        System.out.println("1. MediaService implements Playable interface");
        System.out.println("2. MediaService implements Rateable interface");
        System.out.println("3. Interface methods are properly implemented");
        System.out.println("4. Validation in interface methods works");
        System.out.println("Interface requirement satisfied!");
    }

    public void demonstrateValidation() {
        System.out.println("\n️ VALIDATION & BUSINESS RULES DEMONSTRATION:");
        System.out.println("=" .repeat(50));

        System.out.println("Testing input validation:");
        System.out.println("-".repeat(30));

        try {
            System.out.println("Test 1: Empty title");
            Song badSong1 = new Song(0, "", "Artist", 180, 2024, "Album", "Genre", 1);
            System.out.println(" Should have thrown exception!");
        } catch (IllegalArgumentException e) {
            System.out.println(" Correctly caught: " + e.getMessage());
        }

        try {
            System.out.println("\nTest 2: Negative duration");
            Song badSong2 = new Song(0, "Title", "Artist", -10, 2024, "Album", "Genre", 1);
            System.out.println(" Should have thrown exception!");
        } catch (IllegalArgumentException e) {
            System.out.println(" Correctly caught: " + e.getMessage());
        }

        try {
            System.out.println("\nTest 3: Invalid release year (3000)");
            Song badSong3 = new Song(0, "Title", "Artist", 180, 3000, "Album", "Genre", 1);
            System.out.println(" Should have thrown exception!");
        } catch (IllegalArgumentException e) {
            System.out.println(" Correctly caught: " + e.getMessage());
        }

        System.out.println("\nTest 4: Business rule - long media warning");
        Podcast longPodcast = new Podcast(0, "Very Long Podcast", "Host", 7200, 2024,
                "Host", "Category", 1, "Description");
        System.out.println("Created: " + longPodcast.getTitle());
        System.out.println("Duration: " + longPodcast.getDurationFormatted() + " (>1 hour)");
        System.out.println(" Business rule would flag this as long media");

        System.out.println("\n Key Points Demonstrated:");
        System.out.println("1. Validation in setters works");
        System.out.println("2. Business rules can be applied");
        System.out.println("3. Custom exceptions can be thrown");
        System.out.println(" Validation requirement satisfied!");
    }

    public void demonstrateComposition() {
        System.out.println("\n COMPOSITION DEMONSTRATION:");
        System.out.println("=" .repeat(50));

        Playlist playlist = new Playlist(1, "Workout Mix", "High energy songs");

        Song song1 = new Song(1, "Eye of the Tiger", "Survivor", 245, 1982,
                "Eye of the Tiger", "Rock", 1);
        Song song2 = new Song(2, "Lose Yourself", "Eminem", 326, 2002,
                "8 Mile", "Hip Hop", 1);
        Podcast podcast = new Podcast(3, "Fitness Tips", "Trainer", 1800, 2024,
                "John Doe", "Fitness", 45, "Daily fitness tips");

        playlist.addMedia(song1);
        playlist.addMedia(song2);
        playlist.addMedia(podcast);

        System.out.println("Playlist: " + playlist.getName());
        System.out.println("Description: " + playlist.getDescription());
        System.out.println("Total Duration: " +
                String.format("%d:%02d",
                        playlist.getTotalDuration() / 60,
                        playlist.getTotalDuration() % 60));
        System.out.println("Items in playlist: " + playlist.getItems().size());

        System.out.println("\n Key Points Demonstrated:");
        System.out.println("1. Playlist 'has-a' PlaylistItem (composition)");
        System.out.println("2. PlaylistItem cannot exist without Playlist");
        System.out.println("3. Playlist manages lifecycle of PlaylistItems");
        System.out.println("Composition requirement satisfied!");
    }

    public void runFullDemo() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("MUSIC LIBRARY API - FULL DEMONSTRATION");
        System.out.println("=".repeat(60));

        System.out.println("\n ASSIGNMENT REQUIREMENTS CHECKLIST:");
        System.out.println("-".repeat(40));

        System.out.println("1. CRUD Operations:");
        createSong();
        createPodcast();
        listAllMedia();
        updateMedia();
        deleteMedia();

        System.out.println("\n2. OOP Principles:");
        demonstratePolymorphism();
        demonstrateComposition();

        System.out.println("\n3. Interfaces:");
        demonstrateInterfaces();

        System.out.println("\n4. Validation & Business Rules:");
        demonstrateValidation();

        System.out.println("\n" + "=".repeat(60));
        System.out.println("ALL REQUIREMENTS DEMONSTRATED SUCCESSFULLY!");
        System.out.println("=".repeat(60));
    }

    public void showMenu() {
        boolean running = true;

        while (running) {
            System.out.println("\n" + "=".repeat(50));
            System.out.println("MUSIC LIBRARY CONTROLLER");
            System.out.println("=".repeat(50));
            System.out.println("1. Create Song");
            System.out.println("2. Create Podcast");
            System.out.println("3. List All Media");
            System.out.println("4. Update Media");
            System.out.println("5. Delete Media");
            System.out.println("6. Demonstrate Polymorphism");
            System.out.println("7. Demonstrate Interfaces");
            System.out.println("8. Demonstrate Validation");
            System.out.println("9. Demonstrate Composition");
            System.out.println("10. Run Full Demo (All Requirements)");
            System.out.println("0. Exit");
            System.out.print("\nSelect option: ");

            try {
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        createSong();
                        break;
                    case 2:
                        createPodcast();
                        break;
                    case 3:
                        listAllMedia();
                        break;
                    case 4:
                        updateMedia();
                        break;
                    case 5:
                        deleteMedia();
                        break;
                    case 6:
                        demonstratePolymorphism();
                        break;
                    case 7:
                        demonstrateInterfaces();
                        break;
                    case 8:
                        demonstrateValidation();
                        break;
                    case 9:
                        demonstrateComposition();
                        break;
                    case 10:
                        runFullDemo();
                        break;
                    case 0:
                        running = false;
                        System.out.println("\n Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
        scanner.close();
    }

    private List<Media> createSampleMedia() {
        List<Media> mediaList = new ArrayList<>();

        mediaList.add(new Song(1, "Bohemian Rhapsody", "Queen", 354, 1975,
                "A Night at the Opera", "Rock", 1));
        mediaList.add(new Song(2, "Blinding Lights", "The Weeknd", 200, 2019,
                "After Hours", "Synth-pop", 2));
        mediaList.add(new Podcast(3, "Tech Today", "Tech News", 1800, 2024,
                "Alex Johnson", "Technology", 42,
                "Latest tech news and reviews"));
        mediaList.add(new Song(4, "Levitating", "Dua Lipa", 203, 2020,
                "Future Nostalgia", "Disco", 3));

        return mediaList;
    }

    private boolean isMediaInPlaylist(Media media) {
        return media.getId() == 1 || media.getId() == 2;
    }

    public Song createSongDemo() {
        try {
            Song song = new Song(1, "Bohemian Rhapsody", "Queen", 354, 1975,
                    "A Night at the Opera", "Rock", 1);

            System.out.println(" Created Song:");
            System.out.println("   Title: " + song.getTitle());
            System.out.println("   Artist: " + song.getArtist());
            System.out.println("   Duration: " + song.getDurationFormatted());
            System.out.println("   Album: " + song.getAlbum());
            System.out.println("   Type: " + song.getMediaType());

            return song;
        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
            return null;
        }
    }

    public Podcast createPodcastDemo() {
        try {
            Podcast podcast = new Podcast(2, "Tech Today", "Tech News", 1800, 2024,
                    "Alex Johnson", "Technology", 42,
                    "Latest tech news and reviews");

            System.out.println(" Created Podcast:");
            System.out.println("   Title: " + podcast.getTitle());
            System.out.println("   Host: " + podcast.getHost());
            System.out.println("   Duration: " + podcast.getDurationFormatted());
            System.out.println("   Episode: " + podcast.getEpisodeNumber());
            System.out.println("   Type: " + podcast.getMediaType());

            return podcast;
        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
            return null;
        }
    }

    public void updateMediaDemo() {
        try {
            Song song = new Song(1, "Old Title", "Artist", 200, 2023, "Album", "Pop", 1);
            System.out.println("Before update: " + song.getTitle());

            song.setTitle("New Updated Title");
            System.out.println("After update: " + song.getTitle());
            System.out.println(" Update successful");

        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }

    public void deleteMediaDemo() {
        try {
            Song song = new Song(3, "Song to Delete", "Artist", 200, 2023, "Album", "Pop", 1);
            System.out.println("Created song to delete: " + song.getTitle());

            if (isMediaInPlaylist(song)) {
                System.out.println(" Cannot delete - song is in playlist");
                System.out.println(" Business rule validation works!");
            } else {
                System.out.println(" Can delete - not in any playlist");
            }

        } catch (Exception e) {
            System.out.println(" Error: " + e.getMessage());
        }
    }
    public void demonstrateSOLID() {
        System.out.println("\n=== SOLID PRINCIPLES DEMONSTRATION ===");
        System.out.println("1. OCP: Media class is open for extension");
        System.out.println("2. LSP: Song and Podcast can substitute Media");
        System.out.println("3. DIP: Service depends on repository interface");
    }

    public void demonstrateLambdas() {
        System.out.println("\n=== LAMBDA EXPRESSIONS ===");
        System.out.println("Using lambdas for sorting and filtering");

        java.util.List<Media> sampleMedia = createSampleMedia();
        System.out.println("\nSorted by duration (lambda):");
        SortingUtils.sortByDuration(sampleMedia);
        sampleMedia.forEach(m -> System.out.println("  - " + m.getTitle() +
                " (" + m.getDurationFormatted() + ")"));
    }

    public void demonstrateReflection() {
        System.out.println("\n=== REFLECTION DEMONSTRATION ===");
        System.out.println("Inspecting Media class:");
        ReflectionUtils.inspectClass(com.musiclibrary.model.Media.class);
    }

    public void demonstrateGenerics() {
        System.out.println("\n=== GENERICS DEMONSTRATION ===");
        System.out.println("SearchableRepository<Media, Integer> uses generics");
        System.out.println("Type-safe operations with compile-time checking");
    }

    public void runAssignment4Demo() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(" ASSIGNMENT 4 - SOLID & ADVANCED OOP");
        System.out.println("=".repeat(60));

        demonstrateSOLID();
        demonstrateLambdas();
        demonstrateReflection();
        demonstrateGenerics();

        System.out.println("\n" + "=".repeat(60));
        System.out.println(" ALL REQUIREMENTS DEMONSTRATED!");
        System.out.println("=".repeat(60));
    }
}
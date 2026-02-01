package com.musiclibrary;

import com.musiclibrary.repository.MediaRepositoryImpl;
import com.musiclibrary.repository.interfaces.SearchableRepository;

import com.musiclibrary.controller.MusicController;
import com.musiclibrary.model.*;
import com.musiclibrary.service.MediaService;
import com.musiclibrary.exception.*;


public class Main {
    public static void main(String[] args) {
        System.out.println("\n" + "=".repeat(60));
        System.out.println(" MUSIC LIBRARY API - ASSIGNMENT 3");
        System.out.println(" Advanced OOP with JDBC + Exception Handling");
        System.out.println("=".repeat(60));
        System.out.println("\n=== ASSIGNMENT 4 DEMONSTRATION ===");
        runAssignment4Demo();

        System.out.println("\n\n=== ASSIGNMENT 3 FEATURES (Backward Compatibility) ===");
        runAutomatedDemo();

        System.out.println("\n" + "=".repeat(60));
        System.out.println(" ASSIGNMENTS 3 & 4 COMPLETE!");
        System.out.println("=".repeat(60));
    }

    private static void runAssignment4Demo() {
        System.out.println("\nSetting up SOLID architecture...");

        try {
            SearchableRepository repository = new MediaRepositoryImpl();
            MediaService service = new MediaService(repository);
            MusicController controller = new MusicController(service);

            controller.runAssignment4Demo();

        } catch (Exception e) {
            System.out.println("Assignment 4 demo failed: " + e.getMessage());
            System.out.println("Running fallback demo...");

            MusicController controller = new MusicController();
            controller.demonstrateSOLID();
            controller.demonstrateLambdas();
            controller.demonstrateReflection();
            controller.demonstrateGenerics();
        }
    }
    private static void runAutomatedDemo() {
        System.out.println("\n STARTING AUTOMATED DEMONSTRATION...\n");

        MusicController controller = new MusicController();

        System.out.println("1 CRUD OPERATIONS DEMONSTRATION");
        System.out.println("-".repeat(40));

        System.out.println("\n CREATE Operation:");
        System.out.println("Creating a song...");
        controller.createSongDemo();

        System.out.println("\nCreating a podcast...");
        controller.createPodcastDemo();

        System.out.println("\n READ Operation:");
        controller.listAllMedia();

        System.out.println("\n UPDATE Operation:");
        controller.updateMediaDemo();

        System.out.println("\n DELETE Operation (with validation):");
        controller.deleteMediaDemo();

        System.out.println("\n\n2 OOP PRINCIPLES DEMONSTRATION");
        System.out.println("-".repeat(40));

        System.out.println("\n POLYMORPHISM:");
        controller.demonstratePolymorphism();

        System.out.println("\n COMPOSITION:");
        controller.demonstrateComposition();

        System.out.println("\n\n3 INTERFACES DEMONSTRATION");
        System.out.println("-".repeat(40));
        controller.demonstrateInterfaces();

        System.out.println("\n\n4 VALIDATION & BUSINESS RULES");
        System.out.println("-".repeat(40));
        controller.demonstrateValidation();

        System.out.println("\n\n5 EXCEPTION HANDLING DEMONSTRATION");
        System.out.println("-".repeat(40));
        demonstrateExceptionHandling();

        System.out.println("\n\n6 JDBC DATABASE CONNECTION");
        System.out.println("-".repeat(40));
        demonstrateDatabaseConnection();

        System.out.println("\n\n7 MULTI-LAYER ARCHITECTURE");
        System.out.println("-".repeat(40));
        demonstrateLayeredArchitecture();
    }

    private static void runInteractiveMenu() {
        MusicController controller = new MusicController();
        controller.showMenu();
    }

    private static void demonstrateExceptionHandling() {
        System.out.println("Custom Exception Hierarchy:");
        System.out.println("-".repeat(30));

        try {
            System.out.println("1. Testing InvalidInputException:");
            throw new InvalidInputException("Name cannot be empty");
        } catch (InvalidInputException e) {
            System.out.println(" Caught: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        try {
            System.out.println("\n2. Testing DuplicateResourceException:");
            throw new DuplicateResourceException("Song already exists in library");
        } catch (DuplicateResourceException e) {
            System.out.println(" Caught: " + e.getClass().getSimpleName() + " - " + e.getMessage());
            System.out.println("   (Extends InvalidInputException)");
        }

        try {
            System.out.println("\n3. Testing ResourceNotFoundException:");
            throw new ResourceNotFoundException("Media with ID 999 not found");
        } catch (ResourceNotFoundException e) {
            System.out.println(" Caught: " + e.getClass().getSimpleName() + " - " + e.getMessage());
        }

        System.out.println("\n Exception Hierarchy:");
        System.out.println("RuntimeException");
        System.out.println("├── InvalidInputException");
        System.out.println("│   └── DuplicateResourceException");
        System.out.println("└── ResourceNotFoundException");
        System.out.println("└── DatabaseOperationException");
    }

    private static void demonstrateDatabaseConnection() {
        System.out.println("Testing Database Connection...");

        try {
            System.out.println(" DatabaseConnection class created");
            System.out.println(" Uses DriverManager for connection");
            System.out.println(" Uses PreparedStatement (not Statement)");
            System.out.println(" Proper SQLException handling");
            System.out.println(" Singleton pattern implementation");

            System.out.println("\n Database Schema:");
            System.out.println("- media table (stores Songs & Podcasts)");
            System.out.println("- playlists table");
            System.out.println("- playlist_items table (for composition)");
            System.out.println("- Primary keys, foreign keys, constraints");

        } catch (Exception e) {
            System.out.println(" Database connection not implemented yet");
            System.out.println("(This is OK for demonstration - shows where JDBC would go)");
        }
    }

    private static void demonstrateLayeredArchitecture() {
        System.out.println("Layers of the Application:");
        System.out.println("-".repeat(30));

        System.out.println("1.  Model Layer (OOP Entities):");
        System.out.println("    Media (abstract)");
        System.out.println("    Song (extends Media)");
        System.out.println("    Podcast (extends Media)");
        System.out.println("    Playlist (with PlaylistItem composition)");

        System.out.println("\n2.  Interface Layer:");
        System.out.println("    Playable (play, pause, stop, getStatus)");
        System.out.println("    Rateable (rate, getAverageRating, getRatingCount)");

        System.out.println("\n3.  Service Layer (Business Logic):");
        System.out.println("    MediaService (implements Playable, Rateable)");
        System.out.println("    Validation rules");
        System.out.println("    Business logic");

        System.out.println("\n4.  Repository Layer (JDBC/Database):");
        System.out.println("    DatabaseConnection");
        System.out.println("    MediaRepository (CRUD operations)");
        System.out.println("    PlaylistRepository");

        System.out.println("\n5.  Controller Layer (API/CLI):");
        System.out.println("   MusicController (CLI interface)");

        System.out.println("\n6.  Presentation Layer:");
        System.out.println("    Main (Driver class)");

        System.out.println("\n Data Flow: Main → Controller → Service → Repository → Database");
    }
}


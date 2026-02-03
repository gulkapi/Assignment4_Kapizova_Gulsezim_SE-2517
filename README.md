# Assignment4_Kapizova_Gulsezim_SE-2517
Topic: Music Library API

# Project Overview:
This project extends the Assignment 3 Music Library API to implement SOLID design principles and advanced OOP features in Java. The application manages a digital music library with support for songs, podcasts, and playlists, demonstrating professional software architecture practices.

Learning Goals Achieved:
- Apply all 5 SOLID principles in a real multi-layer Java API

- Implement advanced Java features (Generics, Lambdas, Reflection)

- Strengthen object-oriented design skills

- Improve API structure and documentation

- Demonstrate professional code organization

# SOLID Principles Implementation
1. Single Responsibility Principle (SRP)
Each class has one clear responsibility:

-MediaService - Business logic and validation only

-MediaRepositoryImpl - Data access only (JDBC operations)

-MusicController - User input/output handling only

-SortingUtils - Sorting operations only

-ReflectionUtils - Runtime inspection only

2. Open/Closed Principle (OCP)
The Media abstract class is open for extension but closed for modification:

-New media types can be added without changing existing code

-Repository interfaces allow new implementations without affecting service layer

-Default methods in interfaces provide extensibility

3. Liskov Substitution Principle (LSP)
All subclasses properly substitute their parent:

-Song and Podcast can be used anywhere Media is expected

-No violations of base class contracts

-All abstract methods properly implemented

-No unexpected exceptions in overridden methods

4. Interface Segregation Principle (ISP)
Small, focused interfaces instead of "fat" ones:

-Playable - Only playback operations

-Rateable - Only rating operations

-Searchable<T> - Only search operations

-Validatable - Only validation operations

Clients don't depend on methods they don't use

5. Dependency Inversion Principle (DIP)
High-level modules don't depend on low-level implementations:

-MediaService depends on SearchableRepository interface, not concrete implementation

-Constructor injection used throughout

-Easy to swap implementations

-Testable with mock repositories

package com.example.bolasdejairo.repository;

import com.example.bolasdejairo.model.Game;
import org.springframework.data.repository.query.Param;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface GameRepository extends ReactiveCrudRepository<Game, Long> {

    Flux<Game> findAllByOrderByTitleAsc();

    Flux<Game> findAllByGenreIgnoreCaseOrderByTitleAsc(String genre);

    Flux<Game> findAllByAvailableOrderByTitleAsc(Boolean available);

    Flux<Game> findAllByGenreIgnoreCaseAndAvailableOrderByTitleAsc(String genre, Boolean available);

    @Query("""
            SELECT id, title, studio, genre, platform, release_year, rating, available, created_at, updated_at
            FROM games
            WHERE LOWER(title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(studio) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(platform) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
            ORDER BY title ASC
            """)
    Flux<Game> search(@Param("searchTerm") String searchTerm);

    @Query("""
            SELECT id, title, studio, genre, platform, release_year, rating, available, created_at, updated_at
            FROM games
            WHERE (LOWER(title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(studio) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(platform) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
              AND LOWER(genre) = LOWER(:genre)
            ORDER BY title ASC
            """)
    Flux<Game> searchByGenre(@Param("searchTerm") String searchTerm, @Param("genre") String genre);

    @Query("""
            SELECT id, title, studio, genre, platform, release_year, rating, available, created_at, updated_at
            FROM games
            WHERE (LOWER(title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(studio) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(platform) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
              AND available = :available
            ORDER BY title ASC
            """)
    Flux<Game> searchByAvailability(@Param("searchTerm") String searchTerm, @Param("available") Boolean available);

    @Query("""
            SELECT id, title, studio, genre, platform, release_year, rating, available, created_at, updated_at
            FROM games
            WHERE (LOWER(title) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(studio) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
               OR LOWER(platform) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
              AND LOWER(genre) = LOWER(:genre)
              AND available = :available
            ORDER BY title ASC
            """)
    Flux<Game> searchByGenreAndAvailability(
            @Param("searchTerm") String searchTerm,
            @Param("genre") String genre,
            @Param("available") Boolean available
    );
}

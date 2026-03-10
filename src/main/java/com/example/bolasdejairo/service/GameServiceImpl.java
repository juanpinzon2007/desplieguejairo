package com.example.bolasdejairo.service;

import com.example.bolasdejairo.dto.CreateGameRequest;
import com.example.bolasdejairo.dto.GameResponse;
import com.example.bolasdejairo.dto.UpdateGameRequest;
import com.example.bolasdejairo.exception.GameNotFoundException;
import com.example.bolasdejairo.model.Game;
import com.example.bolasdejairo.repository.GameRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;

    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Flux<GameResponse> findAll(String genre, Boolean available, String search) {
        Flux<Game> games;

        if (hasText(search)) {
            games = searchGames(genre, available, search.trim());
        } else if (hasText(genre) && available != null) {
            games = gameRepository.findAllByGenreIgnoreCaseAndAvailableOrderByTitleAsc(genre.trim(), available);
        } else if (hasText(genre)) {
            games = gameRepository.findAllByGenreIgnoreCaseOrderByTitleAsc(genre.trim());
        } else if (available != null) {
            games = gameRepository.findAllByAvailableOrderByTitleAsc(available);
        } else {
            games = gameRepository.findAllByOrderByTitleAsc();
        }

        return games.map(this::toResponse);
    }

    @Override
    public Mono<GameResponse> findById(Long id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException(id)))
                .map(this::toResponse);
    }

    @Override
    public Mono<GameResponse> create(CreateGameRequest request) {
        LocalDateTime now = LocalDateTime.now();

        Game game = new Game();
        apply(game, request.title(), request.studio(), request.genre(), request.platform(),
                request.releaseYear(), request.rating(), request.available());
        game.setCreatedAt(now);
        game.setUpdatedAt(now);

        return gameRepository.save(game).map(this::toResponse);
    }

    @Override
    public Mono<GameResponse> update(Long id, UpdateGameRequest request) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException(id)))
                .flatMap(game -> {
                    apply(game, request.title(), request.studio(), request.genre(), request.platform(),
                            request.releaseYear(), request.rating(), request.available());
                    game.setUpdatedAt(LocalDateTime.now());
                    return gameRepository.save(game);
                })
                .map(this::toResponse);
    }

    @Override
    public Mono<GameResponse> updateAvailability(Long id, boolean available) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException(id)))
                .flatMap(game -> {
                    game.setAvailable(available);
                    game.setUpdatedAt(LocalDateTime.now());
                    return gameRepository.save(game);
                })
                .map(this::toResponse);
    }

    @Override
    public Mono<Void> delete(Long id) {
        return gameRepository.findById(id)
                .switchIfEmpty(Mono.error(new GameNotFoundException(id)))
                .flatMap(gameRepository::delete);
    }

    private Flux<Game> searchGames(String genre, Boolean available, String search) {
        if (hasText(genre) && available != null) {
            return gameRepository.searchByGenreAndAvailability(search, genre.trim(), available);
        }
        if (hasText(genre)) {
            return gameRepository.searchByGenre(search, genre.trim());
        }
        if (available != null) {
            return gameRepository.searchByAvailability(search, available);
        }
        return gameRepository.search(search);
    }

    private void apply(Game game, String title, String studio, String genre, String platform,
                       Integer releaseYear, BigDecimal rating, Boolean available) {
        game.setTitle(title.trim());
        game.setStudio(studio.trim());
        game.setGenre(genre.trim());
        game.setPlatform(platform.trim());
        game.setReleaseYear(releaseYear);
        game.setRating(rating);
        game.setAvailable(available);
    }

    private GameResponse toResponse(Game game) {
        return new GameResponse(
                game.getId(),
                game.getTitle(),
                game.getStudio(),
                game.getGenre(),
                game.getPlatform(),
                game.getReleaseYear(),
                game.getRating(),
                game.getAvailable(),
                game.getCreatedAt(),
                game.getUpdatedAt()
        );
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}

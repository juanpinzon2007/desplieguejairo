package com.example.bolasdejairo.controller;

import com.example.bolasdejairo.dto.CreateGameRequest;
import com.example.bolasdejairo.dto.GameResponse;
import com.example.bolasdejairo.dto.UpdateGameRequest;
import com.example.bolasdejairo.service.GameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/games")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping
    public Flux<GameResponse> findAll(
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Boolean available,
            @RequestParam(required = false, name = "q") String search
    ) {
        return gameService.findAll(genre, available, search);
    }

    @GetMapping("/{id}")
    public Mono<GameResponse> findById(@PathVariable Long id) {
        return gameService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GameResponse> create(@RequestBody Mono<@Valid CreateGameRequest> request) {
        return request.flatMap(gameService::create);
    }

    @PutMapping("/{id}")
    public Mono<GameResponse> update(@PathVariable Long id, @RequestBody Mono<@Valid UpdateGameRequest> request) {
        return request.flatMap(body -> gameService.update(id, body));
    }

    @PatchMapping("/{id}/availability")
    public Mono<GameResponse> updateAvailability(@PathVariable Long id, @RequestParam boolean available) {
        return gameService.updateAvailability(id, available);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> delete(@PathVariable Long id) {
        return gameService.delete(id);
    }
}

package com.example.bolasdejairo.controller;

import com.example.bolasdejairo.dto.CreateGameRequest;
import com.example.bolasdejairo.dto.GameResponse;
import com.example.bolasdejairo.exception.GlobalExceptionHandler;
import com.example.bolasdejairo.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webflux.test.autoconfigure.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = {GameController.class, SystemController.class})
@Import(GlobalExceptionHandler.class)
class GameControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GameService gameService;

    @Test
    void shouldReturnGames() {
        when(gameService.findAll(null, null, null))
                .thenReturn(Flux.just(sampleGame()));

        webTestClient.get()
                .uri("/api/v1/games")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].title").isEqualTo("Hades");
    }

    @Test
    void shouldCreateGame() {
        when(gameService.create(any(CreateGameRequest.class)))
                .thenReturn(Mono.just(sampleGame()));

        String body = """
                {
                  "title": "Hades",
                  "studio": "Supergiant Games",
                  "genre": "Roguelike",
                  "platform": "PC",
                  "releaseYear": 2020,
                  "rating": 9.4,
                  "available": true
                }
                """;

        webTestClient.post()
                .uri("/api/v1/games")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.studio").isEqualTo("Supergiant Games");
    }

    @Test
    void shouldUpdateAvailability() {
        GameResponse updated = new GameResponse(
                1L, "Hades", "Supergiant Games", "Roguelike", "PC", 2020,
                BigDecimal.valueOf(9.4), false, LocalDateTime.now(), LocalDateTime.now()
        );

        when(gameService.updateAvailability(1L, false)).thenReturn(Mono.just(updated));

        webTestClient.patch()
                .uri("/api/v1/games/1/availability?available=false")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.available").isEqualTo(false);
    }

    private GameResponse sampleGame() {
        return new GameResponse(
                1L,
                "Hades",
                "Supergiant Games",
                "Roguelike",
                "PC",
                2020,
                BigDecimal.valueOf(9.4),
                true,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }
}

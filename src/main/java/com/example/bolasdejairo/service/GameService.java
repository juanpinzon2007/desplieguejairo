package com.example.bolasdejairo.service;

import com.example.bolasdejairo.dto.CreateGameRequest;
import com.example.bolasdejairo.dto.GameResponse;
import com.example.bolasdejairo.dto.UpdateGameRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface GameService {

    Flux<GameResponse> findAll(String genre, Boolean available, String search);

    Mono<GameResponse> findById(Long id);

    Mono<GameResponse> create(CreateGameRequest request);

    Mono<GameResponse> update(Long id, UpdateGameRequest request);

    Mono<GameResponse> updateAvailability(Long id, boolean available);

    Mono<Void> delete(Long id);
}

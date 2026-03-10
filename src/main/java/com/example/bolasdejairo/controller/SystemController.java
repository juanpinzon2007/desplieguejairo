package com.example.bolasdejairo.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@RestController
public class SystemController {

    private final String applicationName;

    public SystemController(@Value("${spring.application.name}") String applicationName) {
        this.applicationName = applicationName;
    }

    @GetMapping("/")
    public Mono<Map<String, Object>> home() {
        return Mono.just(Map.of(
                "application", applicationName,
                "status", "up",
                "timestamp", Instant.now().toString(),
                "docs", "/api/v1/games"
        ));
    }
}

package com.example.bolasdejairo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GameResponse(
        Long id,
        String title,
        String studio,
        String genre,
        String platform,
        Integer releaseYear,
        BigDecimal rating,
        Boolean available,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}

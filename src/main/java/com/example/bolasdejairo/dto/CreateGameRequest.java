package com.example.bolasdejairo.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateGameRequest(
        @NotBlank(message = "El titulo es obligatorio")
        @Size(max = 120, message = "El titulo no puede superar 120 caracteres")
        String title,

        @NotBlank(message = "El estudio es obligatorio")
        @Size(max = 100, message = "El estudio no puede superar 100 caracteres")
        String studio,

        @NotBlank(message = "El genero es obligatorio")
        @Size(max = 60, message = "El genero no puede superar 60 caracteres")
        String genre,

        @NotBlank(message = "La plataforma es obligatoria")
        @Size(max = 60, message = "La plataforma no puede superar 60 caracteres")
        String platform,

        @NotNull(message = "El ano de lanzamiento es obligatorio")
        @Min(value = 1970, message = "El ano minimo permitido es 1970")
        @Max(value = 2100, message = "El ano maximo permitido es 2100")
        Integer releaseYear,

        @NotNull(message = "La calificacion es obligatoria")
        @DecimalMin(value = "0.0", message = "La calificacion minima es 0.0")
        @DecimalMax(value = "10.0", message = "La calificacion maxima es 10.0")
        BigDecimal rating,

        @NotNull(message = "El estado de disponibilidad es obligatorio")
        Boolean available
) {
}

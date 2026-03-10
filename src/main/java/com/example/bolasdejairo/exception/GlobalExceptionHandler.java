package com.example.bolasdejairo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GameNotFoundException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleNotFound(GameNotFoundException exception) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), null);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleValidation(WebExchangeBindException exception) {
        List<Map<String, String>> errors = exception.getFieldErrors()
                .stream()
                .map(this::toFieldError)
                .toList();
        return buildResponse(HttpStatus.BAD_REQUEST, "La solicitud contiene errores de validacion", errors);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleResponseStatus(ResponseStatusException exception) {
        return buildResponse(
                HttpStatus.valueOf(exception.getStatusCode().value()),
                exception.getReason() == null ? "Solicitud invalida" : exception.getReason(),
                null
        );
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGeneric(Exception exception) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrio un error inesperado", null);
    }

    private Mono<ResponseEntity<Map<String, Object>>> buildResponse(
            HttpStatus status,
            String message,
            List<Map<String, String>> errors
    ) {
        Map<String, Object> body = errors == null
                ? Map.of("timestamp", Instant.now().toString(), "message", message)
                : Map.of("timestamp", Instant.now().toString(), "message", message, "errors", errors);

        return Mono.just(ResponseEntity.status(status).body(body));
    }

    private Map<String, String> toFieldError(FieldError fieldError) {
        return Map.of(
                "field", fieldError.getField(),
                "message", fieldError.getDefaultMessage() == null ? "Valor invalido" : fieldError.getDefaultMessage()
        );
    }
}

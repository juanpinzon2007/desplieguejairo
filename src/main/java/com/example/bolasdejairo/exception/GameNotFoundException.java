package com.example.bolasdejairo.exception;

public class GameNotFoundException extends RuntimeException {

    public GameNotFoundException(Long id) {
        super("No se encontro el videojuego con id " + id);
    }
}

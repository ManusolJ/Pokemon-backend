package com.pokemon.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(Long id) {
        super(String.format("User not found with id: %d", id));
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}

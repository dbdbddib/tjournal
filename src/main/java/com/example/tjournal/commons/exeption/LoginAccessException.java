package com.example.tjournal.commons.exeption;

public class LoginAccessException extends RuntimeException {
    public LoginAccessException(String message) {
        super(message);
    }
}

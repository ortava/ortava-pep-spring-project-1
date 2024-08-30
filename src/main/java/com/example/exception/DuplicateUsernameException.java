package com.example.exception;

public class DuplicateUsernameException extends Exception {
    public DuplicateUsernameException(){}

    public DuplicateUsernameException(String message) {
        super(message);
    }
}

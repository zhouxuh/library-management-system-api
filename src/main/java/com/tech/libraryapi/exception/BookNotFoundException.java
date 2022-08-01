package com.tech.libraryapi.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String str) {
        super("Could not find the book by " + str);
    }

    public BookNotFoundException(Long id) {
        super("Could not find the book by id: " + id);
    }

    public BookNotFoundException() {
        super("No book found.");
    }
}


package com.tech.libraryapi.exception;

public class BookAlreadyExistsException extends RuntimeException{
    public BookAlreadyExistsException(String isbn) {
        super("Book with ISBN " + isbn + " already exists.");
    }
}

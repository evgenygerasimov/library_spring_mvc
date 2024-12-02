package com.library_spring_mvc.exception;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) {super(message);}
}

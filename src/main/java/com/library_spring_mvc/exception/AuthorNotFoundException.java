package com.library_spring_mvc.exception;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(String message) {super(message);}
}

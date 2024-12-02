package com.library_spring_mvc.service;

import com.library_spring_mvc.entity.Author;
import com.library_spring_mvc.exception.AuthorNotFoundException;
import com.library_spring_mvc.repository.AuthorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorService authorService;

    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1L);
        author.setName("John Doe");
    }

    @Test
    void shouldAddAuthorTest() {
        when(authorRepository.save(author)).thenReturn(author);

        authorService.addAuthor(author);

        verify(authorRepository, times(1)).save(author);
    }

    @Test
    void shouldReturnAuthorByIdTest() {
        when(authorRepository.existsById(1L)).thenReturn(true);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        Optional<Author> result = authorService.findAuthorById(1L);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());
        verify(authorRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenAuthorByIdIsNotFoundTest() {
        when(authorRepository.existsById(1L)).thenReturn(false);

        assertThrows(AuthorNotFoundException.class, () -> authorService.findAuthorById(1L));
        verify(authorRepository, never()).findById(1L);
    }

    @Test
    void shouldReturnAllAuthorsTest() {
        when(authorRepository.existsByName("John Doe")).thenReturn(true);
        when(authorRepository.findByName("John Doe")).thenReturn(Optional.of(author));

        Optional<Author> result = authorService.findAuthorByName("John Doe");

        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(authorRepository, times(1)).findByName("John Doe");
    }

    @Test
    void shouldThrowExceptionWhenAuthorByNameIsNotFoundTest() {
        when(authorRepository.existsByName("Nonexistent Author")).thenReturn(false);

        assertThrows(AuthorNotFoundException.class, () -> authorService.findAuthorByName("Nonexistent Author"));
        verify(authorRepository, never()).findByName("Nonexistent Author");
    }

    @Test
    void shouldDeleteAuthorByIdTest() {
        when(authorRepository.existsById(1L)).thenReturn(true);

        authorService.deleteAuthorById(1L);

        verify(authorRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeleteAuthorByIdIsNotFoundTest() {
        when(authorRepository.existsById(1L)).thenReturn(false);

        assertThrows(AuthorNotFoundException.class, () -> authorService.deleteAuthorById(1L));
        verify(authorRepository, never()).deleteById(1L);
    }
}
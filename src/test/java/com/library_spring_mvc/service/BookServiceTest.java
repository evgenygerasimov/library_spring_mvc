package com.library_spring_mvc.service;

import com.library_spring_mvc.dto.BookDTO;
import com.library_spring_mvc.entity.Author;
import com.library_spring_mvc.entity.Book;
import com.library_spring_mvc.exception.AuthorNotFoundException;
import com.library_spring_mvc.exception.BookNotFoundException;
import com.library_spring_mvc.repository.AuthorRepository;
import com.library_spring_mvc.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    private Author author;
    private Book book;
    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1L);
        author.setName("John Doe");

        book = new Book(1L, "Test Book", author, "Fiction");

        bookDTO = new BookDTO();
        bookDTO.setTitle("Updated Book");
        bookDTO.setGenre("Horror");
        bookDTO.setAuthorId(1L);
    }

    @Test
    void shouldReturnAllBooksWithoutPaginationTest() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        Page<Book> result = bookService.findAll(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(bookRepository, times(1)).findAll(pageable);
    }

    @Test
    void shouldReturnBookByIdTest() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("Test Book", result.get().getTitle());
        verify(bookRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFoundTest() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.findById(1L));
        verify(bookRepository, never()).findById(1L);
    }

    @Test
    void shouldReturnBookByTitleTest() {
        when(bookRepository.existsByTitle("Test Book")).thenReturn(true);
        when(bookRepository.findByTitle("Test Book")).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.findByTitle("Test Book");

        assertTrue(result.isPresent());
        assertEquals("Test Book", result.get().getTitle());
        verify(bookRepository, times(1)).findByTitle("Test Book");
    }

    @Test
    void shouldThrowExceptionWhenBookByTitleNotFoundTest() {
        when(bookRepository.existsByTitle("Nonexistent")).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.findByTitle("Nonexistent"));
        verify(bookRepository, never()).findByTitle("Nonexistent");
    }

    @Test
    void shouldReturnAllBooksByAuthorWithoutPaginationTest() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(authorRepository.existsByName("John Doe")).thenReturn(true);
        when(bookRepository.findAllByAuthor_Name("John Doe", pageable)).thenReturn(bookPage);

        Page<Book> result = bookService.findAllByAuthor("John Doe", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(bookRepository, times(1)).findAllByAuthor_Name("John Doe", pageable);
    }

    @Test
    void shouldThrowExceptionWhenAuthorNotFoundTest() {
        Pageable pageable = PageRequest.of(0, 5);

        when(authorRepository.existsByName("Any Book")).thenReturn(false);

        assertThrows(AuthorNotFoundException.class, () -> bookService.findAllByAuthor("Any Book", pageable));
        verify(bookRepository, never()).findAllByAuthor_Name("Any Book", pageable);
    }

    @Test
    void shouldSaveBookTest() {
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.save(bookDTO);

        assertNotNull(result);
        assertEquals("Test Book", book.getTitle());
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void shouldThrowExceptionWhenAuthorNotFoundOnSaveTest() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> bookService.save(bookDTO));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void shouldUpdateBookTest() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.update(1L, bookDTO);

        assertNotNull(result);
        assertEquals("Updated Book", book.getTitle());
        assertEquals("Horror", book.getGenre());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFoundOnUpdateTest() {
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(BookNotFoundException.class, () -> bookService.update(1L, bookDTO));
        verify(authorRepository, never()).findById(1L);
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void shouldThrowExceptionWhenAuthorNotFoundOnUpdateTest() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AuthorNotFoundException.class, () -> bookService.update(1L, bookDTO));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void shouldDeleteBookTest() {
        when(bookRepository.existsById(1L)).thenReturn(true);

        bookService.delete(1L);

        verify(bookRepository, times(1)).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFoundOnDeleteTest() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        assertThrows(BookNotFoundException.class, () -> bookService.delete(1L));
        verify(bookRepository, never()).deleteById(1L);
    }
}

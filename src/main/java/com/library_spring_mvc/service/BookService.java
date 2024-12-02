package com.library_spring_mvc.service;

import com.library_spring_mvc.dto.BookDTO;
import com.library_spring_mvc.entity.Author;
import com.library_spring_mvc.entity.Book;
import com.library_spring_mvc.exception.AuthorNotFoundException;
import com.library_spring_mvc.exception.BookNotFoundException;
import com.library_spring_mvc.repository.AuthorRepository;
import com.library_spring_mvc.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable);
    }

    public Optional<Book> findById(Long id) {
        if (!bookRepository.existsById(id)){
            throw new BookNotFoundException("Book not found with id: " + id);
        }
        return bookRepository.findById(id);
    }

    public Optional<Book> findByTitle(String title) {
        if (!bookRepository.existsByTitle(title)){
            throw new BookNotFoundException("Book not found with title: " + title);
        }
        return bookRepository.findByTitle(title);
    }

    public Page<Book> findAllByAuthor(String nameAuthor, Pageable pageable) {
        if (!authorRepository.existsByName(nameAuthor)){
            throw new AuthorNotFoundException("Author not found with name: " + nameAuthor);
        }
        return bookRepository.findAllByAuthor_Name(nameAuthor, pageable);
    }

    public Book save(BookDTO bookDTO) {
        Book book = new Book();
        Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with name: " + bookDTO.getAuthorId()));

        book.setTitle(bookDTO.getTitle());
        book.setGenre(bookDTO.getGenre());
        book.setAuthor(author);
        return bookRepository.save(book);
    }

    public Book update(Long id, BookDTO bookDTO) {
        Book existingBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException("Book not found with id: " + id));

        Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with name: " + bookDTO.getAuthorId()));

        existingBook.setTitle(bookDTO.getTitle());
        existingBook.setGenre(bookDTO.getGenre());
        existingBook.setAuthor(author);
        return bookRepository.save(existingBook);
    }

    public void delete(Long id) {
        if (!bookRepository.existsById(id)){
            throw new BookNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }
}
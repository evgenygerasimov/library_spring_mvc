package com.library_spring_mvc.service;

import com.library_spring_mvc.entity.Author;
import com.library_spring_mvc.exception.AuthorNotFoundException;
import com.library_spring_mvc.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void addAuthor(Author author) {
        authorRepository.save(author);
    }

    public Optional<Author> findAuthorById(long id) {
        if (!authorRepository.existsById(id)) {
            throw new AuthorNotFoundException("Author not found with id: " + id);
        }
        return authorRepository.findById(id);
    }

    public Optional<Author> findAuthorByName(String name) {
        if (!authorRepository.existsByName(name)) {
            throw new AuthorNotFoundException("Author not found with id: " + name);
        }
        return authorRepository.findByName(name);
    }

    public void deleteAuthorById(long id) {
        if (!authorRepository.existsById(id)) {
            throw new AuthorNotFoundException("Author not found with id: " + id);
        }
        authorRepository.deleteById(id);
    }
}

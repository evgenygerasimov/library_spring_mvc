package com.library_spring_mvc.repository;

import com.library_spring_mvc.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Optional<Book> findByTitle(String title);

    Page<Book> findAllByAuthor_Name(String authorName, Pageable pageable);

    boolean existsByTitle(String title);
}

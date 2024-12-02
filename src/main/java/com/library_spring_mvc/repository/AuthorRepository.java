package com.library_spring_mvc.repository;

import com.library_spring_mvc.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorRepository extends JpaRepository<Author, Long> {

    boolean existsByName(String name);

    Optional<Author> findByName(String name);
}

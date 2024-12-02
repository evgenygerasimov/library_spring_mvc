package com.library_spring_mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.library_spring_mvc.dto.BookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Sql(scripts = "/schema.sql")
@Sql(scripts = "/data.sql")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private BookDTO bookDTO;

    @BeforeEach
    void setUp() {
        bookDTO = new BookDTO();
        bookDTO.setTitle("Book 1");
        bookDTO.setGenre("Genre 1");
        bookDTO.setAuthorId(1L);
    }

    @Test
    void shouldReturnAllBooksWithPaginationTest() throws Exception {
        mockMvc.perform(get("/api/books")
                        .param("page", "0")
                        .param("size", "1")
                        .param("sort", "title,asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("Anna Karenina"));
    }

    @Test
    void shouldReturnBookByIdTest() throws Exception {
        mockMvc.perform(get("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("War and Peace"));
    }

    @Test
    void shouldReturnBookByTitleTest() throws Exception {
        mockMvc.perform(get("/api/books/title/{title}", "War and Peace")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("War and Peace"));
    }

    @Test
    void shouldReturnAllBookByAuthorNameWithPaginationTest() throws Exception {
        mockMvc.perform(get("/api/books/author_name/{name}", "Lev Tolstoy")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("War and Peace"))
                .andExpect(jsonPath("$.content[1].title").value("Fillipok"));
    }

    @Test
    void shouldReturnCreatedBookTest() throws Exception {
        mockMvc.perform(post("/api/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.genre").value("Genre 1"))
                .andExpect(jsonPath("$.title").value("Book 1"))
                .andExpect(jsonPath("$.author.name").value("Lev Tolstoy"));
    }

    @Test
    void shouldReturnUpdatedBookTest() throws Exception {
        bookDTO.setTitle("Updated Book");

        mockMvc.perform(put("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Updated Book"))
                .andExpect(jsonPath("$.author.name").value("Lev Tolstoy"));
    }

    @Test
    void shouldReturnNoContentWhenBookIsDeletedTest() throws Exception {
        mockMvc.perform(delete("/api/books/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}

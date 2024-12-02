package com.library_spring_mvc.dto;

import lombok.Data;

@Data
public class BookDTO {
    private String title;
    private String genre;
    private Long authorId;
}

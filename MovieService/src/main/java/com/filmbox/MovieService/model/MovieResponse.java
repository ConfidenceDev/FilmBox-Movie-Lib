package com.filmbox.MovieService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieResponse {
    private Long id;
    private String posterId;
    private String title;
    private String summary;
    private Integer year;
    private String genre;
    private List<String> actors;
    private List<String> tags;
    private String message;
    private Instant createdAt;
}

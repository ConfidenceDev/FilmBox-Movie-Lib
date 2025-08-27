package com.filmbox.MovieService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieRequest {
    private String title;
    private String summary;
    private Integer year;
    private String genre;        // name of genre
    private List<String> actors; // actor names
    private List<String> tags;   // tag names
}
package com.filmbox.MovieService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tag;

    // A tag can belong to many movies
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Set<Movie> movies = new HashSet<>();
}

package com.filmbox.MovieService.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Log4j2
public class Actor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    // Many actors can be in one movie
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id")
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    private Movie movie;
}

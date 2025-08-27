package com.filmbox.MovieService.respository;


import com.filmbox.MovieService.entity.Genre;
import com.filmbox.MovieService.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query("SELECT DISTINCT m FROM Genre g JOIN g.movies m WHERE LOWER(g.genre) LIKE LOWER(CONCAT('%', :genre, '%'))")
    Page<Movie> findMoviesByGenre(@Param("genre") String genre, Pageable pageable);

    Optional<Genre> findByGenreIgnoreCase(String genre);
}
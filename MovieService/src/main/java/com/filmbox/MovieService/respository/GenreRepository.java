package com.filmbox.MovieService.respository;


import com.filmbox.MovieService.entity.Genre;
import com.filmbox.MovieService.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query("SELECT DISTINCT m FROM Genre g JOIN g.movies m " + "WHERE LOWER(g.genre) LIKE LOWER(CONCAT('%', :genre, '%'))")
    List<Movie> findMoviesByGenre(@Param("genre") String genre);

    Optional<Genre> findByGenreIgnoreCase(String genre);
}
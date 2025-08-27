package com.filmbox.MovieService.respository;


import com.filmbox.MovieService.entity.Movie;
import com.filmbox.MovieService.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    @Query("SELECT DISTINCT m FROM Tag t JOIN t.movies m WHERE LOWER(t.tag) LIKE LOWER(CONCAT('%', :tag, '%'))")
    Page<Movie> findMoviesByTag(@Param("tag") String tag, Pageable pageable);

    Optional<Tag> findByTagIgnoreCase(String tag);
}